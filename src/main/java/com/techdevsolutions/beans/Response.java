package com.techdevsolutions.beans;

import java.io.Serializable;

public class Response implements Serializable {
    private Object data = null;
    private Long took = 0L;
    private String error = "";

    public Response() {
    }

    public Response(Object data, Long took) {
        this.data = data;
        this.took = took;
    }

    public Response(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getTook() {
        return took;
    }

    public void setTook(Long took) {
        this.took = took;
    }
}
