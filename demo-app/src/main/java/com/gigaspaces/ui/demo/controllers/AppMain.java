package com.gigaspaces.ui.demo.controllers;

import com.gigaspaces.common.Bundle;
import com.gigaspaces.rest.client.java.invoker.ApiException;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.core.space.SpaceProxyConfigurer;

import static com.gigaspaces.common.Constants.MILLISECONDS_IN_SECOND;
import static com.gigaspaces.common.Constants.NUM_OF_BUNDLES_TO_WRITE;

public class AppMain {
    @GigaSpaceContext
    private GigaSpace gigaSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer("space").lookupGroups("efratGroup")).gigaSpace();

    public Integer execute() {
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        boolean toStop = false;
        System.out.println("Start writing Bundles to space: version1");

//        while (((currentTime - startTime) / MILLISECONDS_IN_SECOND < 10 * MILLISECONDS_IN_SECOND) && !toStop) {
//            long iterationStartTime = System.currentTimeMillis();
//            Bundle[] bundles = new Bundle[NUM_OF_BUNDLES_TO_WRITE];
//            for (int i = 0; i < NUM_OF_BUNDLES_TO_WRITE; i++) {
//                bundles[i] = Bundle.createBundle();
//            }
//            gigaSpace.writeMultiple(bundles);
//            currentTime = System.currentTimeMillis();
//            long differenceTime = currentTime - iterationStartTime;
//
//            if ((differenceTime + currentTime) > (startTime + 10 * MILLISECONDS_IN_SECOND)) {
//                toStop = true;
//            }
//        }

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
    public static void main (String args[]) {
        //DemoController controller = new DemoController();
        /*try {
            System.out.println(controller.markInstanceUnavailable("ProductsCatalog","ProductsCatalog~2_2", 30 ));
        } catch (ApiException e) {
            e.printStackTrace();
        }*/
        /*try {
            System.out.println(controller.markInstanceUnavailable("Mirror","Mirror~1", 30 ));
        } catch (ApiException e) {
            e.printStackTrace();
        }*/

        /*try {
            System.out.println(controller.addCPUAlertTask("space", 60));
        } catch (ApiException e) {
            e.printStackTrace();
        }*/


        AppMain main = new AppMain();
        main.execute();

    }
}
