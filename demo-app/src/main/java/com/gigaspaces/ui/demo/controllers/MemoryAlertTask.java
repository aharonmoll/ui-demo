package com.gigaspaces.ui.demo.controllers;

import com.gigaspaces.annotation.SupportCodeChange;
import com.gigaspaces.common.Bundle;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.executor.Task;
import org.openspaces.core.executor.TaskGigaSpace;
import org.openspaces.core.executor.TaskRoutingProvider;

import static com.gigaspaces.common.Constants.*;

@SupportCodeChange(id = "10")
public class MemoryAlertTask implements Task<Integer>, TaskRoutingProvider, Runnable {
    private int value;
    private int duration;

    @TaskGigaSpace
    private transient GigaSpace gigaSpace;


    public MemoryAlertTask(int value, int duration) {
        this.value = value;
        this.duration = duration;
    }

    public Integer execute() {
      new Thread (this::run).start();
      run();
        return 0;

    }

    public Integer getRouting() {
        return this.value;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        boolean toStop = false;
        System.out.println("Start writing Bundles to space10");

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
        }

        int count = gigaSpace.count(new Bundle());
        System.out.println("Finish writing " + count + " Bundles");
        gigaSpace.clear(new Bundle());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int count2 = gigaSpace.count(new Bundle());
        System.out.println("After clear: there are " + count2 + " Bundles");
        System.gc();
        System.out.println("Cleanup completed...");
    }
}

