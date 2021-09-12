package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangyang
 * @date 2021/6/13
 */
public interface CommentRepository extends JpaRepository<Comment,Integer>
        , JpaSpecificationExecutor<Comment> {
}
