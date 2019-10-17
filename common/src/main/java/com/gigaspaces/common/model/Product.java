package com.gigaspaces.common.model;

import com.gigaspaces.annotation.pojo.SpaceId;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

public class Product implements Serializable {
    private Integer id;
    private String name;
    private String color;
    private Set<String> placesToSell;
    private CommunicationInfo communicationInfo;

    public Product() {
    }

    public Product(Integer id, String name, String color, Set<String> placesToSell, CommunicationInfo communicationInfo) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.placesToSell = placesToSell;
        this.communicationInfo = communicationInfo;
    }

    public static Product createProduct(int id) {
        return new Product(id, RandomUtils.nextString(), RandomUtils.nextString(), Collections.<String>emptySet()
                , CommunicationInfo.createCommunicationInfo());
    }

    @SpaceId
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<String> getPlacesToSell() {
        return placesToSell;
    }

    public void setPlacesToSell(Set<String> placesToSell) {
        this.placesToSell = placesToSell;
    }

    public CommunicationInfo getCommunicationInfo() {
        return communicationInfo;
    }

    public void setCommunicationInfo(CommunicationInfo communicationInfo) {
        this.communicationInfo = communicationInfo;
    }
}
