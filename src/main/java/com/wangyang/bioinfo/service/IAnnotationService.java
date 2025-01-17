package com.wangyang.bioinfo.service;


import com.wangyang.bioinfo.pojo.entity.Annotation;
import com.wangyang.bioinfo.pojo.param.AnnotationParam;
import com.wangyang.bioinfo.pojo.param.AnnotationQuery;
import com.wangyang.bioinfo.pojo.vo.AnnotationSimpleVO;
import com.wangyang.bioinfo.service.base.IBaseFileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAnnotationService extends IBaseFileService<Annotation> {
    Page<Annotation> pageBy(AnnotationQuery annotationQuery, Pageable pageable);

    Page<AnnotationSimpleVO> convert(Page<Annotation> annotations);
    Annotation saveAndCheckFile(Annotation file);

    Annotation findByParACodeId(Integer parentId, Integer codeId);

    Annotation add(AnnotationParam annotationParam);
}
