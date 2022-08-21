package com.mall.exception;

import com.mall.common.ApiRestRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 处理统一异常的handler
 */
/*这个注解是拦截异常的*/
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 重名用户异常在service层抛出的是异常，不是APIResponse
     * 系统级别的异常要打印出日志
     */
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 这个注解是  告诉 处理系统异常
     *
     * @param e 当前产生的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e) {
        log.error("Default Exception", e);
        // 处理系统的异常
        return ApiRestRes.error(MallExceptionEnum.SYSTEM_ERROR);
    }

    /**
     * 拦截自定义异常并处理
     *
     * @param e MallException.class
     */
    @ExceptionHandler(MallException.class)
    @ResponseBody
    public Object handleImoocMallException(MallException e) {
        log.error("MallException" + e);
        return ApiRestRes.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestRes handleMethodArgNotValidException(MethodArgumentNotValidException e) {
        return handleBindingResult(e.getBindingResult());
    }

    private ApiRestRes handleBindingResult(BindingResult bindingResult) {
        List<String> list = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError : allErrors) {
                String message = objectError.getDefaultMessage();
                list.add(message);
            }
        }
        if (list.size() == 0) {
            return ApiRestRes.error(MallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestRes.error(MallExceptionEnum.REQUEST_PARAM_ERROR.getCode(), list.toString());
    }

}
