package com.lala.exception;


import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author snxx
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> exceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        ErrorMeta errorMeta = new ErrorMeta(e);
        HttpHeaders headers = new HttpHeaders();
        headers.set("HTTP", String.valueOf(errorMeta.getCode()));
        return ResponseEntity.ok().headers(headers).body(errorMeta);

    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(HttpServletRequest req, RuntimeException e) throws Exception {
        ErrorMeta errorMeta = new ErrorMeta(e);
        HttpHeaders headers = new HttpHeaders();
        headers.set("HTTP", String.valueOf(errorMeta.getCode()));

        return ResponseEntity.ok().headers(headers).body(errorMeta);
    }
}