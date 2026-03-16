package com.example.demo.config;

import com.example.demo.common.CodeEnum;
import com.example.demo.common.Result;
import com.example.demo.common.ReturnException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReturnException.class)
    public Result<?> handleReturnException(ReturnException e) {
        return Result.fail(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<?>> handleReturnException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.fail(CodeEnum.ServerError, "服务器异常:" + e.getMessage()));
    }
}
