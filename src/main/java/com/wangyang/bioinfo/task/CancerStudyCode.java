package com.wangyang.bioinfo.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.entity.base.BaseFile;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.pojo.entity.CancerStudy;
import com.wangyang.bioinfo.pojo.entity.Code;
import com.wangyang.bioinfo.pojo.entity.OrganizeFile;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CancerStudyCode implements ICodeResult<CancerStudy> {
//    @Autowired
//    ICancerStudyService cancerStudyService;
    @Autowired
    IOrganizeFileService organizeFileService;
    @Override
    public TaskType getTaskType() {
        return TaskType.CANCER_STUDY;
    }
    @Override
    public CancerStudy getObj(int id) {
        return null;//cancerStudyService.findById(id);
    }
    @Override
    public Map<String, String> getMap(CancerStudy cancerStudy) {
//        CancerStudyVO mappingVo = cancerStudyService.convertVo(cancerStudy);
        List<OrganizeFile> organizeFiles = organizeFileService.listAll();
        Map<String, String> map = new HashMap<>();
        if(cancerStudy.getParam()!=null ){
            String param = cancerStudy.getParam();
            param = param.replace("\"", "");
            JSONObject jsonObject = JSONObject.parseObject(param);
            for (String key : jsonObject.keySet()){
                map.put(key,jsonObject.getString(key));
            }
        }

//        map.putAll(ObjectToCollection.setConditionMap(mappingVo,"param"));
        map.putAll(organizeFiles.stream().collect(Collectors.toMap(OrganizeFile::getEnName, OrganizeFile::getAbsolutePath)));
        return map;
    }
    @Override
    public void call(Code code, User user, CodeMsg codeMsg,CancerStudy cancerStudy) {
//        if (codeMsg.getResultMap()!=null && codeMsg.getResultMap().size()!=0) {
//            List<Map<String, String>> resultList = codeMsg.getResultMap();
//            for (Map<String, String> resultMap :resultList){
//
//                CancerStudyParam cancerStudyParam = Map2Obj.convert(resultMap, CancerStudyParam.class);
//                if(cancerStudyParam.getAnalysisSoftware()==null || cancerStudyParam.getAnalysisSoftware().equals("")){
//                    throw new BioinfoException("AnalysisSoftware 不能为空！");
//                }
//                CancerStudy covertCancerStudy = cancerStudyService.convert(cancerStudyParam);
//                if (resultMap.containsKey("update")) {
//                    BeanUtil.copyProperties(covertCancerStudy, cancerStudy);
//                    cancerStudy.setParentId(cancerStudy.getId());
//                    cancerStudy.setCodeId(code.getId());
//                    cancerStudyService.saveCancerStudy(cancerStudy);
//                } else {
//                    CancerStudy cancerStudyProcess = cancerStudyService.findByParACodeId(cancerStudy.getId(), code.getId(),covertCancerStudy.getAnalysisSoftwareId());
//                    if (cancerStudyProcess == null) {
//                        cancerStudyProcess = new CancerStudy();
//                    }
//                    cancerStudyProcess.setCancerId(cancerStudy.getCancerId());
//                    cancerStudyProcess.setStudyId(cancerStudy.getStudyId());
//                    cancerStudyProcess.setDataOriginId(cancerStudy.getDataOriginId());
//                    cancerStudyProcess.setDataCategoryId(cancerStudy.getDataCategoryId());
//                    cancerStudyProcess.setAnalysisSoftwareId(cancerStudy.getAnalysisSoftwareId());
//                    cancerStudyProcess.setGse(cancerStudy.getGse());
//                    BeanUtil.copyProperties(covertCancerStudy, cancerStudyProcess);
//
//                    cancerStudyProcess.setParentId(cancerStudy.getId());
//                    cancerStudyProcess.setUserId(user.getId());
//                    cancerStudyProcess.setCodeId(code.getId());
//                    cancerStudyService.saveCancerStudy(cancerStudyProcess);
//                }
//            }
//
//        }
    }

    @Override
    public void call(User user,CancerStudy cancerStudyPar,List<CancerStudy> cancerStudies) {

        for (CancerStudy cancerStudy: cancerStudies){
//            CancerStudy findCancerStudy = cancerStudyService.checkExist(cancerStudy);
//            if(findCancerStudy!=null){
//                BeanUtils.copyProperties(cancerStudy,findCancerStudy,"id");
//                CancerStudy cancerStudy1 = cancerStudyService.saveCancerStudy(findCancerStudy, user);
//                if(!cancerStudy1.getStatus())return;
//            }else {
//                CancerStudy cancerStudy1 = cancerStudyService.saveCancerStudy(cancerStudy, user);
//                if(!cancerStudy1.getStatus())return;
//            }
        }
        if(cancerStudies.size()!=0){
            cancerStudyPar.setCodeIsSuccess(true);
//            cancerStudyService.save(cancerStudyPar);
        }
    }

    @Override
    public void getRealTimeMsg(User user, String msg) {

    }


    @Override
    public List<CancerStudy> getProcessObj(CancerStudy cancerStudy,Code code, String json) {

        if(json==null|| json.equals(""))return null;
        json = json.replace("\"","");
        List<CancerStudy> cancerStudies = JSONArray.parseArray(json, CancerStudy.class);
        List<CancerStudy> cancerStudyList = cancerStudies.stream().map(canS -> {
            CancerStudy proCan = new CancerStudy();
            BeanUtils.copyProperties(cancerStudy, proCan);
            BeanUtil.copyProperties(canS, proCan);
//            proCan.setParentId(cancerStudy.getId());
//            proCan.setId(null);
            proCan.setCodeId(code.getId());
            return proCan;
        }).collect(Collectors.toList());
        for (CancerStudy cancerStudy1 :cancerStudies){
//            if(!Paths.get(cancerStudy1.getAbsolutePath()).toFile().exists()){
////                CancerStudy findCancerStudy = cancerStudyService.checkExist(cancerStudy);
////                if(cancerStudy!=null){
////                    cancerStudyService.delete(findCancerStudy);
////                }
//                throw new BioinfoException(cancerStudy1.getAbsolutePath()+" 不存在！");
//            }
        }
        return cancerStudyList;
    }

    @Override
    public Boolean checkExist(List<CancerStudy> cancerStudies) {
       for(CancerStudy cancerStudy : cancerStudies){
//           CancerStudy can = cancerStudyService.checkExist(cancerStudy);
//           if(can==null || (can!=null && !Paths.get(can.getAbsolutePath()).toFile().exists() )){
//                return false;
//           }
       }
        return true;
    }

    @Override
    public CancerStudy save(CancerStudy cancerStudy,User user) {
        return null;//cancerStudyService.saveCancerStudy(cancerStudy,user);
    }

    @Override
    public Boolean checkRun(Code code, BaseFile baseFile) {
//        List<CancerStudy> cancerStudies = cancerStudyService.findByCode(code);
        return null;//cancerStudies.contains(baseFile);
    }
}
