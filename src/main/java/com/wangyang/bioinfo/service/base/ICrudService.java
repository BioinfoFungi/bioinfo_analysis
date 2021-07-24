package com.wangyang.bioinfo.service.base;

import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/27
 */
public interface ICrudService<DOMAIN, ID> {
    List<DOMAIN> listAll();
    DOMAIN add(@NonNull DOMAIN domain);
    DOMAIN save(@NonNull DOMAIN domain);
    List<DOMAIN> initData(String filePath);
}
