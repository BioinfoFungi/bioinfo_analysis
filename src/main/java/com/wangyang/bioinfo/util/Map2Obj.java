package com.wangyang.bioinfo.util;

import com.wangyang.bioinfo.pojo.param.CancerStudyParam;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class Map2Obj {

    public static  <T>T convert(Map<String,String> resultMap, Class<T> clz){
        T t = null;
        try {
            Method[] methods = clz.getMethods();
            for (Method method :methods){
                String name = method.getName();
                if(name.startsWith("set")){
                    int length = name.length();
                    String basename = name.substring(3,length);
                    String preName = basename.substring(0, 1).toLowerCase();
                    String var = preName+basename.substring(1);
                    if(resultMap.containsKey(var)){
                        if(t==null){
                            t = clz.newInstance();
                        }
                        method.invoke(t,resultMap.get(var).replaceAll(" ",""));
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return t;
    }
}
