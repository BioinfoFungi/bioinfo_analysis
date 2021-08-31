package com.wangyang.bioinfo.service.impl;


import com.wangyang.bioinfo.handle.FileHandlers;
import com.wangyang.bioinfo.pojo.file.Annotation;
import com.wangyang.bioinfo.pojo.param.AnnotationQuery;
import com.wangyang.bioinfo.pojo.vo.AnnotationSimpleVO;
import com.wangyang.bioinfo.repository.AnnotationRepository;
import com.wangyang.bioinfo.service.IAnnotationService;
import com.wangyang.bioinfo.service.base.BaseFileService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AnnotationFileServiceImpl
        extends BaseFileService<Annotation>
        implements IAnnotationService {

    private final AnnotationRepository annotationFileRepository;
    private final FileHandlers fileHandlers;
    public AnnotationFileServiceImpl(FileHandlers fileHandlers, AnnotationRepository annotationFileRepository) {
        super(fileHandlers, annotationFileRepository);
        this.annotationFileRepository = annotationFileRepository;
        this.fileHandlers=fileHandlers;
    }

    @Override
    public Page<Annotation> pageBy(AnnotationQuery annotationQuery, Pageable pageable) {
        Annotation annotation = new Annotation();
        Set<String> sets = new HashSet<>();
        sets.add("enName");
        Specification<Annotation> specification = buildSpecByQuery(annotation, annotationQuery.getKeyword(), sets);
        return annotationFileRepository.findAll(specification,pageable);
    }

    @Override
    public Page<AnnotationSimpleVO> convert(Page<Annotation> annotations) {
        return annotations.map(annotation -> {
           AnnotationSimpleVO annotationSimpleVO =new AnnotationSimpleVO();
            BeanUtils.copyProperties(annotation, annotationSimpleVO);
            return annotationSimpleVO;
        });
    }
}
