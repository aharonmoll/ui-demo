package com.gigaspaces.products_loader;

import com.gigaspaces.common.Product;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.logging.Logger;

import static com.gigaspaces.common.Constants.NUM_OF_PRODUCTS_TO_LOAD; //why import static?????
import static org.openspaces.extensions.QueryExtension.max;


public class ProductsLoader implements InitializingBean, DisposableBean {

    Logger log = Logger.getLogger(this.getClass().getName());

    @GigaSpaceContext
    private GigaSpace gigaSpace;

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(this::populateSpaceWithProducts).start();
    }

    private void populateSpaceWithProducts() {
        log.info("Start populating space with " + NUM_OF_PRODUCTS_TO_LOAD + " products");

        SQLQuery<Product> query = new SQLQuery<>(Product.class, "");

        int highestId = 0;
        if(gigaSpace.count(new Product()) > 0){
            highestId = max(gigaSpace, query, "id"); // Todo- this returns null pointer exception if no products in space
        }
        int id = highestId + 1;
        System.out.println("+++++++++++++++++++++++++++++++++highest id is " + highestId);
        for (int i = 0; i < NUM_OF_PRODUCTS_TO_LOAD ; i++) {
            gigaSpace.write(Product.createProduct(id));
            id++;
        }

        log.info("Finish populating space with products");
        System.out.println("++++++++++++++++++++++++++++++after populate highest id is " + id);
    }


    @Override
    public void destroy() throws Exception { }
}
