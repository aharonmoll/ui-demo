package com.gigaspaces.products_feeder;

import com.gigaspaces.common.Product;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.logging.Logger;
import static com.gigaspaces.common.Constants.UPDATE_PERIOD_TIME_UNIT;
import static com.gigaspaces.common.Constants.MILLISECONDS_IN_SECOND;
import static com.gigaspaces.common.Constants.SECONDS_IN_MINUTE;

public class ProductsFeeder implements InitializingBean, DisposableBean {
    Logger log = Logger.getLogger(this.getClass().getName());

    @GigaSpaceContext
    private GigaSpace gigaSpace;

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(this::updateProducts).start();
    }

    private void updateProducts() {
        Product [] products = gigaSpace.readMultiple(new Product(), 500); //ToDo- remember it's multiply by partitions

        while(true) {
            long startTime = System.currentTimeMillis();
            long currentTime = 0;
            boolean toStop = false;
            while (((currentTime - startTime) < (UPDATE_PERIOD_TIME_UNIT * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE) && !toStop)) {
                long iterationStartTime = System.currentTimeMillis();
                gigaSpace.writeMultiple(products);
                currentTime = System.currentTimeMillis();
                long differenceTime = currentTime - iterationStartTime;
                if ((differenceTime + currentTime) > (startTime + UPDATE_PERIOD_TIME_UNIT * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE)) {
                    toStop = true;
                }

            }
            log.info("5 minutes passed"); //Todo -this for me
        }
    }


    @Override
    public void destroy() throws Exception {

    }
}
