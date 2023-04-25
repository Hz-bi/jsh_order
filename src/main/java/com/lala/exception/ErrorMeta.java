package com.lala.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.lala.exception.BusinessException;
import com.lala.exception.BusinessRuntimeException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import javax.security.auth.message.AuthException;
import java.sql.SQLException;


public class ErrorMeta {

    private Integer code;
    private String message;
    private String stack;

    public ErrorMeta(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public ErrorMeta(Integer code, String message, Throwable throwable) {
        this.code = code;
        this.message = message + "，异常原因：" + ExceptionUtil.getSimpleMessage(throwable);
        this.stack = ExceptionUtil.stacktraceToString(throwable);
    }
    public ErrorMeta(Throwable throwable) {
        this.stack = ExceptionUtil.stacktraceToString(throwable);
        if (throwable instanceof SQLException) {
            this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
            this.message = "数据库访问错误" + "，异常原因：" + ExceptionUtil.getSimpleMessage(throwable);
        } else if (throwable instanceof BusinessException) {
            this.code = ((BusinessException) throwable).getErrorCode().value();
            this.message = throwable.getMessage();
        } else if (throwable instanceof BusinessRuntimeException) {
            this.code = ((BusinessRuntimeException) throwable).getErrorCode().value();
            this.message = throwable.getMessage();
        } else if (throwable instanceof AuthException) {
            this.code = ((BusinessException) throwable).getErrorCode().value();
            this.message = throwable.getMessage();
        } else if (throwable instanceof DataIntegrityViolationException) {
            this.code = ((BusinessException) throwable).getErrorCode().value();
            this.message = throwable.getMessage();
        } else {
            this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
            this.message = "系统运行错误" + "，异常原因：" + ExceptionUtil.getSimpleMessage(throwable);
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }
}
