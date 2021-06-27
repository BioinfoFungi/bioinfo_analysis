package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.Attachment;
import com.wangyang.bioinfo.pojo.CancerStudy;
import com.wangyang.bioinfo.pojo.Project;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.param.AttachmentParam;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import com.wangyang.bioinfo.pojo.param.CancerStudyQuery;
import com.wangyang.bioinfo.pojo.param.FindCancer;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVo;
import com.wangyang.bioinfo.pojo.vo.ProjectListVo;
import com.wangyang.bioinfo.service.ICancerStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@RestController
@RequestMapping("/api/cancer_study")
public class CancerStudyController {
    @Autowired
    ICancerStudyService cancerStudyService;

    @GetMapping
    public Page<CancerStudyVo> page(CancerStudyQuery cancerStudyQuery, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<CancerStudy> cancerStudies = cancerStudyService.pageCancerStudy(cancerStudyQuery,pageable);
        return cancerStudyService.convertProjectVo(cancerStudies);
    }


    @PostMapping
    public CancerStudy add(@RequestBody CancerStudyParam cancerStudyParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        cancerStudyParam.setUserId(user.getId());
        CancerStudy cancerStudy = cancerStudyService.addCancerStudy(cancerStudyParam);
        return cancerStudy;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CancerStudy upload(@RequestParam("file") MultipartFile file, CancerStudyParam cancerStudyParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        cancerStudyParam.setUserId(user.getId());
        return  cancerStudyService.upload(file,cancerStudyParam);
    }

    @GetMapping("/findOne")
    public CancerStudy findBy(FindCancer findCancer){
        return cancerStudyService.findCancerStudyByAndThree(findCancer);
    }
}
