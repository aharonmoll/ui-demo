package com.gigaspaces.ui.demo.controllers;

import com.gigaspaces.annotation.SupportCodeChange;
import com.gigaspaces.common.Bundle;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.executor.Task;
import org.openspaces.core.executor.TaskGigaSpace;
import org.openspaces.core.executor.TaskRoutingProvider;

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
        System.out.println("Start writing Bundles to space");

        double maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024 / 1024.0;
        double freeMemory;
        double usedMemory;
        double usedMemoryGB;
        System.out.println("Max memory in GB: " + maxMemory + " in MB: " + (maxMemory * 1024) + " in KB: " + (maxMemory * 1024 *1024));

        while (((currentTime - startTime) / MILLISECONDS_IN_SECOND < duration * MILLISECONDS_IN_SECOND) && !toStop) {
            freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024 / 1024.0;
            usedMemory = ((maxMemory - freeMemory) / maxMemory);
            usedMemoryGB = maxMemory -freeMemory;

            System.out.println("Used memory in %: " + usedMemory + " in GB: " + usedMemoryGB + " in KB: " + usedMemoryGB * 1024 *1024);
            if (usedMemory < 0.82){
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

        freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024 / 1024.0;
        usedMemory = (maxMemory - freeMemory) / maxMemory;
        System.out.println("After time passed: used memory is: " + usedMemory);

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int count = gigaSpace.count(new Bundle());
        System.out.println("Finish writing " + count + " Bundles");
    }
}

