package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.entity.ProjectUser;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.repository.ProjectUserRepository;
import com.wangyang.bioinfo.service.IProjectUserService;
import com.wangyang.bioinfo.util.BioinfoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author wangyang
 * @date 2021/6/14
 */
@Service
public class ProjectUserServiceImpl implements IProjectUserService {

    @Autowired
    ProjectUserRepository projectUserRepository;

    @Override
    public ProjectUser addProjectUser(ProjectUser projectUser) {
        return projectUserRepository.save(projectUser);
    }

    @Override
    public List<ProjectUser> saveAll(Collection<ProjectUser> projectUsers) {
        return projectUserRepository.saveAll(projectUsers);
    }

    @Override
    public ProjectUser delProjectUser(int id, User user) {
        return null;
    }

    @Override
    public List<ProjectUser> delALLById(Collection<Integer> id) {

        return null;
    }

    @Override
    public List<ProjectUser> delProjectUserByProjectId(int projectId) {
        List<ProjectUser> projectUsers = findProjectUserByProjectId(projectId);
        projectUserRepository.deleteAll(projectUsers);
        return projectUsers;
    }

    @Override
    public ProjectUser findProjectUserById(int id) {
        Optional<ProjectUser> projectUserOptional = projectUserRepository.findById(id);
        if(!projectUserOptional.isPresent()){
            throw new BioinfoException("要操作的对象不存在！");
        }
        ProjectUser projectUser = projectUserOptional.get();
        return projectUser;
    }

    @Override
    public List<ProjectUser> findProjectUserByProjectId(int id) {
        List<ProjectUser> projectUsers = projectUserRepository.findAll(new Specification<ProjectUser>() {
            @Override
            public Predicate toPredicate(Root<ProjectUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("projectId"),id)).getRestriction();
            }
        });
        return projectUsers;
    }
}
