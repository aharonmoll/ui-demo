//var baseUrl = "http://localhost:8080/web"
var baseUrl = ""

function clearSelect(select, keep) {
    while (select.length > keep) {
        select.remove(select.length - 1);
    }
}


function updateInstances(serviceName, elem) {
    var service = allServices.filter(function (service) { return service.name == serviceName; })[0];
    clearSelect(elem, 0);
    service.instances.forEach(function (instance) {
        var option = document.createElement("option");
        option.text = instance;
        elem.add(option);
    });
}

function addOption(select, text, value) {
     var option = document.createElement("option");
     option.text = text;
     if (value) {
        option.value = value;
     }
     select.add(option);
}

function updateContainers(hostName, elem) {
    var host = hosts.filter(function (host) { return host.name == hostName; })[0];
    console.log(host);
    clearSelect(elem, 0);
    if (host.containers.length == 0) {
            addOption(elem, "No containers on selected host");
    } else {
        host.containers.forEach(function (container) {
            addOption(elem, container);
        });
    }
}

/// APIs
function createContainer(elem) {
    var parent = $(elem).parents(".form-horizontal");
    var selects = parent.find("select");
    var result = parent.find(".result");
    var selectedHost = selects[0].value;
    if (!selectedHost) {
        alert("Please select a host");
        return;
    }

    elem.disabled = true;
    $.post( baseUrl + "/container?hostName="+selectedHost)
     .done(function( data ) {
        result.html(data);
        elem.disabled = false;
    })
    .fail(function (xhr, textStatus, errorThrown) {
        result.html(xhr.responseText);
        elem.disabled = false;
    });
}

function removeContainer(elem) {
    var parent = $(elem).parents(".form-horizontal");
    var selects = parent.find("select");
    var result = parent.find(".result");
    var selectedContainer = selects[1].value;
    if (!selectedContainer) {
        alert("Please select a container");
        return;
    }

    elem.disabled = true;
    $.ajax({
        url: baseUrl + "/container?containerId="+selectedContainer,
        type: 'DELETE',
        success: function( data ) {
                         selects[1].remove(selects[1].selectedIndex);
                         result.html(data);
                         elem.disabled = false;
                     },
        error: function (xhr, textStatus, errorThrown) {
                      result.html(xhr.responseText);
                      elem.disabled = false;
                  }
    });
}

function scaleUp(elem) {
    var parent = $(elem).parents(".form-horizontal");
    var selects = parent.find("select");
    var result = parent.find(".result");
    var selectedService = selects[0].value;
    if (!selectedService) {
        alert("Please select a service");
        return;
    }

    elem.disabled = true;
    $.post( baseUrl + "/service/scale?upOrDown=up&serviceName="+selectedService)
     .done(function( data ) {
        result.html(data);
        elem.disabled = false;
    })
    .fail(function (xhr, textStatus, errorThrown) {
        result.html(xhr.responseText);
        elem.disabled = false;
    });
}

function scaleDown(elem) {
    var parent = $(elem).parents(".form-horizontal");
    var selects = parent.find("select");
    var result = parent.find(".result");

    var selectedService = selects[0].value;
    if (!selectedService) {
        alert("Please select a service");
        return;
    }
    elem.disabled = true;

    $.post( baseUrl + "/service/scale?upOrDown=down&serviceName="+selectedService)
     .done(function( data ) {
        result.html(data);
        elem.disabled = false;
    })
    .fail(function (xhr, textStatus, errorThrown) {
        result.html(xhr.responseText);
        elem.disabled = false;
    });
}


/// End of APIs

$(document).ready(function() {

    $(".stateful-services").change(function(e) {
        var instancesSelect = $(this).parents(".form-horizontal").find(".instances")[0];
        if (instancesSelect) {
            updateInstances(this.value, instancesSelect);
        }
    });

    $(".stateless-services").change(function(e) {
        var instancesSelect = $(this).parents(".form-horizontal").find(".instances")[0];
        if (instancesSelect) {
            updateInstances(this.value, instancesSelect);
        }
    });

    $(".hosts").change(function(e) {
        var containersSelect = $(this).parents(".form-horizontal").find(".host-containers")[0];
        if (containersSelect) {
            updateContainers(this.value, containersSelect);
        }
    });

    $.get( baseUrl + "/services", function( data ) {

      allServices = data;
      statefulServices = data.filter(function (service) {
                          return service.topology.partitions != null;
                      });

      statelessServices = data.filter(function (service) {
                          return service.topology.partitions == null;
                      });

      scalableServices = data.filter(function (service) {
                          return service.scalable;
                      });

      $(".stateful-services").each(function (i, select) {
        clearSelect(select, 1);
        statefulServices.forEach(function (service) {
            addOption(select, service.name +" (" + service.instances.length + ")", service.name);
        });
      });

      $(".stateless-services").each(function (i, select) {
        clearSelect(select, 1);
        statelessServices.forEach(function (service) {
            addOption(select, service.name +" (" + service.instances.length + ")", service.name);
        });
      });

      $(".scalable-services").each(function (i, select) {
        clearSelect(select, 1);
        scalableServices.forEach(function (service) {
            addOption(select, service.name +" (" + service.instances.length + ")", service.name);
        });
      });

    });

    $.get( baseUrl + "/hosts", function( data ) {
        hosts = data;
        $(".hosts").each(function (i, select) {
            clearSelect(select, 1);
            hosts.forEach(function (host) {
                addOption(select, host.name + " ("+host.containers.length+")", host.name)
            });
        });
    });


});