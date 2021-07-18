package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.file.Attachment;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.AttachmentParam;
import com.wangyang.bioinfo.pojo.param.BaseFileQuery;
import com.wangyang.bioinfo.pojo.param.OrganizeFileParam;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/7/8
 */
@RestController
@RequestMapping("/api/organize_file")
public class OrganizeFileController {

    @Autowired
    IOrganizeFileService organizeService;
    @PostMapping
    public OrganizeFile add(@RequestBody OrganizeFileParam organizeFileParam,HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        organizeFileParam.setUserId(user.getId());
        return organizeService.save(organizeFileParam);
    }

    @GetMapping
    public Page<OrganizeFile> page(BaseFileQuery baseFileQuery, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<OrganizeFile> organizeFiles = organizeService.pageBy(baseFileQuery, pageable);
        return organizeFiles;
    }


    @GetMapping("/download/{enName}")
    public OrganizeFile download(@PathVariable("enName") String enName, HttpServletResponse response,HttpServletRequest request){
        OrganizeFile organizeFile = organizeService.download(enName, response,request);
        return organizeFile;
    }

    @GetMapping("/findOne/{enName}")
    public OrganizeFile findByEnName(@PathVariable("enName") String enName){
        return organizeService.findByEnNameAndCheck(enName);
    }
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public OrganizeFile upload(@RequestParam("file") MultipartFile file, OrganizeFileParam organizeFileParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        organizeFileParam.setUserId(user.getId());
        return  organizeService.upload(file,"organizeFile",organizeFileParam);
    }

}
