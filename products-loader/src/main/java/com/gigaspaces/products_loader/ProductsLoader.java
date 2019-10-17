package com.gigaspaces.products_loader;

import com.gigaspaces.common.model.Product;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.logging.Logger;

import static com.gigaspaces.common.Constants.*;

/**
 * A feeder bean starts a scheduled task that writes a new Data objects to the space (in an
 * unprocessed state).
 *
 * <p>The space is injected into this bean using OpenSpaces support for @GigaSpaceContext
 * annotation.
 *
 * <p>The scheduling uses the java.util.concurrent Scheduled Executor Service. It is started and
 * stopped based on Spring lifecycle events.
 */
public class ProductsLoader implements InitializingBean, DisposableBean {

    Logger log = Logger.getLogger(this.getClass().getName());

    @GigaSpaceContext
    private GigaSpace gigaSpace;

    // This is the place to write static data into the space
    public void afterPropertiesSet() throws Exception {
        new Thread(this::populateSpaceWithProducts).start();
    }

    private void populateSpaceWithProducts() {
        log.info("Start populating space with " +   NUM_OF_PRODUCT_OFFERS + " product's offers");
        int numOfProductsInSpace = gigaSpace.count(new Product());

        for (int id = numOfProductsInSpace; id < numOfProductsInSpace + NUM_OF_PRODUCT_OFFERS; id++) {
            gigaSpace.write(Product.createProduct(id));
        }

        log.info("Finish populating space with product's offers");
    }

    public void destroy() throws Exception {
    }
}
