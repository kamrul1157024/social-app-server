package com.kamrul.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Hashtable;

public class Utils {

    public static<T> T override(T obj,T ...overrides){
        if(overrides.length==0) return obj;
        Class overrideClass = overrides[0].getClass();
        Method[] methods = overrideClass.getDeclaredMethods();
        Class classOfObj = obj.getClass();
        Hashtable<String,Class[]> methodParamTypes = new Hashtable<>();
        Arrays.stream(methods).forEach(method->{
            try {
                if(method.getName().startsWith("set")) {
                    methodParamTypes.put(method.getName(), method.getParameterTypes());
                }
                if(!method.getName().startsWith("get")) return;
                String setterMethodName = String.format("set%s",method.getName().substring(3));
                if(method.invoke(overrides[0])!=null){
                    Method setter = classOfObj.getDeclaredMethod(setterMethodName,methodParamTypes.get(setterMethodName));
                    setter.invoke(obj,method.invoke(overrides[0]));
                }
            } catch (IllegalAccessException |InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {}
        });
        return obj;
    }
}
