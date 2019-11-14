package com.gigaspaces.ui.demo.controllers;

import com.gigaspaces.annotation.SupportCodeChange;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.executor.Task;
import org.openspaces.core.executor.TaskGigaSpace;
import org.openspaces.core.executor.TaskRoutingProvider;

import static com.gigaspaces.common.Constants.*;

@SupportCodeChange(id="2")
public class CPUAlertTask implements Task<Integer>, TaskRoutingProvider {
    private int value;
    private int duration;

    @TaskGigaSpace
    private transient GigaSpace gigaSpace;


    public CPUAlertTask(int value, int duration) {
        this.value = value;
        this.duration = duration;
    }

    public Integer execute() {
        System.out.println("CPU: execute alert");
        long startTime = System.currentTimeMillis();
        long longMax = 1000;
        long primeCount = 0;
        long primeMax = 0;


        while ((System.currentTimeMillis() - startTime) / MILLISECONDS_IN_SECOND < duration ) {
            long count = 0;
            long max = 0;
            for (long i=3; i<=longMax; i++) {
                boolean isPrime = true;
                for (long j=2; j<=i/2 && isPrime; j++) {
                    isPrime = i % j > 0;
                }
                if (isPrime) {
                    count++;
                    max = i;
                }
            }
        }
        int timeOut = (int)(System.currentTimeMillis() - startTime) / MILLISECONDS_IN_SECOND;

        return timeOut;
    }

    public Integer getRouting() {
        return this.value;
    }
}
