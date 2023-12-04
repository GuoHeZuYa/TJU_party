package edu.twt.party.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class WebLogAspect {
    private final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(* edu.twt.party.controller..*.*(..))")
    public void apiOperationLog(){}

    @Before("apiOperationLog()")
    public void logBeforeInvokeApi(JoinPoint joinPoint){
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();

            //todo: something wrong
            String message = "Request Sender: "
                    // .concat(request.getHeader("domain")).concat(" ")
                    .concat("Request URL: "+ request.getRequestURL().toString()).concat(" ")
                    .concat("Request Args: "+ Arrays.toString(joinPoint.getArgs()));
            logger.info(message);
        } catch (Exception e){
            logger.error("Request failed ",e);
        }
    }



    @AfterThrowing(value = "apiOperationLog()", throwing = "e")
    public void logAfterThrowingException(JoinPoint joinPoint, Throwable e){
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            String className = joinPoint.getTarget().getClass().getName();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            String methodName = method.getName();
            String url = request.getRequestURL().toString();
            String args = Arrays.toString(joinPoint.getArgs());
            String message = "Exception thrown: "
                    .concat(url).concat(" ")
                    .concat(className).concat(" ")
                    .concat(methodName).concat(" ")
                    .concat(args);
            logger.error(message,e);
        } catch (Exception ee){
            logger.error("Request failed ",ee);
        }
    }
}