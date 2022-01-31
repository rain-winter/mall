package com.imooc.mall.exception;

/**
 * 统一异常。这个方法没有setter方法
 */
public class ImoocMallException extends RuntimeException {
    private final Integer code;
    private final String message;

    public ImoocMallException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 调用默认构造方法，参数是异常枚举
     *
     * @param exceptionEnum
     */
    public ImoocMallException(ImoocMallExceptionEnum exceptionEnum) {
        this(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
