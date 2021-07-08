package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.Attachment;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.repository.base.BaseFileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangyang
 * @date 2021/6/13
 */
public interface AttachmentRepository extends BaseFileRepository<Attachment> {
}
