package com.gigaspaces.common.model;

import com.gigaspaces.annotation.pojo.SpaceId;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

import static com.gigaspaces.common.Constants.NUM_OF_CREW_MEMBERS_IN_FLIGHT;

public class Service {
    private Integer id;
    private String name;

    public Service() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
