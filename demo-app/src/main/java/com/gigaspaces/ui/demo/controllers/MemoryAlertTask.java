package com.gigaspaces.ui.demo.controllers;

import com.gigaspaces.annotation.SupportCodeChange;
import com.gigaspaces.common.Bundle;
import com.gigaspaces.internal.jvm.JVMHelper;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.executor.Task;
import org.openspaces.core.executor.TaskGigaSpace;
import org.openspaces.core.executor.TaskRoutingProvider;
import org.springframework.dao.DataAccessException;


import java.lang.management.ManagementFactory;

import static com.gigaspaces.common.Constants.*;

@SupportCodeChange(id = "1")
public class MemoryAlertTask implements Task<Integer>, TaskRoutingProvider {
    private int value;
    private int duration;

    @TaskGigaSpace
    private transient GigaSpace gigaSpace;

    public MemoryAlertTask(int value, int duration) {
        this.value = value;
        this.duration = duration;
    }

    public Integer execute() {
        doMemoryAlert();

        return 0;
    }

    public Integer getRouting() {
        return this.value;
    }

    private void doMemoryAlert() {
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        boolean toStop = false;
        long heapMaxJvmHelper = JVMHelper.getDetails().getMemoryHeapMax();
        System.out.println("Start writing Bundles to space");

        getMaxAndUsedMemory();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try{
            while (((currentTime - startTime) < duration * MILLISECONDS_IN_SECOND) && !toStop) {
                long usedMxBean = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
                double usedMemoryPercentage =  usedMxBean / 1.0 / heapMaxJvmHelper;
                System.gc();
                getMaxAndUsedMemory();

                if (usedMemoryPercentage < 0.75){
                    long iterationStartTime = System.currentTimeMillis();
                    Bundle[] bundles = new Bundle[NUM_OF_BUNDLES_TO_WRITE];
                    for (int i = 0; i < NUM_OF_BUNDLES_TO_WRITE ; i++) {
                        bundles[i] = Bundle.createBundle();
                    }
                    gigaSpace.writeMultiple(bundles);
                    currentTime = System.currentTimeMillis();
                    long differenceTime = currentTime - iterationStartTime;

                    if ((differenceTime + currentTime) > (startTime + duration * MILLISECONDS_IN_SECOND)) {
                        toStop = true;
                    }
                }
                else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("Failed with error: " + e);
                    }
                }
            }

            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int count = gigaSpace.count(new Bundle());
            System.out.println("Finish writing " + count + " Bundles");
        } catch (DataAccessException e) {
            System.out.println("Failed with error: " + e);
        } finally {
            gigaSpace.clear(new Bundle());
            int count2 = gigaSpace.count(new Bundle());
            System.out.println("After clear: there are " + count2 + " Bundles");
        }

        getMaxAndUsedMemory();
    }


    public void getMaxAndUsedMemory(){
        long heapMaxJvmHelper = JVMHelper.getDetails().getMemoryHeapMax();
        double heapJvmHelperGB = heapMaxJvmHelper / 1024.0 / 1024 / 1024;

        long maxHeapRuntime = Runtime.getRuntime().maxMemory();
        double maxHeapGBRuntime = maxHeapRuntime / 1024.0 / 1024 / 1024;

        long maxHeapMxBean = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax();
        double maxHeapGBMxBean = maxHeapMxBean / 1024.0 / 1024 / 1024;

        System.out.println("++++++++++++++++MAx heap:++++++++++++");
//        System.out.println("Only accurate with ui:JVM helper: " + heapMaxJvmHelper + "in GB: " + heapJvmHelperGB);
//        System.out.println("Runtime is: " + maxHeapRuntime + "in GB: " + maxHeapGBRuntime);
//        System.out.println("MxBean is: " + maxHeapMxBean + "in GB: " + maxHeapGBMxBean);
        System.out.println("|      Runtime    |    MXBean     |     JVMHelper    |  same ?  |  same2? |");
        System.out.println("|" + maxHeapRuntime + " | " + maxHeapMxBean +" | " + heapMaxJvmHelper +" | " + (maxHeapRuntime == maxHeapMxBean) +" | "
                + (maxHeapMxBean == heapMaxJvmHelper) + "|");



        long usedMxBean = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
        double usedMxBeanGB = usedMxBean / 1024.0 /1024 /1024;
        double usedMxBeanPercentage = usedMxBean / 1.0 /maxHeapMxBean;

        double usedMemorydivideMaxJvm = usedMxBean / 1.0 / heapMaxJvmHelper;

        long usedRuntime = maxHeapRuntime - Runtime.getRuntime().freeMemory();
        double usedRuntimeGB = usedRuntime / 1024.0 / 1024 / 1024;
        double usedRuntimePercentage = usedRuntime / 1.0 / maxHeapRuntime;

//        System.out.println("++++++++++++++++++++used Memory:+++++++++++++++++++++++ ");
//        System.out.println("|      Runtime    |    MXBean     |     same ?     |");
//        System.out.println("| " + usedRuntime +" | " + usedMxBean +" | " + (usedRuntime == usedMxBean) );
//        System.out.println("Nothing accurate: Runtime is: " + usedRuntime + " in GB: " + usedRuntimeGB + " in %: " + usedRuntimePercentage);
//        System.out.println("Only values Accurate, not percentage: MxBean is: " + usedMxBean + "in GB: " + usedMxBeanGB + "in %: " + usedMxBeanPercentage);
//        System.out.println("Accurate with UI :JVM(MxBean): " + usedMxBean + "in GB: " + usedMxBeanGB + "in %: " + usedMemorydivideMaxJvm);

//        System.out.println("Max Memory is: " + heapJvmHelperGB + " Used Memory: " + usedMxBeanGB + " Used Memory in %: " + usedMemorydivideMaxJvm);
    }
}

