package com.gigaspaces.web;

import com.gigaspaces.common.Product;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.logging.Logger;
import static com.gigaspaces.common.Constants.NUM_OF_ENTITIES;
import static com.gigaspaces.common.Constants.PRODUCT_FEEDER_PERIOD_TIME_UNIT;
import static com.gigaspaces.common.Constants.MILLISECONDS_IN_SECOND;
import static com.gigaspaces.common.Constants.SECONDS_IN_MINUTE;

public class WebApplication1 implements InitializingBean, DisposableBean {
    Logger log = Logger.getLogger(this.getClass().getName());

    @GigaSpaceContext
    private GigaSpace gigaSpace;

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(this::doQueries).start();
    }

    private void doQueries() {
        //Product [] products = gigaSpace.readMultiple(new Product(), 500); //ToDo- remember it's multiply by partitions

        while(true) {
            log.info("read: start 5 minutes"); //Todo -this for me
            long startTime = System.currentTimeMillis();
            long currentTime = 0;
            boolean toStop = false;
            while (((currentTime - startTime) < (PRODUCT_FEEDER_PERIOD_TIME_UNIT * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE) && !toStop)) {
                long iterationStartTime = System.currentTimeMillis();
                gigaSpace.readMultiple(new SQLQuery<>(Product.class, null), 1000); //Todo- many types of read multiple
                currentTime = System.currentTimeMillis();
                long differenceTime = currentTime - iterationStartTime;
                if ((differenceTime + currentTime) > (startTime + PRODUCT_FEEDER_PERIOD_TIME_UNIT * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE)) {
                    toStop = true;
                }

            }
            log.info("read: 5 minutes passed"); //Todo -this for me
        }
    }


    @Override
    public void destroy() throws Exception {

    }
}
