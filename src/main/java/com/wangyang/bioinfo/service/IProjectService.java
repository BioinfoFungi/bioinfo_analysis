package com.wangyang.bioinfo.service;


import com.wangyang.bioinfo.pojo.Project;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.param.ProjectQuery;
import com.wangyang.bioinfo.pojo.param.ProjectSDK;
import com.wangyang.bioinfo.pojo.vo.ProjectListVo;
import com.wangyang.bioinfo.pojo.vo.ProjectVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * @author wangyang
 * @date 2021/6/12
 */
public interface IProjectService {
    Project addProject(Project inputProject, Set<Integer> userIds);
    Project delProject(int id, User user);
    Project findProjectById(int id);
    List<Project> findProjectByName(String name);
    Page<Project> pageBy(ProjectQuery projectQuery, Pageable pageable);
    Project updateProject(int id,Project project, User user, Set<Integer> userIds);
    Project updateProject(int id,String content, User user);
    Project updateProject(int id, ProjectSDK projectSDK);
    // ---------------------------------------

    ProjectVo convertProjectVo(Project project);
    Page<ProjectListVo> convertProjectListVo(Page<Project> projects);
}
