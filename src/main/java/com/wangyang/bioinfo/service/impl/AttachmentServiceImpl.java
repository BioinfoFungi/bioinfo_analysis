package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.handle.FileHandler;
import com.wangyang.bioinfo.handle.FileHandlers;
import com.wangyang.bioinfo.pojo.Attachment;
import com.wangyang.bioinfo.pojo.Project;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.enums.AttachmentType;
import com.wangyang.bioinfo.pojo.param.AttachmentParam;
import com.wangyang.bioinfo.pojo.support.UploadResult;
import com.wangyang.bioinfo.repository.AttachmentRepository;
import com.wangyang.bioinfo.service.IAttachmentService;
import com.wangyang.bioinfo.service.IProjectService;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author wangyang
 * @date 2021/6/13
 */
@Service
public class AttachmentServiceImpl implements IAttachmentService {
    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    IProjectService projectService;

    @Autowired
    FileHandlers fileHandlers;

    @Override
    public Attachment addAttachment(AttachmentParam attachmentParam) {
        Attachment attachment = findAttachmentByPathOrName(attachmentParam.getProjectId(),attachmentParam.getFileName(),attachmentParam.getPath());

        if(attachment==null){
            attachment =new Attachment();
        }
        BeanUtils.copyProperties(attachmentParam,attachment);
        if(attachmentParam.getFileType()==null){
            String extension = FilenameUtils.getExtension(attachmentParam.getPath());
            if(extension.equals("")){
                throw new BioinfoException("路径必须添加后缀名！");
            }
            attachment.setFileType(extension);
        }
        if(attachment.getFileName()==null){
            String basename = FilenameUtils.getBasename(attachmentParam.getPath());
            attachment.setFileName(basename);
        }
        attachment = attachmentRepository.save(attachment);
        return attachment;
    }

    @Override
    public Attachment delAttachment(int id, User user) {
        Attachment attachment = findAttachmentById(id);
        Project project = projectService.findProjectById(attachment.getProjectId());
        if(user.getId()!=attachment.getUserId()&&!user.getUsername().equals("admin")&&project.getUserId()!=user.getId()){
            throw new BioinfoException("您不是该附件的创建者或者该项目创建者,不能删除！");
        }
        attachmentRepository.delete(attachment);
        return attachment;
    }

    @Override
    public Attachment findAttachmentById(int id) {
        Optional<Attachment> attachmentOptional = attachmentRepository.findById(id);
        if(!attachmentOptional.isPresent()){
            throw new BioinfoException("操作的对象不存在！");
        }
        return attachmentOptional.get();
    }

    @Override
    public Attachment findAttachmentByName(String name) {
        List<Attachment> attachments = attachmentRepository.findAll(new Specification<Attachment>() {
            @Override
            public Predicate toPredicate(Root<Attachment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("fileName"),name)).getRestriction();
            }
        });
        if(attachments.size()==0){
            return null;
        }
        return attachments.get(0);
    }

    @Override
    public Attachment findAttachmentByPathOrName(int projectId,String name,String path) {
        List<Attachment> attachments = attachmentRepository.findAll(new Specification<Attachment>() {
            @Override
            public Predicate toPredicate(Root<Attachment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicateAnd = criteriaBuilder.and(criteriaBuilder.equal(root.get("projectId"), projectId));
                Predicate predicateOr = criteriaBuilder.or(criteriaBuilder.equal(root.get("fileName"), name), criteriaBuilder.equal(root.get("path"), path));
                return criteriaQuery.where(predicateOr,predicateAnd).getRestriction();
            }
        });
        if(attachments.size()==0){
            return null;
        }
        return attachments.get(0);
    }

    @Override
    public List<Attachment> findAllById(Collection<Integer> id) {
        return null;
    }

    @Override
    public Page<Attachment> pageAttachment(Pageable pageable) {
        Page<Attachment> attachments = attachmentRepository.findAll(pageable);
        return attachments;
    }

    @Override
    public Attachment updateAttachment(Attachment attachment) {
        return null;
    }

    @Override
    public Attachment upload(MultipartFile file,AttachmentParam attachmentParam) {
        String  originalFilename = file.getOriginalFilename();
        String basename = FilenameUtils.getBasename(originalFilename);
        if(attachmentParam.getFileName()==null){
            attachmentParam.setFileName(basename);
        }
        Attachment attachment = findAttachmentByPathOrName(attachmentParam.getProjectId(),attachmentParam.getFileName(),"");
        UploadResult uploadResult;
        if(attachment==null){
            attachment = new Attachment();
            uploadResult = fileHandlers.upload(file, AttachmentType.LOCAL);
        }else {
            if(attachment.getPath().startsWith("upload")){
                uploadResult = fileHandlers.upload(file, AttachmentType.LOCAL,attachment.getPath());
            }else {
                uploadResult = fileHandlers.upload(file, AttachmentType.LOCAL);
            }
        }


        BeanUtils.copyProperties(attachmentParam,attachment);
//        if(attachmentParam.getFileName()==null){
//            attachment.setFileName(uploadResult.getFilename());
//        }
        if(attachment.getFileType()==null){
            attachment.setFileType(uploadResult.getSuffix());
        }

        attachment.setPath(uploadResult.getFilePath());
        attachment.setSize(uploadResult.getSize());

        attachment = attachmentRepository.save(attachment);
        return attachment;
    }

    @Override
    public Attachment upload(int id, MultipartFile file) {
        Attachment attachment = new Attachment();
        return new Attachment();
    }

    @Override
    public Page<Attachment> pageAttachmentByProjectId(int projectId, Pageable pageable) {
        Page<Attachment> attachments = attachmentRepository.findAll(new Specification<Attachment>() {
            @Override
            public Predicate toPredicate(Root<Attachment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("projectId"),projectId)).getRestriction();
            }
        }, pageable);
        return attachments;
    }

    @Override
    public List<Attachment> listAttachmentByProjectId(int projectId) {
        List<Attachment> attachments = attachmentRepository.findAll(new Specification<Attachment>() {
            @Override
            public Predicate toPredicate(Root<Attachment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("projectId"), projectId)).getRestriction();
            }
        });
        return attachments;
    }
}
