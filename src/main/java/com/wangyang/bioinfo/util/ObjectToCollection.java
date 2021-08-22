package com.wangyang.bioinfo.util;

import io.swagger.models.auth.In;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ObjectToMap {
    public static List<Field> setConditionFieldMap(Object obj) {
        Class<?> clazz = obj.getClass();
        List<Field> fields = new ArrayList<>();
        //把父类包含的字段遍历出来
        while (clazz!=null){
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();

        }
        return fields;
    }
    public static Map<String, Object> setConditionObjMap(Object obj) {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            Class<?> clazz = obj.getClass();
            List<Field> fields = new ArrayList<>();
            //把父类包含的字段遍历出来
            while (clazz!=null){
                fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
                clazz = clazz.getSuperclass();

            }
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object o = field.get(obj);
                map.put(fieldName, o);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 将一个类查询方式加入map（属性值为int型时，0时不加入，
     * 属性值为String型或Long时为null和“”不加入）
     *注：需要转换的必须是对象，即有属性
     */
    public static Map<String, String> setConditionMap(Object obj) {
        Map<String, String> map = new HashMap<String,String>();
        try {
            Class<?> clazz = obj.getClass();
            List<Field> fields = new ArrayList<>();
            //把父类包含的字段遍历出来
            while (clazz!=null){
                fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
                clazz = clazz.getSuperclass();

            }
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object o = field.get(obj);
                if(o instanceof String){
                    map.put(fieldName, String.valueOf(o));
                }else if (o instanceof Integer){
                    map.put(fieldName,String.valueOf(o));
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }
    /**
     * 根据属性名获取该类此属性的值
     * @param fieldName
     * @param object
     * @return
     */
    public static Object getValueByFieldName(String fieldName,Object object){
        String firstLetter=fieldName.substring(0,1).toUpperCase();
        String getter = "get"+firstLetter+fieldName.substring(1);
        try {
            Method method = object.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(object, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }
}
