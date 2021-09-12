package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.entity.Comment;
import com.wangyang.bioinfo.pojo.entity.Project;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.vo.CommentVo;
import com.wangyang.bioinfo.repository.CommentRepository;
import com.wangyang.bioinfo.service.ICommentService;
import com.wangyang.bioinfo.service.IProjectService;
import com.wangyang.bioinfo.service.IUserService;
import com.wangyang.bioinfo.util.BioinfoException;
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
 * @date 2021/6/13
 */
@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    IProjectService projectService;

    @Autowired
    IUserService userService;

    @Override
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment delComment(int id,User user) {
        Comment comment = findCommentById(id);
        Project project = projectService.findProjectById(comment.getProjectId());

        if(comment.getUserId()!=user.getId() && project.getUserId()!=user.getId()&& !user.getUsername().equals("admin")){
            throw new BioinfoException("您不是这条评论的创建者或者该项目创建者！");
        }

        commentRepository.deleteById(id);
        return comment;
    }

    @Override
    public   List<Comment> delALLById(Collection<Integer> id) {
        List<Comment> comments = findAllById(id);
        commentRepository.deleteAll(comments);
        return comments;
    }

    @Override
    public List<Comment> delCommentByProjectId(int projectId) {
        List<Comment> comments = findCommentByProjectId(projectId);
        commentRepository.deleteAll(comments);
        return comments;
    }

    @Override
    public Comment findCommentById(int id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if(!commentOptional.isPresent()){
            throw  new BioinfoException("操作额对象不存在！");
        }
        Comment comment = commentOptional.get();
        return comment;
    }

    @Override
    public List<Comment> findCommentByProjectId(int projectId) {
        List<Comment> comments = commentRepository.findAll(new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("projectId"), projectId)).getRestriction();
            }
        });
        return comments;
    }

    @Override
    public List<Comment> findAllById(Collection<Integer> id) {
        return commentRepository.findAllById(id);
    }

    @Override
    public Page<Comment> pageComment(Pageable pageable) {
        return null;
    }

    @Override
    public Comment updateComment(Comment comment) {
        return null;
    }

    @Override
    public Page<Comment> pageCommentByProjectId(int projectId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findAll(new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("projectId"),projectId)).getRestriction();
            }
        }, pageable);
        return comments;
    }

    @Override
    public Page<CommentVo> convertCommentVo(Page<Comment> commentPage) {
        List<Comment> comments = commentPage.getContent();
        Set<Integer> ids = ServiceUtil.fetchProperty(comments, Comment::getUserId);
        List<User> users = userService.findAllById(ids);
        Map<Integer, User> userMap = ServiceUtil.convertToMap(users, User::getId);
        Page<CommentVo> commentVoPage = commentPage.map(comment -> {
            CommentVo commentVo = new CommentVo();
            commentVo.setUser(userMap.get(comment.getUserId()));
            BeanUtils.copyProperties(comment,commentVo);
            return commentVo;
        });
        return commentVoPage;
    }
}
