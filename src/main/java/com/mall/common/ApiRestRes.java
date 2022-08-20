package com.mall.common;

import com.mall.exception.MallExceptionEnum;

public class ApiRestRes<T> {
    private Integer status;
    private String msg;
    private T data;
    private static final int OK_CODE = 10000;
    private static final String OK_MSG = "SUCCESS";

    public ApiRestRes(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 两参构造方法
     *
     * @param status 状态码
     * @param msg    消息
     */
    public ApiRestRes(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    /**
     * 默认构造方法。调用两参构造方法
     */
    public ApiRestRes() {
        this(OK_CODE, OK_MSG);
    }

    /**
     * 返回当前的通用对象的信息
     *
     * @param <T>
     * @return
     */
    public static <T> ApiRestRes success() {
        return new ApiRestRes<>();
    }

    /**
     * 返回通用对象的方法，这个方法可以再传一个数据
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> ApiRestRes<T> success(T result) {
        ApiRestRes<T> response = new ApiRestRes<>();
        response.setData(result);
        return response;
    }

    public static <T> ApiRestRes<T> error(Integer code, String msg) {
        // 调用两参构造方法返回
        return new ApiRestRes<>(code, msg);
    }

    public static <T> ApiRestRes<T> error(MallExceptionEnum ex) {
        // 调用两参构造方法返回
        // 用的是异常枚举的参数
        return new ApiRestRes(ex.getCode(), ex.getMsg());
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
