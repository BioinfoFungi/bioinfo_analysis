package com.wangyang.bioinfo.web;


import com.wangyang.bioinfo.pojo.file.Annotation;
import com.wangyang.bioinfo.pojo.param.AnnotationQuery;
import com.wangyang.bioinfo.pojo.vo.AnnotationSimpleVO;
import com.wangyang.bioinfo.service.IAnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/annotation")
public class AnnotationController {
    @Autowired
    IAnnotationService annotationService;

    @GetMapping
    public Page<AnnotationSimpleVO> page(AnnotationQuery annotationQuery, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<Annotation> annotations = annotationService.pageBy(annotationQuery, pageable);
        return annotationService.convert(annotations);
    }

}
