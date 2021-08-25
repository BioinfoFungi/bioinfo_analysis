package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.file.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.pojo.param.StudyParam;
import com.wangyang.bioinfo.pojo.trem.Study;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.service.IStudyService;
import com.wangyang.bioinfo.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@RestController
@RequestMapping("/api/study")
public class StudyController {
    @Autowired
    IStudyService studyService;

    @Autowired
    IOrganizeFileService organizeFileService;

    @GetMapping
    public Page<Study> page(BaseTermParam baseTermParam, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable) {
        Page<Study> studies = studyService.pageBy(baseTermParam,pageable);
        return studies;
    }
    @PostMapping
    public Study add(@RequestBody  StudyParam studyParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return  studyService.addStudy(studyParam,user);
    }

    @GetMapping("/createTSVFile")
    public void createTSVFile(HttpServletResponse response){
        studyService.createTSVFile(response);
    }
    @GetMapping("/listAll")
    public List<Study> listAll(){
        return studyService.listAll();
    }


    @GetMapping("/init/{name}")
    public BaseResponse initData(@PathVariable("name") String name){
        OrganizeFile organizeFile = organizeFileService.findByEnName(name);
        studyService.initData(organizeFile.getAbsolutePath());
        return BaseResponse.ok("初始化完成!");
    }

    @GetMapping("/init")
    public BaseResponse initDataBy(@RequestParam("path") String path){
        studyService.initData(path);
        return BaseResponse.ok("初始化完成!");
    }
}
