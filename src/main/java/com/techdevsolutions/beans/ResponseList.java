package com.techdevsolutions.beans;

import java.io.Serializable;
import java.util.List;

public class ResponseList extends Response implements Serializable {
    private Integer size = 0;

    public ResponseList(List data, Long took) {
        this.setData(data);
        this.setSize(data.size());
        this.setTook(took);
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
