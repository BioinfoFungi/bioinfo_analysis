package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.pojo.enums.FileLocation;
import com.wangyang.bioinfo.pojo.param.BaseFileParam;
import com.wangyang.bioinfo.pojo.param.BaseFileQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/8
 */
public interface IBaseFileService<FILE extends BaseFile> extends ICrudService<FILE,Integer>{


    List<FILE> findByFileName(String fileName);

    FILE findByUUIDAndCheck(String uuid);

    FILE findById(Integer Id);


    FILE findByUUID(String uuid);

    Page<FILE> pageBy(BaseFileQuery baseFileQuery, Pageable pageable);

    FILE download(String uuid,FileLocation fileLocation, HttpServletResponse response);

    FILE download(Integer id, FileLocation fileLocation, HttpServletResponse response);

}
