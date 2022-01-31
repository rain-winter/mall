package com.imooc.mall.exception;

import com.imooc.mall.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
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
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e) {
        log.error("Default Exception", e);
        // 处理系统的异常
        return ApiRestResponse.error(ImoocMallExceptionEnum.SYSTEM_ERROR);
    }

    /**
     * 拦截自定义异常并处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ImoocMallException.class)
    @ResponseBody
    public Object handleImoocMallException(ImoocMallException e) {
        log.error("ImoocMallException", e);
        return ApiRestResponse.error(e.getCode(), e.getMessage());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestResponse handleMethodArgNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        //  e.getBindingResult() 得到 @Valid的验证结果
        return handleBindingResult(e.getBindingResult());

    }

    private ApiRestResponse handleBindingResult(BindingResult result) {

        // 把异常处理为对外暴漏的提示
        List<String> list = new ArrayList<>();
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors(); // 得到所有的错误
            for (int i = 0; i < allErrors.size(); i++) {
                ObjectError objectError = allErrors.get(i);  // // 得到每一个错误
                String message = objectError.getDefaultMessage();
                System.out.println("---------------------------------");
                System.out.println(message);
                System.out.println("---------------------------------");
                list.add(message);
            }
        }
        // 此时已经初始化了
        if (list.size() == 0) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse.error(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR.getCode(), list.toString());

    }
}
