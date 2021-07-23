package com.lola.digiccy.client;

import java.io.Serializable;

/**
 * @Author: shrimp
 * @Date: 2020/12/21 18:10
 */
public class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = -1186076784068926641L;
    private int code;
    private String message;
    private T data;
    private String description;

    public ApiResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ApiResult<T> success() {
        return new ApiResult<>(200, "SUCCESS");
    }

    public static <T> ApiResult<T> success(T data) {
        ApiResult<T> result = new ApiResult<>(200, "SUCCESS");
        result.setData(data);
        return result;
    }

    public static <T> ApiResult<T> error(String message) {
        return new ApiResult<>(500, message);
    }

    public static <T> ApiResult<T> error() {
        return new ApiResult<>(500, "ERROR");
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
