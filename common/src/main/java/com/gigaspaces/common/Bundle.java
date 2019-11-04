package com.gigaspaces.common;

import com.gigaspaces.annotation.pojo.SpaceId;


public class Bundle {

    private String id;
    private String name;
    private byte[] bytes = new byte[1000];
    //private Integer quantity;

    public Bundle() {
    }

    public Bundle(String name) {
        this.name = name;
    }

    public static Bundle createBundle() {
        return new Bundle(RandomUtils.nextString());
    }

    @SpaceId(autoGenerate = true)  //Todo- when it gets the id? not in setID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}


