package com.techdevsolutions.beans;

import java.io.Serializable;
import java.util.Objects;

public class ErrorResponse implements Serializable {
    private String path = "";
    private String status = "";
    private String error = "";
    private String message = "";

    public ErrorResponse() {
    }

    public ErrorResponse(String path, String status, String error, String message) {
        this.path = path;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponse that = (ErrorResponse) o;
        return Objects.equals(path, that.path) &&
                Objects.equals(status, that.status) &&
                Objects.equals(error, that.error) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {

        return Objects.hash(path, status, error, message);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "path='" + path + '\'' +
                ", status='" + status + '\'' +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
