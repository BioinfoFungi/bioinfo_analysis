package com.wangyang.bioinfo.handle;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.pojo.file.Annotation;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.service.IAnnotationService;
import com.wangyang.bioinfo.util.Map2Obj;
import com.wangyang.bioinfo.util.ObjectToCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AnnotationCode implements ICodeResult<Annotation>{
    @Autowired
    IAnnotationService annotationService;
    @Override
    public void call(Code code, User user, CodeMsg codeMsg,Annotation annotation) {
        Annotation ann = Map2Obj.convert(codeMsg.getResultMap(), Annotation.class);
        annotation.setAbsolutePath(ann.getAbsolutePath());
        annotationService.saveAndCheckFile(annotation);
    }

    @Override
    public Map<String, Object> getMap(Annotation annotation) {
        Map<String, Object> map = new HashMap<>();
        map.putAll(ObjectToCollection.setConditionObjMap(annotation));
        return map;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.ANNOTATION;
    }

    @Override
    public Annotation getObj(int id){
       return annotationService.findById(id);
    }

}
