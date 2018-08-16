package com.example.thirdplatform.aspect;

import android.widget.Toast;

import com.example.thirdplatform.MyApplication;
import com.example.thirdplatform.utils.NetUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class NetAspect {
    @Pointcut("execution(@com.example.thirdplatform.aspect.CheckNet * **(..))")
    public void execute() {

    }

    @Around("execute()")
    public void beforeAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!NetUtils.isNetworkAvailable(MyApplication.getApplication())) {
            Toast.makeText(MyApplication.getApplication(), "网络不可用", Toast.LENGTH_SHORT).show();
        } else {
            joinPoint.proceed(joinPoint.getArgs());
        }
    }
}
