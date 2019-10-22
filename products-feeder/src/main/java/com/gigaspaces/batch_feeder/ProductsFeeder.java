package com.gigaspaces.batch_feeder;

import com.gigaspaces.common.model.Product;
import com.gigaspaces.common.model.RandomUtils;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
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
public class ProductsFeeder /*implements InitializingBean, DisposableBean*/ {

   /* Logger logger = Logger.getLogger(this.getClass().getName());
    private ScheduledExecutorService queriesService;
*/
   /* @GigaSpaceContext
    private GigaSpace gigaSpace;

    // This is the place to write static data into the space
    public void afterPropertiesSet() {
        new Thread(this::run).start();
    }*/

    /*public void run() {
       // setupLogger();
        log("Start ProductCatalogue");
        try {
            initDoQueriesExecutor();
        } catch (InterruptedException e) {
            log("--------------------------------------------------------------------");
            log("ProductCatalogue failed: ", e);
            log("--------------------------------------------------------------------");
            queriesService.shutdown();
        }
    }

    private void initDoQueriesExecutor() throws InterruptedException {
        queriesService = Executors.newScheduledThreadPool(1);
        queriesService.scheduleAtFixedRate(this::doWrite, 0, 10, TimeUnit.SECONDS);
        queriesService.awaitTermination(30, TimeUnit.DAYS);
    }

    private void doWrite() {
        //long startTime = System.currentTimeMillis();

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            products.add(Product.createProduct(RandomUtils.nextInt()));
        }

        gigaSpace.writeMultiple(products.toArray());
    }

    private List<Product> getProductsFromSpace() {
        Product[] readProductsArray = gigaSpace.readMultiple(new Product());
        List<Product> crewMembers = new ArrayList<Product>(Arrays.asList(readProductsArray));

        return crewMembers;
    }

    private List<List<Product>> createProductsBuckets(List<Product> crewMembers) {
        int numOfProducts = crewMembers.size();
        int numOfBuckets = numOfProducts / NUM_OF_CREW_MEMBERS_IN_FLIGHT;
        List<List<Product>> buckets = new ArrayList<>(numOfBuckets);
        int bucketStartIdx = 0;

        for (int bucketNum = 0; bucketNum < numOfBuckets; bucketNum++) {
            int bucketEndIdx = bucketStartIdx + NUM_OF_CREW_MEMBERS_IN_FLIGHT;
            List<Product> list = new ArrayList<>(NUM_OF_CREW_MEMBERS_IN_FLIGHT);
            crewMembers.subList(bucketStartIdx, bucketEndIdx).forEach(crewMember -> list.add(crewMember));
            buckets.add(bucketNum, list);
            bucketStartIdx = bucketEndIdx;
        }

        return buckets;
    }


    private void log(String msg, Exception e) {
        logger.log(Level.SEVERE, msg);
        logger.log(Level.SEVERE, "Got Exception: ", e);
    }

    private void log(String msg) {
        logger.log(Level.INFO, msg);
    }


    public void destroy() throws Exception {
    }*/
}
