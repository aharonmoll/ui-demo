package com.gigaspaces.web_application.web;

public class PCCalc {

    public static void main(String[] args) {

        long longMax = 1000;
        long primeCount = 0;
        long primeMax = 0;

        long jobCount = 0;
        long s = System.currentTimeMillis();
        while (jobCount < longMax*100) {
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
    }
}
