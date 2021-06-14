package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Attachment;
import com.wangyang.bioinfo.pojo.Comment;
import com.wangyang.bioinfo.pojo.Project;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.vo.CommentVo;
import com.wangyang.bioinfo.pojo.vo.ProjectVo;
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
    Comment delComment(int id,int userId);
    Comment findCommentById(int id);
    List<Comment> findAllById(Collection<Integer> id);
    Page<Comment> pageComment(Pageable pageable);
    Comment updateComment(Comment comment);

    Page<Comment> pageCommentByProjectId(int projectId, Pageable pageable);
    Page<CommentVo> convertCommentVo(Page<Comment> commentPage);

}
