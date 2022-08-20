package com.mall.exception;

public class MallException {
    private final Integer code;
    private final String message;

    public MallException(MallExceptionEnum mallExceptionEnum) {
        this(mallExceptionEnum.getCode(),mallExceptionEnum.getMsg());
    }


    public MallException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
