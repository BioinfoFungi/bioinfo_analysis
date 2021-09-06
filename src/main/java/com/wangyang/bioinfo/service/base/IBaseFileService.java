package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.pojo.enums.FileLocation;
import com.wangyang.bioinfo.pojo.support.FileTree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * @author wangyang
 * @date 2021/7/8
 */
public interface IBaseFileService<FILE extends BaseFile> extends ICrudService<FILE,Integer>{


    List<FILE> findByFileName(String fileName);

    FILE findByUUIDAndCheck(String uuid);

//    FILE findById(Integer Id);


    FILE findByUUID(String uuid);

    Page<FILE> pageBy(FILE baseFileQuery, String keywords, Pageable pageable);

    FILE download(String uuid,FileLocation fileLocation, HttpServletResponse response);

    FILE download(Integer id, FileLocation fileLocation, HttpServletResponse response);


    FILE checkFileExist(int id);

    void saveContent(@NonNull String absolutePath, String content);

    String getFileContent(@NonNull String absolutePath);

    List<FileTree> listFiles(@NotNull String strPath);
}
