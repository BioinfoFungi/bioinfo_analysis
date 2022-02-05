package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.handle.CrudHandlers;
import com.wangyang.bioinfo.pojo.annotation.Anonymous;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.entity.CancerStudy;
import com.wangyang.bioinfo.pojo.entity.Code;
import com.wangyang.bioinfo.pojo.entity.Task;
import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.pojo.param.CancerStudyQuery;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.util.BaseResponse;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.CacheStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@RestController
@RequestMapping("/api/cancer_study")
public class CancerStudyController {
//    @Autowired
//    ICancerStudyService cancerStudyService;
//    @Autowired
//    ITermMappingService termMappingService;
    @Autowired
    IOrganizeFileService organizeFileService;

    @Autowired
    CrudHandlers crudHandlers;


    @Anonymous
    @GetMapping("/{crudEnum}")
    public Page<? extends BaseEntity> page(@PathVariable(value = "crudEnum") CrudType crudEnum,
                                           @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable,
                                           @RequestParam(required = false) String keywords,
                                           @RequestParam(value = "more", defaultValue = "false") Boolean more){
//        Page<CancerStudy> cancerStudies = cancerStudyService.pageBy(cancerStudyQuery,pageable);
//        termMappingHandlers.pageBy(pageable, CrudType.TEST);
//
//        if(more){
//            return cancerStudyService.convertVo(cancerStudies);
//        }
//        return cancerStudies;
        return crudHandlers.pageBy(crudEnum,pageable,keywords);
    }

    @GetMapping("/{crudEnum}/findById")
    public BaseEntity findById(@PathVariable(value = "crudEnum") CrudType crudEnum,
                        Integer id){
        return crudHandlers.findById(crudEnum,id);
    }
    @GetMapping("/{crudEnum}/getFields")
    public  List<String> getFields(@PathVariable(value = "crudEnum") CrudType crudEnum){
        return crudHandlers.getFields(crudEnum);
    }
//    @GetMapping
//    @Anonymous
//    public Page<? extends TermMapping> pageBy(CancerStudyQuery cancerStudyQuery,
//                                            @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable,
//                                            @RequestParam(value = "more", defaultValue = "false") Boolean more){
//        Page page = termMappingService.pageBy(pageable);
//        Page<CancerStudy> cancerStudies = cancerStudyService.pageBy(cancerStudyQuery,pageable);
//        if(more){
//            termMappingService.convertVo((TermMapping) page);
//
//        }
//        return cancerStudies;
//    }

//    @GetMapping("/pageByCodeId/{codeId}")
//    @Anonymous
//    public Page<? extends TermMapping> pageByCodeId(@PathVariable("codeId") Integer codeId,
//                                                    CancerStudyQuery cancerStudyQuery,
//                                            @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable,
//                                            @RequestParam(value = "more", defaultValue = "false") Boolean more){
//        Page<CancerStudy> cancerStudies = cancerStudyService.pageByCodeId(codeId,cancerStudyQuery,pageable);
//        if(more){
//            return cancerStudyService.convertVo(cancerStudies);
//        }
//        return cancerStudies;
//    }
//
//
//    @GetMapping("list")
//    @Anonymous
//    public List<? extends TermMapping> list(CancerStudyQuery cancerStudyQuery,
//                                            @RequestParam(value = "more", defaultValue = "false") Boolean more){
//        List<CancerStudy> cancerStudies = cancerStudyService.listBy(cancerStudyQuery);
//        if(more){
//            return cancerStudyService.convertVo(cancerStudies);
//        }
//        return cancerStudies;
//    }
//
//
//    @GetMapping("/listByCancerId/{id}")
//    public List<? extends TermMapping> listByCancerId(@PathVariable("id") Integer id){
//        List<CancerStudy> cancerStudies = cancerStudyService.listByCancerId(id);
//        return cancerStudyService.convertVo(cancerStudies);
//    }
//
//    @PostMapping("/update/{id}")
//    public CancerStudy update(@PathVariable("id")Integer id,@RequestBody @Valid CancerStudyParam cancerStudyParam, HttpServletRequest request){
//        User user = (User) request.getAttribute("user");
//        CancerStudy cancerStudy = cancerStudyService.updateCancerStudy(id,cancerStudyParam,user);
//        return cancerStudy;
//    }
//
//

//
//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public CancerStudy upload(@RequestParam("file") MultipartFile file, CancerStudyParam cancerStudyParam, HttpServletRequest request){
//        User user = (User) request.getAttribute("user");
//        return  cancerStudyService.upload(file,cancerStudyParam);
//    }
//
//
////    @GetMapping("/findByCategory")
////    public Page<CancerStudy> findByCategory(@Valid CancerStudyQuery findCancer, @PageableDefault(sort = {"id"},direction = DESC,size = 3)Pageable pageable){
////        return cancerStudyService.pageCancerStudy(findCancer,pageable);
////    }
////
////    @GetMapping("/findVoByCategory")
////    public Page<CancerStudyVo> findVoByCategory(@Valid CancerStudyQuery findCancer, Pageable pageable){
////        return cancerStudyService.pageCancerStudyVo(findCancer,pageable);
////    }
//
    @PostMapping("/{crudEnum}/createTSVFile")
    public void createTSVFile(@PathVariable(value = "crudEnum") CrudType crudEnum,HttpServletResponse response){
        crudHandlers.createTSVFile(crudEnum,response);
    }
//
//    @GetMapping("/download/{uuid}")
//    public CancerStudy download(@PathVariable("uuid") String uuid,
//                                @RequestParam(value = "location",defaultValue = "LOCAL")FileLocation fileLocation,
//                                HttpServletResponse response){
//        CancerStudy cancerStudy = cancerStudyService.download(uuid,fileLocation, response);
//        return cancerStudy;
//    }
//    @GetMapping("/downloadById/{Id}")
//    public CancerStudy downloadById(@PathVariable("Id") Integer id,
//                                    @RequestParam(value = "location",defaultValue = "LOCAL")FileLocation fileLocation,
//                                    HttpServletResponse response){
//        CancerStudy cancerStudy = cancerStudyService.download(id, fileLocation,response);
//        return cancerStudy;
//    }
//
//    @GetMapping("/findById/{id}")
//    public TermMapping findById(@PathVariable("id") Integer id,
//                                @RequestParam(value = "more", defaultValue = "false") Boolean more){
//        CancerStudy cancerStudy = cancerStudyService.findCancerStudyById(id);
//        if(more){
//            return cancerStudyService.convertVo(cancerStudy);
//        }
//        return cancerStudy;
//    }
//
//
//    @GetMapping("/findOne/{uuid}")
//    public CancerStudy findByUUID(@PathVariable("uuid") String uuid){
//        return cancerStudyService.findByUUIDAndCheck(uuid);
//    }
//
//    @GetMapping("/init/{name}")
//    public BaseResponse initData(@PathVariable("name") String name){
//        OrganizeFile organizeFile = organizeFileService.findByEnName(name);
//        cancerStudyService.initData(organizeFile.getAbsolutePath(),false);
//        return BaseResponse.ok("CancerStudy初始化完成!");
//    }
//
    @GetMapping("/init/{crudEnum}")
    public BaseResponse initDataBy(@PathVariable(value = "crudEnum") CrudType crudEnum,
                                   @RequestParam String name,
                                   @RequestParam(value = "isEmpty", defaultValue = "false") Boolean isEmpty){

        String path = CacheStore.getValue("workDir")+"/data/"+name;
        if(!Paths.get(path).toFile().exists()){
            throw new BioinfoException(path+"不存在!!");
        }
        List<CancerStudy> cancerStudyList = crudHandlers.initData(path, isEmpty,crudEnum);
        return BaseResponse.ok("导入["+cancerStudyList.size()+"]个对象！");
    }

