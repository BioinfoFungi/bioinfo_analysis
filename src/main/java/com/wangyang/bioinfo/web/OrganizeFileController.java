package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.enums.FileLocation;
import com.wangyang.bioinfo.pojo.file.OrganizeFile;
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
    public OrganizeFile add(@RequestBody OrganizeFileParam organizeFileParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return organizeService.save(organizeFileParam);
    }

    @GetMapping
    public Page<OrganizeFile> page(OrganizeFile baseFileQuery, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<OrganizeFile> organizeFiles = organizeService.pageBy(baseFileQuery, "",pageable);
        return organizeFiles;
    }


    @GetMapping("/download/{uuid}")
    public OrganizeFile download(@PathVariable("uuid") String uuid,
                               @RequestParam(value = "location",defaultValue = "LOCAL")FileLocation fileLocation,
                               HttpServletResponse response){
        OrganizeFile organizeFile = organizeService.download(uuid,fileLocation, response);
        return organizeFile;
    }

    @GetMapping("/downloadById/{Id}")
    public OrganizeFile downloadById(@PathVariable("Id") Integer id,
                                   @RequestParam(value = "location",defaultValue = "LOCAL")FileLocation fileLocation,
                                     HttpServletResponse response){
        OrganizeFile organizeFile = organizeService.download(id, fileLocation,response);
        return organizeFile;
    }

    @GetMapping("/findName/{enName}")
    public OrganizeFile findByEnName(@PathVariable("enName") String enName){
        return organizeService.findByEnName(enName);
    }
    @GetMapping("/findOne/{uuid}")
    public OrganizeFile findByUUID(@PathVariable("uuid") String uuid){
        return organizeService.findByUUID(uuid);
    }
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public OrganizeFile upload(@RequestParam("file") MultipartFile file, OrganizeFileParam organizeFileParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return  organizeService.upload(file,organizeFileParam);
    }

}
