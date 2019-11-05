package com.gigaspaces.ui.demo.controllers;

import com.gigaspaces.rest.client.java.api.ContainersApi;
import com.gigaspaces.rest.client.java.api.HostsApi;
import com.gigaspaces.rest.client.java.api.ProcessingUnitsApi;
import com.gigaspaces.rest.client.java.invoker.ApiException;
import com.gigaspaces.rest.client.java.invoker.Configuration;
import com.gigaspaces.rest.client.java.model.CreateContainerRequest;
import com.gigaspaces.rest.client.java.model.Host;
import com.gigaspaces.rest.client.java.model.ProcessingUnit;
import com.gigaspaces.rest.client.java.model.ProcessingUnitInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DemoController {


    private final ProcessingUnitsApi processingUnitsApi;
    private final ContainersApi containersApi;
    private final HostsApi hostsApi;

    public DemoController() {
        Configuration.getDefaultApiClient().setBasePath("http://192.168.35.164:8090/v2"); //TODO make configurable
        processingUnitsApi = new ProcessingUnitsApi();
        containersApi = new ContainersApi();
        hostsApi = new HostsApi();
    }

    @GetMapping(value = "/services")
    public List<ProcessingUnit> getServices() throws ApiException {
        return processingUnitsApi.pusGet();
    }


    @PostMapping(value = "/service/scale")
    public String scaleService(@RequestParam String serviceName, @RequestParam String upOrDown) throws ApiException {
        if (upOrDown.equalsIgnoreCase("up")) {
            processingUnitsApi.pusIdInstancesPost(serviceName);
            return "Incremented instance for "+serviceName+ " service";
        } else if (upOrDown.equalsIgnoreCase("down")) {
            List<ProcessingUnitInstance> instances = processingUnitsApi.pusIdInstancesGet(serviceName);
            if (instances.size() == 0) {
                throw new IllegalArgumentException("Can't decrement service " + serviceName);
            }
            List<String> sorted = instances.stream().map(ProcessingUnitInstance::getId).sorted().collect(Collectors.toList());
            String instanceIdToRemove = sorted.get(sorted.size() - 1);
            processingUnitsApi.pusIdInstancesInstanceIdDelete(serviceName, instanceIdToRemove);
            return "Instance " + instanceIdToRemove +" was decremented";
        } else {
            throw new IllegalArgumentException("Unhandled behavior for upOrDown=["+upOrDown+"]");
        }
    }

    @GetMapping("/hosts")
    public List<Host> getHosts() throws ApiException {
        return hostsApi.hostsGet();
    }

    @PostMapping(value = "/container")
    public String createContainer(@RequestParam String hostName) throws ApiException {
        CreateContainerRequest request = new CreateContainerRequest();
        request.setHost(hostName);
        request.addVmArgumentsItem("-Dcom.gigaspaces.grid.gsc.serviceLimit=1");
        containersApi.containersPost(request);
        return "GSC created on " + hostName;
    }

    @DeleteMapping(value = "/container")
    public String removeContainer(@RequestParam String containerId) throws ApiException {
        containersApi.containersIdDelete(containerId);
        return "GSC " + containerId + " was removed";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }



}