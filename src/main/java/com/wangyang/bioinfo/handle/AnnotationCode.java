package com.wangyang.bioinfo.handle;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.pojo.file.Annotation;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.service.IAnnotationService;
import com.wangyang.bioinfo.util.BeanUtil;
import com.wangyang.bioinfo.util.Map2Obj;
import com.wangyang.bioinfo.util.ObjectToCollection;
import org.springframework.beans.BeanUtils;
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
        if (codeMsg.getResultMap()!=null && codeMsg.getResultMap().size()!=0) {
            Annotation ann = Map2Obj.convert(codeMsg.getResultMap(), Annotation.class);
            if(codeMsg.getIsUpdate()){
                annotation.setAbsolutePath(ann.getAbsolutePath());
                annotationService.saveAndCheckFile(annotation);
            }else {
                Annotation annotationProcess = annotationService.findByParACodeId(annotation.getId(),code.getId());
                if(annotationProcess==null){
                    annotationProcess = new Annotation();
                }
                BeanUtils.copyProperties(annotation,annotationProcess,"id");
                BeanUtil.copyProperties(ann, annotationProcess);
                annotationProcess.setParentId(annotation.getId());
                annotationProcess.setCodeId(code.getId());
                annotationService.saveAndCheckFile(annotationProcess);
            }
        }
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

    @Override
    public void getRealTimeMsg(User user, String msg) {

    }
}
