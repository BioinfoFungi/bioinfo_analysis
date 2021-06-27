package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.*;
import com.wangyang.bioinfo.pojo.param.CancerStudyQuery;
import com.wangyang.bioinfo.pojo.param.ProjectQuery;
import com.wangyang.bioinfo.pojo.param.ProjectSDK;
import com.wangyang.bioinfo.pojo.vo.ProjectListVo;
import com.wangyang.bioinfo.pojo.vo.ProjectVo;
import com.wangyang.bioinfo.repository.ProjectRepository;
import com.wangyang.bioinfo.service.ICommentService;
import com.wangyang.bioinfo.service.IProjectService;
import com.wangyang.bioinfo.service.IProjectUserService;
import com.wangyang.bioinfo.service.IUserService;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.MarkdownUtils;
import com.wangyang.bioinfo.util.ServiceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @author wangyang
 * @date 2021/6/12
 */
@Service
public class ProjectServiceImpl implements IProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    IUserService userService;

    @Autowired
    ICommentService commentService;

    @Autowired
    IProjectUserService projectUserService;

    @Override
    public Project addProject(Project inputProject, Set<Integer> userIds) {
        Project project = projectRepository.save(inputProject);
        List<ProjectUser> projectUsers = new ArrayList<>();
        List<User> users = userService.findAllById(userIds);
        for (User user :users){
            ProjectUser projectUser= new ProjectUser();
            projectUser.setProjectId(project.getId());
            projectUser.setUserId(user.getId());
            projectUsers.add(projectUser);
        }
        projectUserService.saveAll(projectUsers);
        return project;
    }

    @Override
    public Project delProject(int id, User user) {
        Project project = findProjectById(id);
        if(project.getUserId()!=user.getId()&&!user.getUsername().equals("admin")){
            throw new BioinfoException("该项目不是由您创建不能删除！");
        }
        // 删除评论
        commentService.delCommentByProjectId(project.getId());
        //删除项目负责人
        projectUserService.delProjectUserByProjectId(project.getId());
        projectRepository.deleteById(id);
        return project;
    }

    @Override
    public Project findProjectById(int id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if(!projectOptional.isPresent()){
            throw new BioinfoException("要操作的Project不存在！");
        }
        Project project = projectOptional.get();
        return project;
    }

    @Override
    public List<Project> findProjectByName(String name) {
        List<Project> projects = projectRepository.findAll(new Specification<Project>() {
            @Override
            public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.like(root.get("name"), name)).getRestriction();
            }
        });
        if(projects.size()<=0){
            throw new BioinfoException("查找的项目不存在");
        }
        return projects;
    }
    private  Specification<Project> buildSpecByQuery(ProjectQuery projectQuery){
        return (Specification<Project>) (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new LinkedList<>();
            if(projectQuery.getProjectStatus()!=null){
                Predicate predicate = criteriaBuilder.equal(root.get("projectStatus"), projectQuery.getProjectStatus());
                predicates.add(predicate);
            }

            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }
    @Override
    public Page<Project> pageBy(ProjectQuery projectQuery,Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(buildSpecByQuery(projectQuery),pageable);
        return projects;
    }

    @Override
    public Project updateProject(int id,Project updateProject, User onlineUser, Set<Integer> userIds) {
        Project project = findProjectById(id);
        if(project.getUserId()!=onlineUser.getId()&&!onlineUser.getUsername().equals("admin")){
            throw new BioinfoException("该项目不是由您创建不能更新！");
        }
        BeanUtils.copyProperties(updateProject,project,"id","userId","createDate","originalContent","formatContent");
        projectRepository.save(project);

        //更新之前删除旧的责任人
        projectUserService.delProjectUserByProjectId(project.getId());
        List<ProjectUser> projectUsers = new ArrayList<>();
        List<User> users = userService.findAllById(userIds);
        for (User user :users){
            ProjectUser projectUser= new ProjectUser();
            projectUser.setProjectId(project.getId());
            projectUser.setUserId(user.getId());
            projectUsers.add(projectUser);
        }
        projectUserService.saveAll(projectUsers);

        return project;
    }

    @Override
    public Project updateProject(int id, String content, User user) {
        Project project = findProjectById(id);

        List<ProjectUser> projectUsers = projectUserService.findProjectUserByProjectId(id);
        if(projectUsers.size()<0&&project.getUserId()!=user.getId()&&!user.getUsername().equals("admin")){
            throw new BioinfoException("该项目没有指定责任人，您不是项目创建者，不能更新内容！");
        }
        Set<Integer> userIds = ServiceUtil.fetchProperty(projectUsers, ProjectUser::getUserId);
         if(!userIds.contains(user.getId())&&project.getUserId()!=user.getId()&&!user.getUsername().equals("admin")){
             throw new BioinfoException("您不是项目负责人或者项目创建者，不能更新内容！");
         }


        project.setOriginalContent(content);
        project.setFormatContent(MarkdownUtils.renderHtmlOutput(content));
        projectRepository.save(project);
        return project;
    }

    @Override
    public Project updateProject(int id, ProjectSDK projectSDK) {
        Project project = findProjectById(id);
        BeanUtils.copyProperties(projectSDK,project);
        projectRepository.save(project);
        return project;
    }

    @Override
    public ProjectVo convertProjectVo(Project project) {
        ProjectVo projectVo = new ProjectVo();
        BeanUtils.copyProperties(project,projectVo);
        User user = userService.findUserById(project.getUserId());
        projectVo.setUser(user);

        List<ProjectUser> projectUsers = projectUserService.findProjectUserByProjectId(project.getId());
        Set<Integer> userIds = ServiceUtil.fetchProperty(projectUsers, ProjectUser::getUserId);
        List<User> users = userService.findAllById(userIds);
        projectVo.setChargePerson(users);
        projectVo.setUserIds(userIds);
        return projectVo;
    }

    @Override
    public Page<ProjectListVo> convertProjectListVo(Page<Project> pageProject) {
        List<Project> projects = pageProject.getContent();
        Set<Integer> ids = ServiceUtil.fetchProperty(projects, Project::getUserId);
        List<User> users = userService.findAllById(ids);
        Map<Integer, User> userMap = ServiceUtil.convertToMap(users, User::getId);

        Page<ProjectListVo> projectListVos = pageProject.map(project -> {
            ProjectListVo projectListVo = new ProjectListVo();
            projectListVo.setUser(userMap.get(project.getUserId()));
            BeanUtils.copyProperties(project,projectListVo);
            return projectListVo;
        });
        return projectListVos;
    }
}
