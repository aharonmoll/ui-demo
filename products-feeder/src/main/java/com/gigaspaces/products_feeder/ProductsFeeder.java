package com.gigaspaces.products_feeder;

import com.gigaspaces.common.Product;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.logging.Logger;

import static com.gigaspaces.common.Constants.*;

public class ProductsFeeder implements InitializingBean, DisposableBean {
    Logger log = Logger.getLogger(this.getClass().getName());

    @GigaSpaceContext
    private GigaSpace gigaSpace;

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(this::updateProducts).start();
    }

    private void updateProducts() {
        Product [] products = gigaSpace.readMultiple(new Product(), NUM_OF_ENTITIES);

        while(true) {
            long startTime = System.currentTimeMillis();
            long currentTime = 0;
            boolean toStop = false;
            while (((currentTime - startTime) < (TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE) && !toStop)) {
                long iterationStartTime = System.currentTimeMillis();
                gigaSpace.writeMultiple(products);
                //gigaSpace.readMultiple(new SQLQuery<>(Product.class, null), 1000);
                currentTime = System.currentTimeMillis();
                long differenceTime = currentTime - iterationStartTime;
                if ((differenceTime + currentTime) > (startTime + TIME_PERIOD_OF_5_MINUTES * MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE)) {
                    toStop = true;
                }
            }
        }
    }


    @Override
    public void destroy() throws Exception {
    }
}
