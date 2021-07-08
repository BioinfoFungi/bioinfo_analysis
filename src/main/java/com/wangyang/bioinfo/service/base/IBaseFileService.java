package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.pojo.param.BaseFileQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author wangyang
 * @date 2021/7/8
 */
public interface IBaseFileService<FILE extends BaseFile> extends ICrudService<FILE,Integer>{

    FILE findByEnNameAndCheck(String name);

    FILE findByEnName(String name);

    Page<FILE> pageBy(BaseFileQuery baseFileQuery, Pageable pageable);
}
