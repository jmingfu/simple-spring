package com.example.demo.common;

import lombok.Data;

@Data
public class Result<T> {
    private CodeEnum code;
    private T data;

    public CodeEnum getCode() {
        return code;
    }

    public void setCode(CodeEnum code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(CodeEnum.Success);
        r.setData(data);
        return r;
    }

    public static <T> Result<T> fail(T data) {
        Result<T> r = new Result<>();
        r.setCode(CodeEnum.ServerError);
        r.setData(data);
        return r;
    }
}
