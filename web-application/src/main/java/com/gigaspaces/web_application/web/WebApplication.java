package com.gigaspaces.web_application.web;


import com.gigaspaces.common.Bundle;
import com.gigaspaces.common.Product;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.core.space.SpaceProxyConfigurer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.logging.Logger;

import static com.gigaspaces.common.Constants.*;

public class WebApplication implements InitializingBean, DisposableBean {
    Logger log = Logger.getLogger(this.getClass().getName());

    @GigaSpaceContext
    private GigaSpace gigaSpace;

    public WebApplication() {
    }

    public WebApplication(String spaceName, String groupName) {
        this.gigaSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer(spaceName).lookupGroups(groupName)).gigaSpace();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(this::doQueries).start();
    }

    private void doQueries() {
        while(true) {
            log.info("++++++++++++++++++++++Recurrent read: start 5 minutes");
            long startTime = System.currentTimeMillis();
            long currentTime = 0;
            boolean toStop = false;
            while (((currentTime - startTime) < (TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE) && !toStop)) {
                long iterationStartTime = System.currentTimeMillis();
                gigaSpace.readMultiple(new SQLQuery<>(Product.class, null), NUM_OF_ENTITIES_TO_READ);
                currentTime = System.currentTimeMillis();
                try {  //Todo-delete
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long differenceTime = currentTime - iterationStartTime;
                if ((differenceTime + currentTime) > (startTime + TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE)) {
                    toStop = true;
                }

            }
            log.info("+++++++++++++++++++++++++Recurrent read: 5 minutes passed");
        }
    }

    /*public void doRAMAlert() {
        new Thread(this::doRAMAlert2).start();
        SQLQuery<Product> query = new SQLQuery<>(Product.class, "");

        int highestId = 0;
        if (gigaSpace.count(new Product()) > 0) {
            highestId = max(gigaSpace, query, "id");
        }
        int id = highestId + 1;
        log.info("doRAMAlert: starts writing for 5 minutes");

        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        boolean toStop = false;
        while (((currentTime - startTime) < (TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE) && !toStop)) {
            long iterationStartTime = System.currentTimeMillis();

            for (int i = 0; i < NUM_OF_BUNDLES_TO_WRITE; i++) {
                gigaSpace.write(Product.createProduct(id));
                id++;
            }

            currentTime = System.currentTimeMillis();

            long differenceTime = currentTime - iterationStartTime;
            if ((differenceTime + currentTime) > (startTime + TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE)) {
                toStop = true;
            }
        }
        log.info("doRAMAlert: finished writing for 5 minutes, highest id is: " + id);
    }

    private void doRAMAlert2() {
        SQLQuery<Bundle> query = new SQLQuery<>(Bundle.class, "");

        int highestId = 0;
        if (gigaSpace.count(new Bundle()) > 0) {
            highestId = max(gigaSpace, query, "id");
        }
        int id = highestId + 1;
        log.info("doRAMAlert: starts writing for 5 minutes");

        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        boolean toStop = false;
        while (((currentTime - startTime) < (TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE) && !toStop)) {
            long iterationStartTime = System.currentTimeMillis();

            for (int i = 0; i < NUM_OF_BUNDLES_TO_WRITE; i++) {
                gigaSpace.write(Bundle.createBundle(id));
                id++;
            }

            currentTime = System.currentTimeMillis();

            long differenceTime = currentTime - iterationStartTime;
            if ((differenceTime + currentTime) > (startTime + TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE)) {
                toStop = true;
            }
        }
        log.info("doRAMAlert: finished writing for 5 minutes, highest id is: " + id); //Todo -this for me
    }
*/

    public void doRAMAlert(){
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
        log.info("+++++++++++++++++++++++++Bundle: finished writing for 5 minutes, highest id is: " ); //Todo -this for me
    }


    public void doCPUAlert(){
        new Thread(this::doCPUAlert2).start();
        log.info("++++++++++++++++++++++pick of read: start 5 minutes"); //Todo -this for me
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        boolean toStop = false;
        while (((currentTime - startTime) < (TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE) && !toStop)) {
            long iterationStartTime = System.currentTimeMillis();
            gigaSpace.readMultiple(new SQLQuery<>(Product.class, null), NUM_OF_ENTITIES_TO_READ); //Todo- many types of read multiple
            currentTime = System.currentTimeMillis();
            long differenceTime = currentTime - iterationStartTime;
            if ((differenceTime + currentTime) > (startTime + TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE)) {
                toStop = true;
            }
        }
        log.info("+++++++++++++++++++++++++pick of read: 5 minutes passed"); //Todo -this for me
    }

    private void doCPUAlert2(){
        log.info("++++++++++++++++++++++pick of read: start 5 minutes"); //Todo -this for me
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        boolean toStop = false;
        while (((currentTime - startTime) < (TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE) && !toStop)) {
            long iterationStartTime = System.currentTimeMillis();
            gigaSpace.readMultiple(new SQLQuery<>(Product.class, null), NUM_OF_ENTITIES_TO_READ); //Todo- many types of read multiple
            currentTime = System.currentTimeMillis();
            long differenceTime = currentTime - iterationStartTime;
            if ((differenceTime + currentTime) > (startTime + TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE)) {
                toStop = true;
            }
        }
        log.info("+++++++++++++++++++++++++pick of read: 5 minutes passed"); //Todo -this for me
    }

    @Override
    public void destroy() throws Exception { }
}