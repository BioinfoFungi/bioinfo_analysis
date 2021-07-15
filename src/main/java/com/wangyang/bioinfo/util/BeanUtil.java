package com.wangyang.bioinfo.util;


import com.google.common.collect.Lists;
import com.wangyang.bioinfo.pojo.base.BaseRNA;
import com.wangyang.bioinfo.pojo.txt.Annotation;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

}
