package com.wangyang.bioinfo.handle;

import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.service.ICancerStudyService;
import com.wangyang.bioinfo.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RCode implements ICodeResult{

    @Autowired
    ICancerStudyService cancerStudyService;

    @Override
    public CodeType getType() {
        return null;
    }

    @Override
    public void call(CodeMsg codeMsg) {
//        if(codeMsg.getCancerStudyParam()!=null){
//            CancerStudy covertCancerStudy = cancerStudyService.convert(codeMsg.getCancerStudyParam());
//            if(codeMsg.getIsUpdate()){
//                BeanUtil.copyProperties(covertCancerStudy,cancerStudy);
//                cancerStudyService.saveCancerStudy(cancerStudy);
//            }else {
//                BeanUtil.copyProperties(covertCancerStudy,cancerStudyProcess);
//                cancerStudyService.saveCancerStudy(cancerStudyProcess);
//            }
//        }
//        if(codeMsg.getStatus()){
//            task.setIsSuccess(true);
//        }
    }


}
