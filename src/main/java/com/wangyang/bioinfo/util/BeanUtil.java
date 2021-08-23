package com.wangyang.bioinfo.util;


import com.google.common.collect.Lists;
import com.wangyang.bioinfo.pojo.base.BaseRNA;
import com.wangyang.bioinfo.pojo.txt.Annotation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/7/10
 */
public class BeanUtil {
    /**
     * 单个对象属性复制
     *
     * @param source 复制源
     * @param clazz  目标对象class
     * @param <T>    目标对象类型
     * @param <M>    源对象类型
     * @return 目标对象
     */
    public static <T, M> T copyProperties(M source, Class<T> clazz) {
        if (Objects.isNull(source) || Objects.isNull(clazz)) {
            throw new IllegalArgumentException();
        }
        T t = null;
        try {
            t = clazz.newInstance();
            BeanUtils.copyProperties(source, t);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 列表对象属性复制
     *
     * @param <T>     目标对象类型
     * @param <M>     源对象类型
     * @param sources 源对象列表
     * @param clazz   目标对象class
     * @return 目标对象列表
     */
//    public static <T, M> List<T> copyObjects(List<Annotation> sources, Class<? extends BaseRNA> clazz) {
//        if (Objects.isNull(sources) || Objects.isNull(clazz))
//            throw new IllegalArgumentException();
//        return Optional.of(sources)
//                .orElse(Lists.newArrayList())
//                .stream().map(m -> copyProperties(m, clazz))
//                .collect(Collectors.toList());
//    }
    public static <T, M> List<T> copyObjects(List<M> sources, Class<T> clazz) {
        if (Objects.isNull(sources) || Objects.isNull(clazz)) {
            throw new IllegalArgumentException();
        }
        return Optional.of(sources)
                .orElse(Lists.newArrayList())
                .stream().map(m -> copyProperties(m, clazz))
                .collect(Collectors.toList());
    }

    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source,target,getNullPropertyNames(source));
    }


    /**
     * 获取所有字段为null的属性名
     * 用于BeanUtils.copyProperties()拷贝属性时，忽略空值
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
