package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Attachment;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.param.AttachmentParam;
import com.wangyang.bioinfo.service.base.IBaseFileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/13
 */
public interface IAttachmentService extends IBaseFileService<Attachment> {
    Attachment addAttachment(AttachmentParam attachment);
    Attachment delAttachment(int id, User user);
    Attachment findAttachmentById(int id);
    Attachment findAttachmentByName(String name);

    /**
     * 根据path或者name查找附件 projectId and (name or path)
     * @param name
     * @param path
     * @return
     */
    Attachment findAttachmentByPathOrName(int projectId,String name,String path);
    List<Attachment> findAllById(Collection<Integer> id);
    Page<Attachment> pageAttachment(Pageable pageable);
    Attachment updateAttachment(Attachment attachment);
    //----------------------------------------
    Attachment upload(@NonNull MultipartFile file,AttachmentParam attachmentParam);
    Attachment upload(int id,@NonNull MultipartFile file);

    // 带page条件查询
    Page<Attachment> pageAttachmentByProjectId(int projectId,Pageable pageable);

    List<Attachment> listAttachmentByProjectId(int projectId);
}
