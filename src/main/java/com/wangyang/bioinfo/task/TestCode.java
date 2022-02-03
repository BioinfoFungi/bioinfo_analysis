package com.wangyang.bioinfo.task;

import com.alibaba.fastjson.JSONObject;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.entity.base.BaseFile;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.pojo.entity.CancerStudy;
import com.wangyang.bioinfo.pojo.entity.Code;
import com.wangyang.bioinfo.pojo.entity.OrganizeFile;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TestCode implements ICodeResult<CancerStudy>{
//    @Autowired
//    ICancerStudyService cancerStudyService;
    @Autowired
    IOrganizeFileService organizeFileService;
    @Autowired
    private  WebSocketServer springWebSocketHandler;
    @Override
    public void call(Code code, User user, CodeMsg codeMsg, CancerStudy cancerStudy) {

    }

    @Override
    public Map<String, String> getMap(CancerStudy cancerStudy) {
//        CancerStudyVO mappingVo = cancerStudyService.convertVo(cancerStudy);
        List<OrganizeFile> organizeFiles = organizeFileService.listAll();
        Map<String, String> map = new HashMap<>();
        if(cancerStudy.getParam()!=null ){
            JSONObject jsonObject = JSONObject.parseObject(cancerStudy.getParam());
            for (String key : jsonObject.keySet()){
                map.put(key,jsonObject.getString(key));
            }
        }
//        map.putAll(ObjectToCollection.setConditionMap(mappingVo,"param"));
        map.putAll(organizeFiles.stream().collect(Collectors.toMap(OrganizeFile::getEnName, OrganizeFile::getAbsolutePath)));
        return map;
    }

    @Override
    public CancerStudy getObj(int id) {
        return null;//cancerStudyService.findById(id);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.TEST;
    }

    @Override
    public void getRealTimeMsg(User user, String msg) {
//        System.out.println(">>>>>>>>>>>>>>>>>>"+msg);
//        springWebSocketHandler.sendMessageToUser(user.getUsername(), BaseResponse.ok(BaseResponse.MsgType.TEST_CODE,msg));
    }


    @Override
    public Boolean checkExist(List<CancerStudy> cancerStudies) {
        return null;
    }

    @Override
    public CancerStudy save(CancerStudy cancerStudy, User user) {
        return null;
    }

    @Override
    public List<CancerStudy> getProcessObj(CancerStudy cancerStudy, Code code, String json) {
        return null;
    }

    @Override
    public void call(User user, CancerStudy cancerStudy, List<CancerStudy> cancerStudies) {

    }

    @Override
    public Boolean checkRun(Code code, BaseFile baseFile) {
        return null;
    }
}
