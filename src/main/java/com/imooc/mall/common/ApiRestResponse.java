package com.imooc.mall.common;

import com.imooc.mall.exception.ImoocMallExceptionEnum;

/**
 * 通用返回对象
 */
public class ApiRestResponse<T> {

    /**
     * 请求返回，有三个通用的。分别是状态码，msg，数据
     */
    private Integer status;
    private String msg;
    private T data;
    private static final int OK_CODE = 10000;
    private static final String OK_MSG = "SUCCESS";

    public ApiRestResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 两参构造方法
     *
     * @param status
     * @param msg
     */
    public ApiRestResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    /**
     * 默认构造方法。调用两参构造方法
     */
    public ApiRestResponse() {
        this(OK_CODE, OK_MSG);
    }

    /**
     * 返回当前的通用对象的信息
     *
     * @param <T>
     * @return
     */
    public static <T> ApiRestResponse success() {
        return new ApiRestResponse<>();
    }

    /**
     * 返回通用对象的方法，这个方法可以再传一个数据
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> ApiRestResponse<T> success(T result) {
        ApiRestResponse<T> response = new ApiRestResponse<>();
        response.setData(result);
        return response;
    }

    public static <T> ApiRestResponse<T> error(Integer code, String msg) {
        // 调用两参构造方法返回
        return new ApiRestResponse<>(code, msg);
    }

    public static <T> ApiRestResponse<T> error(ImoocMallExceptionEnum ex) {
        // 调用两参构造方法返回
        // 用的是异常枚举的参数
        return new ApiRestResponse<>(ex.getCode(), ex.getMsg());
    }

    @Override
    public String toString() {
        return "ApiRestResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
