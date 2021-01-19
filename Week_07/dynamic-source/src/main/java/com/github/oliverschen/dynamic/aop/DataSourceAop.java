package com.github.oliverschen.dynamic.aop;

import com.github.oliverschen.dynamic.annotation.Ds;
import com.github.oliverschen.dynamic.config.DataSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author ck
 */
@Slf4j
@Aspect
@Component
public class DataSourceAop {


    @Around("@annotation(com.github.oliverschen.dynamic.annotation.Ds)")
    public Object changeDataSource(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Ds ds = method.getAnnotation(Ds.class);
        log.info("==> 当前使用的数据源：{}",ds.dsType().getName());
        DataSourceHolder.setDataSource(ds.dsType().getName());
        Object proceed = point.proceed();
        DataSourceHolder.clearDataSource();
        return proceed;
    }

}
