package com.gigaspaces.common;

import com.gigaspaces.annotation.pojo.SpaceId;

import java.util.Arrays;
import java.util.Objects;


public class Bundle {

    private String id;
    private String name;
    private byte[] bytes = new byte[1000];


    public Bundle() {
    }

    public static Bundle createBundle() {
        return new Bundle();
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bundle)) return false;
        Bundle bundle = (Bundle) o;
        return /*Objects.equals(getId(), bundle.getId()) &&*/
                Objects.equals(getName(), bundle.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(/*getId(),*/ getName());
    }
}


