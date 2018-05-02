package com.techdevsolutions.beans;

import java.io.Serializable;
import java.util.Objects;

public class ValidationResponse implements Serializable {
    private String field = "";
    private String message = "";
    private Boolean valid = false;

    public ValidationResponse() {}

    public ValidationResponse(Boolean valid, String field, String message) {
        this.setValid(valid);
        this.setField(field);
        this.setMessage(message);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationResponse that = (ValidationResponse) o;
        return Objects.equals(field, that.field) &&
                Objects.equals(message, that.message) &&
                Objects.equals(valid, that.valid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(field, message, valid);
    }

    @Override
    public String toString() {
        return "ValidationResponse{" +
                "field='" + field + '\'' +
                ", message='" + message + '\'' +
                ", valid=" + valid +
                '}';
    }
}
