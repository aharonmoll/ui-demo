package com.gigaspaces.common;

import com.gigaspaces.annotation.pojo.SpaceId;

public class Bundle {
    private Integer id;
    private String name;
    //private Integer quantity;

    public Bundle(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Bundle() {
    }

    public static Bundle createBundle(int id) {
        return new Bundle(id, RandomUtils.nextString());
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

}


