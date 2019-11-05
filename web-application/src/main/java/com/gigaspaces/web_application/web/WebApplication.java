package com.gigaspaces.web_application.web;


import com.gigaspaces.async.AsyncFuture;
import com.gigaspaces.common.Bundle;
import com.gigaspaces.common.Product;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.core.space.SpaceProxyConfigurer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import static com.gigaspaces.common.Constants.*;

public class WebApplication implements InitializingBean, DisposableBean {
    Logger log = Logger.getLogger(this.getClass().getName());

    @GigaSpaceContext
    private GigaSpace gigaSpace;

    public WebApplication() {
    }

    public WebApplication(String spaceName, String groupName) {
        //this.gigaSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer(spaceName).lookupLocators("192.168.35.164")).gigaSpace();
        this.gigaSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer(spaceName).lookupGroups(groupName)).gigaSpace();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(this::doQueries).start();
    }

    private void doQueries() {
        while (true) {
            long startTime = System.currentTimeMillis();
            long currentTime = 0;
            boolean toStop = false;

            while (((currentTime - startTime) < (TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE) && !toStop)) {
                long iterationStartTime = System.currentTimeMillis();
                gigaSpace.readMultiple(new SQLQuery<>(Product.class, null), NUM_OF_ENTITIES_TO_READ);
                currentTime = System.currentTimeMillis();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long differenceTime = currentTime - iterationStartTime;
                if ((differenceTime + currentTime) > (startTime + TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE)) {
                    toStop = true;
                }
            }
        }
    }

    public void doCPUAlert() {
        AsyncFuture<Integer> future = gigaSpace.execute(new CPUAlertTask(1));
        try {
            int result = future.get(1, TimeUnit.HOURS); //Todo- change time
            System.out.println("result is" + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    public void doRAMAlert() {
        AsyncFuture<Integer> future = gigaSpace.execute(new RAMAlertTask(1));
        try {
            int result = future.get(1, TimeUnit.HOURS); //Todo- change time
            System.out.println("result is" + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() throws Exception {
    }

    /*public void doCPUAlert(){
        new Thread(this::doCPUAlert2).start();
        log.info("doCPUAlert: Starts 5 minutes");
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        boolean toStop = false;

        while (((currentTime - startTime) < (TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE) && !toStop)) {
            long iterationStartTime = System.currentTimeMillis();
            gigaSpace.readMultiple(new SQLQuery<>(Product.class, null), NUM_OF_ENTITIES_TO_READ);
            currentTime = System.currentTimeMillis();
            long differenceTime = currentTime - iterationStartTime;
            if ((differenceTime + currentTime) > (startTime + TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE)) {
                toStop = true;
            }
        }
       //log.info("doCPUAlert: finished 5 minutes");
    }*/

    /*private void doCPUAlert2() {
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        boolean toStop = false;
        while (((currentTime - startTime) < (TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE) && !toStop)) {
            long iterationStartTime = System.currentTimeMillis();
            gigaSpace.readMultiple(new SQLQuery<>(Product.class, null), NUM_OF_ENTITIES_TO_READ);
            currentTime = System.currentTimeMillis();
            long differenceTime = currentTime - iterationStartTime;
            if ((differenceTime + currentTime) > (startTime + TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE)) {
                toStop = true;
            }
        }
    }
*/
    /*public void doRAMAlert() {
        //new Thread(this::doRAMAlert2).start();
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        boolean toStop = false;

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
        log.info("Bundle: finished writing after 5 minutes");
    }*/

    /*private void doRAMAlert2() {
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        boolean toStop = false;

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
    }*/

}