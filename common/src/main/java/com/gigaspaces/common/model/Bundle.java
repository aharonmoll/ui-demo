package com.gigaspaces.common.model;

public class Bundle {
    private Product product;
    private Service service;

    public Bundle(Product product, Service service) {
        this.product = product;
        this.service = service;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
