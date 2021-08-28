package com.wangyang.bioinfo.util;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangyang
 * @date 2021/6/14
 */
public class CacheStore {
    public final static ConcurrentHashMap<String,Object> CACHE_CONTAINER = new ConcurrentHashMap<>();
    public static Optional<String> get(String key){
        return Optional.ofNullable((String) CACHE_CONTAINER.get(key));
    }
    public static void setValue(String key,Object value){
        CACHE_CONTAINER.put(key,value);
    }
    public static void clearByKey(String key){
        CACHE_CONTAINER.remove(key);
    }
    public static void clearAll(){
        CACHE_CONTAINER.clear();
    }

    public static String getValue(String key){
        return (String)CACHE_CONTAINER.get(key);
    }

    public static  <T>T save(String key,T t){
        CACHE_CONTAINER.put(key,t);
        return t;
    }
    public static  <T>List<T> getList(String key,Class<T> clz){
        Object o = CACHE_CONTAINER.get(key);
        if(o!=null){
            List<T> list = (List<T>)o;
            return list;
        }
        return null;
    }
    public static  <T>T get(String key,Class<T> clz){
        Object o = CACHE_CONTAINER.get(key);
        if(clz.isInstance(o)){
            return clz.cast(o);
        }
        return null;
    }
}
