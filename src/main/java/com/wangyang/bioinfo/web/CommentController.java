package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.entity.Comment;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.param.CommentParam;
import com.wangyang.bioinfo.pojo.vo.CommentVo;
import com.wangyang.bioinfo.service.ICommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/6/13
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    ICommentService commentService;

    @GetMapping
    public Page<Comment> page(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<Comment> comments = commentService.pageComment(pageable);
        return comments;
    }

    @GetMapping("/projectId/{projectId}")
    public Page<CommentVo> pageByProjectId(@PathVariable("projectId") int projectId, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<Comment> comments = commentService.pageCommentByProjectId(projectId,pageable);
        return commentService.convertCommentVo(comments);
    }


    @PostMapping
    public Comment add(@RequestBody @Validated CommentParam commentParam, HttpServletRequest request){
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentParam,comment);
        User user = (User) request.getAttribute("user");
        comment.setUserId(user.getId());
        comment = commentService.addComment(comment);
        return comment;
    }

    @GetMapping("/del/{id}")
    public Comment del(@PathVariable("id") int id, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        Comment comment = commentService.delComment(id,user);
        return comment;
    }

}
