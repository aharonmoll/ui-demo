package com.gigaspaces.web_application.web;

import com.gigaspaces.annotation.SupportCodeChange;
import com.gigaspaces.common.Product;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.executor.Task;
import org.openspaces.core.executor.TaskGigaSpace;
import org.openspaces.core.executor.TaskRoutingProvider;

import static com.gigaspaces.common.Constants.*;

@SupportCodeChange(id="1")
public class CPUAlertTask implements Task<Integer>, TaskRoutingProvider {
    private int value;

    @TaskGigaSpace
    private transient GigaSpace gigaSpace;


    public CPUAlertTask(int value) {
        this.value = value;
    }

    public Integer execute() {
       System.out.println("CPU: execute version1");
        long longMax = 1000;
        long primeCount = 0;
        long primeMax = 0;
        int minutes=2;

        long jobCount = 0;
        long s = System.currentTimeMillis();
        while (jobCount < longMax*100*minutes) {
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
            primeCount = count;
            primeMax = max;
            jobCount++;
        }
        long end = System.currentTimeMillis();
        System.out.println("took " + (end-s));

        return 0;
    }

    private void doCPUAlert2() {
        System.out.println("in second thread");
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        boolean toStop = false;
        while (((currentTime - startTime) < (2 * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE) && !toStop)) {
            long iterationStartTime = System.currentTimeMillis();
            gigaSpace.readMultiple(new SQLQuery<>(Product.class, null), NUM_OF_ENTITIES_TO_READ);
            currentTime = System.currentTimeMillis();
            long differenceTime = currentTime - iterationStartTime;
            if ((differenceTime + currentTime) > (startTime + 2 * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE)) {
                toStop = true;
            }
        }
    }


    public Integer getRouting() {
        return this.value;
    }
}
