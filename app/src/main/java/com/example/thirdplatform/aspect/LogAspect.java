package com.example.thirdplatform.aspect;

import android.os.SystemClock;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LogAspect {
    @Pointcut("execution(@com.example.thirdplatform.aspect.CheckTime * **(..))")
    public void execute() {

    }

    @Around("execute()")
    public void aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = SystemClock.uptimeMillis();
        Object object = joinPoint.getThis();
        joinPoint.proceed(joinPoint.getArgs());
        long endTime = SystemClock.uptimeMillis();
        Log.e(object.getClass().getName(), joinPoint.toShortString() + " costs " + (endTime - startTime) + " millis");
    }

}
