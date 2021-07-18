package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.pojo.file.OrganizeFile;
import com.wangyang.bioinfo.service.IBaseFileService;
import com.wangyang.bioinfo.service.base.IAbstractBaseFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author wangyang
 * @date 2021/7/18
 */
@RestController
@RequestMapping("/api/base_file")
public class BaseFileController {
    @Autowired
    IBaseFileService baseFileService;

    @GetMapping("/download/{enName}")
    public BaseFile download(@PathVariable("enName") String enName, HttpServletResponse response){
        BaseFile baseFile = baseFileService.download(enName, response);
        return baseFile;
    }
    @GetMapping("/downloadById/{Id}")
    public BaseFile downloadById(@PathVariable("Id") Integer Id, HttpServletResponse response){
        BaseFile baseFile = baseFileService.download(Id, response);
        return baseFile;
    }
    @GetMapping("/findById/{Id}")
    public BaseFile findById(@PathVariable("Id") Integer Id){
        return baseFileService.findById(Id);
    }
    @GetMapping("/findOne/{enName}")
    public BaseFile findByEnName(@PathVariable("enName") String enName){
        return baseFileService.findByEnNameAndCheck(enName);
    }


}
