package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.Project;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.param.ProjectContentParam;
import com.wangyang.bioinfo.pojo.param.ProjectParam;
import com.wangyang.bioinfo.pojo.param.ProjectQuery;
import com.wangyang.bioinfo.pojo.param.ProjectSDK;
import com.wangyang.bioinfo.pojo.vo.ProjectListVo;
import com.wangyang.bioinfo.pojo.vo.ProjectVo;
import com.wangyang.bioinfo.service.IProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/12
 */
@RestController
@RequestMapping("/api/project")
public class ProjectController {
    @Autowired
    IProjectService projectService;



    @GetMapping
    public Page<ProjectListVo> page(ProjectQuery projectQuery, @PageableDefault(sort = {"id"},direction =  Sort.Direction.DESC) Pageable pageable){
        Page<Project> projects = projectService.pageBy(projectQuery,pageable);
        return projectService.convertProjectListVo(projects);
    }
    @PostMapping
    public Project add( @Valid @RequestBody ProjectParam projectParam,HttpServletRequest request){
        Project project = new Project();
        BeanUtils.copyProperties(projectParam,project);
        User user = (User) request.getAttribute("user");
        project.setUserId(user.getId());
        project = projectService.addProject(project,projectParam.getUserIds());
        return project;
    }
    @PostMapping("/update/{id}")
    public Project update(@PathVariable("id")int id, @Valid @RequestBody ProjectParam projectParam, HttpServletRequest request){
        Project project = new Project();
        BeanUtils.copyProperties(projectParam,project);
        User user = (User) request.getAttribute("user");
        project = projectService.updateProject(id,project,user,projectParam.getUserIds());
        return project;
    }

    @PostMapping("/updateSDK/{id}")
    public Project updateSDK(@PathVariable("id")int id, @Valid @RequestBody ProjectSDK projectSDK){
        Project project = projectService.updateProject(id,projectSDK);
        return project;
    }


    @PostMapping("/updateContent/{id}")
    public Project updateContent(@PathVariable("id")int id, @Valid @RequestBody ProjectContentParam projectContentParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        Project project = projectService.updateProject(id,projectContentParam.getContent(),user);
        return project;
    }

    @GetMapping("/find/{id}")
    public ProjectVo findProjectById(@PathVariable("id")int id){
        Project project = projectService.findProjectById(id);
        return projectService.convertProjectVo(project);
    }

    @GetMapping("/find")
    public  List<Project> findProjectByName(@RequestParam("name")String name){
        List<Project> project = projectService.findProjectByName(name);
        return project;
    }

    @GetMapping("/del/{id}")
    public Project delProject(@PathVariable("id")int id,HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return projectService.delProject(id,user);
    }
}
