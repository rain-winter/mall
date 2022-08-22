package com.mall.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 描述：     打印请求和响应信息
 */
@Aspect
@Component
public class WebLogAspect {

    private final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(public * com.mall.controller.*.*(..)))")
    public void webLog() {

    }

    /**
     * 通过连接点获取目标类/方法的信息
     * @param joinPoint (连接点)
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        //收到请求，记录请求内容
        // 非Controller层通过RequestContextHolder.getRequestAttributes()获取HttpServletRequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD :" + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName());
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs())); // 获取目标方法的参数。返回的是Obj数组
    }

    /**
     * 返回时也要记录 ，返回类型是res，返回的点是webLog()
     *
     * @param res
     * @throws JsonProcessingException
     */
    @AfterReturning(returning = "res", pointcut = "webLog()")
    public void doAfterReturning(Object res) throws JsonProcessingException {
        //处理完请求，返回内容 （jakson的方法）
        log.info("RESPONSE : " + new ObjectMapper().writeValueAsString(res));
    }
}
