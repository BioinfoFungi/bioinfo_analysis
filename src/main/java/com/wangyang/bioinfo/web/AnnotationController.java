package com.wangyang.bioinfo.web;


import com.wangyang.bioinfo.pojo.file.Annotation;
import com.wangyang.bioinfo.pojo.param.AnnotationParam;
import com.wangyang.bioinfo.pojo.param.AnnotationQuery;
import com.wangyang.bioinfo.pojo.vo.AnnotationSimpleVO;
import com.wangyang.bioinfo.service.IAnnotationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/annotation")
public class AnnotationController {
    @Autowired
    IAnnotationService annotationService;

    @GetMapping("/simple")
    public Page<AnnotationSimpleVO> pageSimple(AnnotationQuery annotationQuery,
                                         @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<Annotation> annotations = annotationService.pageBy(annotationQuery, pageable);
            return annotationService.convert(annotations);
    }
    @GetMapping
    public Page<Annotation> page(AnnotationQuery annotationQuery,
                                         @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<Annotation> annotations = annotationService.pageBy(annotationQuery, pageable);
        return annotations;
    }
    @PostMapping
    public Annotation add(@RequestBody AnnotationParam annotationParam){
        return annotationService.add(annotationParam);
    }
    @PostMapping("/update/{id}")
    public Annotation update(@PathVariable("id") Integer id,@RequestBody AnnotationParam annotationParam){
        Annotation annotation = annotationService.findById(id);
        BeanUtils.copyProperties(annotationParam,annotation,"id");
        return annotationService.saveAndCheckFile(annotation);
    }

    @GetMapping("/del/{id}")
    public Annotation del(@PathVariable("id") int id){
        return annotationService.delBy(id);
    }
}
