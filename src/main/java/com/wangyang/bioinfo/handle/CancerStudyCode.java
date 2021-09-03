package com.wangyang.bioinfo.handle;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.pojo.file.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVO;
import com.wangyang.bioinfo.service.ICancerStudyService;
import com.wangyang.bioinfo.service.ICodeService;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.util.BeanUtil;
import com.wangyang.bioinfo.util.Map2Obj;
import com.wangyang.bioinfo.util.ObjectToCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CancerStudyCode implements ICodeResult<CancerStudy> {
    @Autowired
    ICancerStudyService cancerStudyService;
    @Autowired
    IOrganizeFileService organizeFileService;
    @Override
    public TaskType getTaskType() {
        return TaskType.CANCER_STUDY;
    }
    @Override
    public CancerStudy getObj(int id) {
        return cancerStudyService.findById(id);
    }
    @Override
    public Map<String, Object> getMap(CancerStudy cancerStudy) {
        CancerStudyVO mappingVo = cancerStudyService.convertVo(cancerStudy);
        List<OrganizeFile> organizeFiles = organizeFileService.listAll();
        Map<String, Object> map = new HashMap<>();
        map.putAll(ObjectToCollection.setConditionObjMap(mappingVo));
        map.putAll(organizeFiles.stream().collect(Collectors.toMap(OrganizeFile::getEnName, OrganizeFile::getAbsolutePath)));
        return map;
    }
    @Override
    public void call(Code code, User user, CodeMsg codeMsg,CancerStudy cancerStudy) {
        if (codeMsg.getResultMap()!=null && codeMsg.getResultMap().size()!=0) {
            Map<String, String> resultMap = codeMsg.getResultMap();
            CancerStudyParam cancerStudyParam = Map2Obj.convert(resultMap, CancerStudyParam.class);
            CancerStudy covertCancerStudy = cancerStudyService.convert(cancerStudyParam);
            if (codeMsg.getIsUpdate()) {
                BeanUtil.copyProperties(covertCancerStudy, cancerStudy);
                cancerStudyService.saveCancerStudy(cancerStudy);
            } else {
                CancerStudy cancerStudyProcess = cancerStudyService.findByParACodeId(cancerStudy.getId(), code.getId());
                if (cancerStudyProcess == null) {
                    cancerStudyProcess = new CancerStudy();
                }
                cancerStudyProcess.setCancerId(cancerStudy.getCancerId());
                cancerStudyProcess.setStudyId(cancerStudy.getStudyId());
                cancerStudyProcess.setDataOriginId(cancerStudy.getDataOriginId());
                cancerStudyProcess.setDataCategoryId(cancerStudy.getDataCategoryId());
                cancerStudyProcess.setAnalysisSoftwareId(cancerStudy.getAnalysisSoftwareId());
                cancerStudyProcess.setGse(cancerStudy.getGse());
                BeanUtil.copyProperties(covertCancerStudy, cancerStudyProcess);

                cancerStudyProcess.setParentId(cancerStudy.getId());
                cancerStudyProcess.setUserId(user.getId());
                cancerStudyProcess.setCodeId(code.getId());
                cancerStudyService.saveCancerStudy(cancerStudyProcess);
            }
        }
    }
}
