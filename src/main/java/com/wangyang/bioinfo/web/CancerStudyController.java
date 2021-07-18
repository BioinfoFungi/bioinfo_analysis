package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.*;
import com.wangyang.bioinfo.pojo.file.Attachment;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import com.wangyang.bioinfo.pojo.param.CancerStudyQuery;
import com.wangyang.bioinfo.pojo.param.FindCancer;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVo;
import com.wangyang.bioinfo.service.ICancerStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

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
        return cancerStudyService.convertVo(cancerStudies);
    }


    @GetMapping("/listByCancerId/{id}")
    public List<CancerStudyVo> listByCancerId(@PathVariable("id") Integer id){
        List<CancerStudy> cancerStudies = cancerStudyService.listByCancerId(id);
        return cancerStudyService.convertVo(cancerStudies);
    }


    @PostMapping
    public CancerStudy add(@RequestBody CancerStudyParam cancerStudyParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        cancerStudyParam.setUserId(user.getId());
        CancerStudy cancerStudy = cancerStudyService.saveCancerStudy(cancerStudyParam);
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

    @GetMapping("/download/{enName}")
    public CancerStudy download(@PathVariable("enName") String enName, HttpServletResponse response,HttpServletRequest request){
        CancerStudy cancerStudy = cancerStudyService.download(enName, response,request);
        return cancerStudy;
    }
    @GetMapping("/findOne/{enName}")
    public CancerStudy findByEnName(@PathVariable("enName") String enName){
        return cancerStudyService.findByEnNameAndCheck(enName);
    }
}
