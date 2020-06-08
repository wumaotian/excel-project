package com.excel.excelproject.exception;

import com.excel.excelproject.res.WsResult;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseHandler {

    @ExceptionHandler({Exception.class})
    @Order(100)
    public WsResult getAopCheckErrorResult(Exception e) {
        e.printStackTrace();
        return new WsResult(WsResult.FAILD, e.getMessage(), null);
    }

}
