package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.*;
import com.wangyang.bioinfo.pojo.enums.FileLocation;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import com.wangyang.bioinfo.pojo.param.CancerStudyQuery;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVo;
import com.wangyang.bioinfo.service.ICancerStudyService;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

    @Autowired
    IOrganizeFileService organizeFileService;

    @GetMapping
    public Page<? extends CancerStudy> page(CancerStudyQuery cancerStudyQuery,
                                    @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable,
                                    @RequestParam(value = "more", defaultValue = "false") Boolean more){
        Page<CancerStudy> cancerStudies = cancerStudyService.pageCancerStudy(cancerStudyQuery,pageable);
        if(more){
            return cancerStudyService.convertVo(cancerStudies);
        }
        return cancerStudies;
    }


    @GetMapping("/listByCancerId/{id}")
    public List<CancerStudyVo> listByCancerId(@PathVariable("id") Integer id){
        List<CancerStudy> cancerStudies = cancerStudyService.listByCancerId(id);
        return cancerStudyService.convertVo(cancerStudies);
    }


    @PostMapping
    public CancerStudy add(@RequestBody @Valid CancerStudyParam cancerStudyParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        CancerStudy cancerStudy = cancerStudyService.saveCancerStudy(cancerStudyParam,user);
        return cancerStudy;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CancerStudy upload(@RequestParam("file") MultipartFile file, CancerStudyParam cancerStudyParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return  cancerStudyService.upload(file,cancerStudyParam);
    }

//    @GetMapping("/findByCategory")
//    public Page<CancerStudy> findByCategory(@Valid CancerStudyQuery findCancer, @PageableDefault(sort = {"id"},direction = DESC,size = 3)Pageable pageable){
//        return cancerStudyService.pageCancerStudy(findCancer,pageable);
//    }
//
//    @GetMapping("/findVoByCategory")
//    public Page<CancerStudyVo> findVoByCategory(@Valid CancerStudyQuery findCancer, Pageable pageable){
//        return cancerStudyService.pageCancerStudyVo(findCancer,pageable);
//    }

    @GetMapping("/createTSVFile")
    public void createTSVFile(HttpServletResponse response){
        cancerStudyService.createTSVFile(response);
    }

    @GetMapping("/download/{uuid}")
    public CancerStudy download(@PathVariable("uuid") String uuid,
                                @RequestParam(value = "location",defaultValue = "LOCAL")FileLocation fileLocation,
                                HttpServletResponse response){
        CancerStudy cancerStudy = cancerStudyService.download(uuid,fileLocation, response);
        return cancerStudy;
    }
    @GetMapping("/downloadById/{Id}")
    public CancerStudy downloadById(@PathVariable("Id") Integer id,
                                    @RequestParam(value = "location",defaultValue = "LOCAL")FileLocation fileLocation,
                                    HttpServletResponse response){
        CancerStudy cancerStudy = cancerStudyService.download(id, fileLocation,response);
        return cancerStudy;
    }

    @GetMapping("/findById/{id}")
    public CancerStudy findById(@PathVariable("id") Integer id){
        return cancerStudyService.findCancerStudyById(id);
    }


    @GetMapping("/findOne/{uuid}")
    public CancerStudy findByUUID(@PathVariable("uuid") String uuid){
        return cancerStudyService.findByUUIDAndCheck(uuid);
    }

    @GetMapping("/init/{name}")
    public BaseResponse initData(@PathVariable("name") String name){
        OrganizeFile organizeFile = organizeFileService.findByEnName(name);
        cancerStudyService.initData(organizeFile.getAbsolutePath());
        return BaseResponse.ok("CancerStudy初始化完成!");
    }

    @GetMapping("/init")
    public BaseResponse initDataBy(@RequestParam("path") String path){
        cancerStudyService.initData(path);
        return BaseResponse.ok("初始化完成!");
    }
}
