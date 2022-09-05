package com.mall.exception;

import com.mall.common.ApiRestRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

//    @ExceptionHandler(NotLoginException.class)
//    @ResponseBody
//    public Object handleNotLoginException(NotLoginException nle) {
//        log.error("NotLoginException", nle);
//        // 处理系统的异常
//        String message = "";
//        if (nle.getType().equals(NotLoginException.NOT_TOKEN)) {
//            message = "未提供Token";
//        } else if (nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
//            message = "未提供有效的Token";
//        } else if (nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
//            message = "登录信息已过期，请重新登录";
//        } else if (nle.getType().equals(NotLoginException.BE_REPLACED)) {
//            message = "您的账户已在另一台设备上登录，如非本人操作，请立即修改密码";
//        } else if (nle.getType().equals(NotLoginException.KICK_OUT)) {
//            message = "已被系统强制下线";
//        } else {
//            message = "当前会话未登录";
//        }
//        // 返回给前端
//        return ApiRestRes.error(MallExceptionEnum.NEED_ADMIN);
//    }

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

    //处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ApiRestRes ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        return ApiRestRes.error(MallExceptionEnum.REQUEST_PARAM_ERROR.getCode(), message);
    }

    /**
     * 拦截的是 @RequestBody 产生的异常
     *
     * @param e MethodArgumentNotValidException.class
     */
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
