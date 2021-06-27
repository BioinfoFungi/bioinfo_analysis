package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.*;
import com.wangyang.bioinfo.pojo.param.StudyParam;
import com.wangyang.bioinfo.service.IStudyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping
    public Page<Study> page(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable) {
        Page<Study> studies = studyService.pageStudy(pageable);
        return studies;
    }
    @PostMapping
    public Study add(@RequestBody  StudyParam studyParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return  studyService.addStudy(studyParam,user);
    }
    @GetMapping("/listAll")
    public List<Study> listAll(){
        return studyService.listAll();
    }
}