    @GetMapping("/addTask/{crudEnum}")
    public Task addTask(@PathVariable(value = "crudEnum") CrudType crudEnum,
                        Integer id,
                        Integer codeId,
                        HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return crudHandlers.addTask(crudEnum,id,codeId,user);
    }
    @GetMapping("/runTask/{crudEnum}")
    public Task runTask(@PathVariable(value = "crudEnum") CrudType crudEnum,
                        Integer id,
                        HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return crudHandlers.runTask(crudEnum,id,user);
    }


    @GetMapping("/{crudEnum}/delById")
    public BaseEntity delById(@PathVariable(value = "crudEnum") CrudType crudEnum,
                           Integer id,
                           HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return  crudHandlers.delById(crudEnum,id,user);
    }
    @PostMapping("/{crudEnum}/add")
    public BaseEntity add(@PathVariable(value = "crudEnum") CrudType crudEnum,
                          @RequestBody  Map<String,Object> map,
                           HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return crudHandlers.add(crudEnum,map,user);
    }

    @PostMapping("/{crudEnum}/update")
    public BaseEntity update(@PathVariable(value = "crudEnum") CrudType crudEnum,
                                Integer id,
                                @RequestBody Map<String,Object> map,
                                HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return crudHandlers.update(crudEnum,id,map,user);
    }
//
//    @GetMapping("/checkFile/{id}")
//    @Anonymous
//    public CancerStudy checkFileExist(@PathVariable("id") Integer id){
//        CancerStudy cancerStudy = cancerStudyService.checkFileExist(id);
//        return cancerStudy;
//    }
}
