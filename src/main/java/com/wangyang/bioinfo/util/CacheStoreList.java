package com.wangyang.bioinfo.util;

import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class CacheStoreList<T,ID> {
    public  Map<String, Map<ID,T>> CACHE_CONTAINER =null;
    public CacheStoreList(){
        if(CACHE_CONTAINER==null){
            CACHE_CONTAINER = new ConcurrentHashMap<>();
        }
    }
    public List<T> get(Class<T> clz){
        Map<ID, T> idtMap = CACHE_CONTAINER.get(clz.getName());
        if (idtMap==null)return null;
        List<T> list = idtMap.values().stream().collect(Collectors.toList());
        return list;
    }
    public  Map<ID,T>  add(Map<ID,T> map){
        return CACHE_CONTAINER.put(getKey(),map);
    }
    public  Map<ID,T>  add(ID id, T t){
        Map<ID,T> map =new HashMap<>();
        map.put(id,t);
        return CACHE_CONTAINER.put(getKey(),map);
    }
    public void update(ID id, T entity) {
        Map<ID, T> idtMap = CACHE_CONTAINER.get(getKey());
        if(idtMap.containsKey(id)){
            idtMap.put(id,entity);
        }
    }
//
    protected String getKey(){
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
        return type.getName();
    }


}
