package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.ProjectUser;
import com.wangyang.bioinfo.pojo.authorize.User;

import java.util.Collection;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/14
 */
public interface IProjectUserService {
    ProjectUser addProjectUser(ProjectUser projectUser);
    List<ProjectUser> saveAll(Collection<ProjectUser> projectUsers);
    ProjectUser delProjectUser(int id, User user);
    List<ProjectUser> delALLById(Collection<Integer> id);
    List<ProjectUser> delProjectUserByProjectId(int projectId);

    ProjectUser findProjectUserById(int id);
    List<ProjectUser> findProjectUserByProjectId(int id);
}
