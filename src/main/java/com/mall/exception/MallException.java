package com.mall.exception;

public class MallException extends RuntimeException {
    private final String message;
    private final Integer code;

    public MallException(String message, Integer code) {
        this.message = message; // 错误信息
        this.code = code; // 错误码
    }

    /**
     * 调用默认构造方法
     *
     * @param exceptionEnum 异常枚举
     */
    public MallException(MallExceptionEnum exceptionEnum) {
        this(exceptionEnum.getMsg(), exceptionEnum.getCode());
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
