package com.example.myknife;

import android.app.Activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyKnife {
    public static void bind(Activity activity) {
        String className = activity.getClass().getName();
        String bindClassName = className + "$$ViewBinding";
        try {
            Class clazz = Class.forName(bindClassName);
            Object object = clazz.newInstance();
            Method bindView = clazz.getDeclaredMethod("bind", activity.getClass());
            bindView.setAccessible(true);
            bindView.invoke(object, activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
