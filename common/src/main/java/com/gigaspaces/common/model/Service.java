package com.gigaspaces.common.model;

import com.gigaspaces.annotation.pojo.SpaceId;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

import static com.gigaspaces.common.Constants.NUM_OF_CREW_MEMBERS_IN_FLIGHT;

public class Service {
    private Integer id;
    private List<Product> crewMembers;

    public Service() {}

    private Service(Integer flightNumber, List<Product> crewMembers) {
        this.id = flightNumber;
        this.crewMembers = crewMembers;
    }

    public Service(Integer flightNum) {
        id = flightNum;
    }

    public static List<Product> createProducts(int numOfProducts) {
        List<Product> crewMembers = new ArrayList<Product>(numOfProducts);

        for (int i = 0; i < numOfProducts; i++) {
            crewMembers.add(Product.createProduct(i));
        }

        return crewMembers;
    }

    @Id
    @SpaceId
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return crewMembers;
    }

    public void setProducts(List<Product> crewMembers) {
        this.crewMembers = crewMembers;
    }
}
