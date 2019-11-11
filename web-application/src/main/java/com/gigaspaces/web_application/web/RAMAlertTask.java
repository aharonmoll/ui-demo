package com.gigaspaces.web_application.web;

import com.gigaspaces.annotation.SupportCodeChange;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.executor.Task;
import org.openspaces.core.executor.TaskGigaSpace;
import org.openspaces.core.executor.TaskRoutingProvider;
import static com.gigaspaces.common.Constants.*;
import com.gigaspaces.common.Bundle;

@SupportCodeChange(id="1")
public class RAMAlertTask implements Task<Integer>, TaskRoutingProvider {
    private int value;

    @TaskGigaSpace
    private transient GigaSpace gigaSpace;


    public RAMAlertTask(int value) {
        this.value = value;

    }

    public Integer execute() {
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        boolean toStop = false;
        System.out.println("Start writing Bundles to space: version1");

        while (((currentTime - startTime) < (TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE) && !toStop)) {
            long iterationStartTime = System.currentTimeMillis();
            Bundle[] bundles = new Bundle[NUM_OF_BUNDLES_TO_WRITE];

            for (int i = 0; i < NUM_OF_BUNDLES_TO_WRITE; i++) {
                bundles[i] = Bundle.createBundle();
            }

            gigaSpace.writeMultiple(bundles);

            currentTime = System.currentTimeMillis();

            long differenceTime = currentTime - iterationStartTime;
            if ((differenceTime + currentTime) > (startTime + TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE)) {
                toStop = true;
            }
        }
        int count = gigaSpace.count(new Bundle());
        System.out.println("Finish writing with " + count + " Bundles");

        gigaSpace.clear(new Bundle());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int count2 = gigaSpace.count(new Bundle());
        System.out.println("After clear: " + count2 + "Bundles");

        System.gc();
        System.out.println("Cleanup completed...");

        return count;
    }

    public Integer getRouting() {
        return this.value;
    }
}

