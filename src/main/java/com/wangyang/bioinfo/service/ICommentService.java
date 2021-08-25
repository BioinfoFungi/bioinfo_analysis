package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Comment;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.vo.CommentVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/13
 */
public interface ICommentService {
    Comment addComment(Comment comment);
    Comment delComment(int id,User user);
    List<Comment> delALLById(Collection<Integer> id);
    List<Comment> delCommentByProjectId(int projectId);

    Comment findCommentById(int id);
    List<Comment> findCommentByProjectId(int id);
    List<Comment> findAllById(Collection<Integer> id);
    Page<Comment> pageComment(Pageable pageable);
    Comment updateComment(Comment comment);

    Page<Comment> pageCommentByProjectId(int projectId, Pageable pageable);
    Page<CommentVo> convertCommentVo(Page<Comment> commentPage);

}
