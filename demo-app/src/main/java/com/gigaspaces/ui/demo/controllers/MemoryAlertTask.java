package com.gigaspaces.ui.demo.controllers;

import com.gigaspaces.annotation.SupportCodeChange;
import com.gigaspaces.common.Bundle;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.executor.Task;
import org.openspaces.core.executor.TaskGigaSpace;
import org.openspaces.core.executor.TaskRoutingProvider;

import static com.gigaspaces.common.Constants.*;

@SupportCodeChange(id = "5")
public class MemoryAlertTask implements Task<Integer>, TaskRoutingProvider {
    private int value;
    private int duration;

    @TaskGigaSpace
    private transient GigaSpace gigaSpace;


    public MemoryAlertTask(int value, int duration) {
        this.value = value;
        this.duration = duration;
    }

    public Integer execute() {  //Todo -code design
        try {
            doMemoryAlert();
        } catch (Exception e) {
            System.out.println("Failed with error: " + e);
        } finally {
            gigaSpace.clear(new Bundle());
            int count2 = gigaSpace.count(new Bundle());
            System.out.println("After clear: there are " + count2 + " Bundles");
        }

        return 0;
    }

    public Integer getRouting() {
        return this.value;
    }

    private void doMemoryAlert() {
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        boolean toStop = false;
        System.out.println("Start writing Bundles to space6");

        double totalMemory = Runtime.getRuntime().maxMemory() / 1024;
        double freeMemory;
        double usedMemory;

        while (((currentTime - startTime) / MILLISECONDS_IN_SECOND < duration * MILLISECONDS_IN_SECOND) && !toStop) {
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

            freeMemory = Runtime.getRuntime().freeMemory() / 1024;
            usedMemory = ((totalMemory - freeMemory) / totalMemory);
            System.out.println("Used memory is:" + usedMemory);
            if(usedMemory > 0.85){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    System.out.println("Failed with error: " + e);
                }
                System.out.println("between checks");
                freeMemory = Runtime.getRuntime().freeMemory() / 1024;
                usedMemory = ((totalMemory - freeMemory) / totalMemory);
                if(usedMemory > 0.85){
                    System.out.println("more than 0.85 heap");
                    toStop = true;
                }
                else{
                    System.out.println("now beneath");
                }
            }
        }

        totalMemory = Runtime.getRuntime().maxMemory() / 1024;
        freeMemory = Runtime.getRuntime().freeMemory() / 1024;
        usedMemory = (totalMemory - freeMemory) / totalMemory;
        System.out.println("Used memory is:" + usedMemory);

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int count = gigaSpace.count(new Bundle());
        System.out.println("Finish writing " + count + " Bundles");
    }
}

