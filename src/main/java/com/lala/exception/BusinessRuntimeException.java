package com.lala.exception;

import org.springframework.http.HttpStatus;

public class BusinessRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * * 错误编码
     */
    private HttpStatus errorCode;

    /**
     * * 消息是否为属性文件中的Key
     */
    private boolean propertiesKey = true;

    public BusinessRuntimeException(String message) {
        super(message);
    }

    public BusinessRuntimeException(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(HttpStatus errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isPropertiesKey() {
        return propertiesKey;
    }

    public void setPropertiesKey(boolean propertiesKey) {
        this.propertiesKey = propertiesKey;
    }
}
