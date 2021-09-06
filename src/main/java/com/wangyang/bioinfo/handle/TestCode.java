package com.wangyang.bioinfo.handle;

import com.alibaba.fastjson.JSONObject;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.pojo.file.OrganizeFile;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVO;
import com.wangyang.bioinfo.service.ICancerStudyService;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.util.BaseResponse;
import com.wangyang.bioinfo.util.ObjectToCollection;
import com.wangyang.bioinfo.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TestCode implements ICodeResult<CancerStudy>{
    @Autowired
    ICancerStudyService cancerStudyService;
    @Autowired
    IOrganizeFileService organizeFileService;
    @Autowired
    private  WebSocketServer springWebSocketHandler;
    @Override
    public void call(Code code, User user, CodeMsg codeMsg, CancerStudy cancerStudy) {

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
    public CancerStudy getObj(int id) {
        return cancerStudyService.findById(id);
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
}
