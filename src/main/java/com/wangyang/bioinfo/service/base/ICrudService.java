package com.wangyang.bioinfo.service.base;

import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/27
 */
public interface ICrudService<DOMAIN, ID> {
    List<DOMAIN> listAll();
    DOMAIN add(@NonNull DOMAIN domain);
    DOMAIN save(@NonNull DOMAIN domain);
    DOMAIN findById(@NonNull ID id);
    void createTSVFile(HttpServletResponse response);



    File createTSVFile(List<DOMAIN> domains, String filePath, String[] heads);

    List<DOMAIN> tsvToBean(String filePath);
    DOMAIN delBy(ID id);
    List<DOMAIN> initData(String filePath);
}
