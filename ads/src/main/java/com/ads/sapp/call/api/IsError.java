package com.ads.sapp.call.api;

public class IsError {
    Boolean isError = false;
    String errorCode;
    String errorName;
    String errorNameDes;

    public IsError() {
    }

    public IsError(Boolean isError, String errorCode, String errorName, String errorNameDes) {
        this.isError = isError;
        this.errorCode = errorCode;
        this.errorName = errorName;
        this.errorNameDes = errorNameDes;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorNameDes() {
        return errorNameDes;
    }

    public void setErrorNameDes(String errorNameDes) {
        this.errorNameDes = errorNameDes;
    }

    public Boolean getError() {
        return isError;
    }

    public void setError(Boolean error) {
        isError = error;
    }
}
