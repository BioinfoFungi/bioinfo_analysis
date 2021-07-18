package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.pojo.param.BaseFileParam;
import com.wangyang.bioinfo.pojo.param.BaseFileQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author wangyang
 * @date 2021/7/8
 */
public interface IAbstractBaseFileService<FILE extends BaseFile> extends ICrudService<FILE,Integer>{

    FILE findByEnNameAndCheck(String name);

    FILE findById(Integer Id);

    FILE findByEnName(String name);

    Page<FILE> pageBy(BaseFileQuery baseFileQuery, Pageable pageable);

    FILE download(String enName, HttpServletResponse response);

    FILE download(Integer id, HttpServletResponse response);

    FILE save(BaseFileParam baseFileParam);

    FILE upload(MultipartFile multipartFile, String path,BaseFileParam baseFileParam);
}
