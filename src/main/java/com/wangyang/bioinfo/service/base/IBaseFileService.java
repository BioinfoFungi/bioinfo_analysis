package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.pojo.enums.FileLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/8
 */
public interface IBaseFileService<FILE extends BaseFile> extends ICrudService<FILE,Integer>{


    List<FILE> findByFileName(String fileName);

    FILE findByUUIDAndCheck(String uuid);

//    FILE findById(Integer Id);


    FILE findByUUID(String uuid);

    Page<FILE> pageBy(FILE baseFileQuery, String keywords,Pageable pageable);

    FILE download(String uuid,FileLocation fileLocation, HttpServletResponse response);

    FILE download(Integer id, FileLocation fileLocation, HttpServletResponse response);

    FILE checkFileExist(int id);
}
