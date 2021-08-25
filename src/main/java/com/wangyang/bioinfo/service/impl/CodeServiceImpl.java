package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.annotation.QueryField;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.pojo.param.CodeParam;
import com.wangyang.bioinfo.pojo.param.CodeQuery;
import com.wangyang.bioinfo.pojo.vo.CodeVO;
import com.wangyang.bioinfo.repository.CodeRepository;
import com.wangyang.bioinfo.service.ICancerStudyService;
import com.wangyang.bioinfo.service.ICodeService;
import com.wangyang.bioinfo.service.ITaskService;
import com.wangyang.bioinfo.service.base.BaseDataCategoryServiceImpl;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.ObjectToCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/22
 */
@Service
@Slf4j
public class CodeServiceImpl extends BaseDataCategoryServiceImpl<Code>
        implements ICodeService {

    @Autowired
    CodeRepository codeRepository;

    @Autowired
    ICancerStudyService cancerStudyService;

    @Autowired
    ITaskService taskService;

    @Override
    public Code findBy(String dataCategory,String analysisSoftware){
        List<Code> codes = codeRepository.findAll(new Specification<Code>() {
            @Override
            public Predicate toPredicate(Root<Code> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(
                        criteriaBuilder.equal(root.get("dataCategory"),dataCategory),
                        criteriaBuilder.equal(root.get("analysisSoftware"),analysisSoftware)
                ).getRestriction();
            }
        });
        if(codes.size()==0){
            throw new BioinfoException("查找的code不存在！");
        }
        return codes.get(0);
    }

    @Override
    public Page<Code> pageBy(CodeQuery codeQuery, Pageable pageable) {
        Code code = super.convert(codeQuery);
        Page<Code> codePage = pageBy(code, codeQuery.getKeyWard(), pageable);
        return codePage;
    }
    @Override
    public Page<CodeVO> convertVo(Page<Code> codes) {
        return super.convertVo(codes, CodeVO.class);
    }



    @Override
    public Code saveBy(CodeParam codeParam, User user) {
        Code code = super.convert(codeParam);
        return  add(code,user);
    }

    @Override
    public Code updateBy(Integer id, CodeParam codeParam, User user) {
        Code code = super.convert(codeParam);
        return  update(id,code,user);
    }
    public Specification<Code> buildSpecBy(Code termMapping,String keyWard) {
        return (Specification<Code>) (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new LinkedList<>();
            try {
                List<Field> fields = ObjectToCollection.setConditionFieldList(termMapping);
                for(Field field : fields){
                    boolean fieldAnnotationPresent = field.isAnnotationPresent(QueryField.class);
                    if(fieldAnnotationPresent){
                        QueryField queryField = field.getDeclaredAnnotation(QueryField.class);
                        field.setAccessible(true);
                        String fieldName = field.getName();
                        Object value = field.get(termMapping);
                        if(value!=null){
                            predicates.add(criteriaBuilder.or(
                                    criteriaBuilder.equal(root.get(fieldName),value),
                                    criteriaBuilder.isNull(root.get(fieldName))
                            ));
                        }
//                        if(baseTermMappingDTO.getKeyword()!=null && !baseTermMappingDTO.getKeyword().equals("") && queryField.keyWards()){
//                            String likeCondition = String
//                                    .format("%%%s%%", StringUtils.strip(baseTermMappingDTO.getKeyword()));
//                            Predicate fileName = criteriaBuilder.like(root.get("fileName"), likeCondition);
//
//                            predicates.add(criteriaBuilder.or(fileName));
//                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0]))).getRestriction();
        };
    }
    @Override
    public List<Code> findByCan(Integer id) {
        CancerStudy cancerStudy = cancerStudyService.findById(id);
        Code code = new Code();
        BeanUtils.copyProperties(cancerStudy,code);
        List<Code> codes = codeRepository.findAll(buildSpecBy(code,null));
        return codes;
    }

    @Override
    public Code delBy(Integer id) {
        taskService.delByCodeId(id);
        return super.delBy(id);
    }

    @Override
    public CodeVO convertVo(Code code) {
        return super.convertVo(code, CodeVO.class);
    }
//    private Code findOneBy(int dataOriginId,int studyId){
//        List<Code> codes = rCodeRepository.findAll(new Specification<Code>() {
//            @Override
//            public Predicate toPredicate(Root<Code> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                return criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("dataOriginId"),dataOriginId),
//                        criteriaBuilder.equal(root.get("studyId"),studyId))).getRestriction();
//            }
//        });
//        if (codes.size()==0){
//            return null;
//        }
//        return codes.get(0);
//    }
////
//    private Code findOneCheckBy(int dataOriginId,int studyId){
//        Code code = findOneBy(dataOriginId, studyId);
//        if(code==null){
//            throw new BioinfoException("查找对象不存在!");
//        }
//        return code;
//    }
//


//    @Override
//    @Async("taskExecutor")
//    public void rCodePlot(Integer id, Integer cancerStudyId){
//        CancerStudy cancerStudy = cancerStudyService.findCancerStudyById(cancerStudyId);
//        Map<String, String> conditionMap = ObjectToMap.setConditionMap(cancerStudy);
//        rCodePlot(id,conditionMap);
//    }






//    @Override
//    @Async("taskExecutor")
//    public void runRCode(Task task, Code code, CancerStudy cancerStudy){
//        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
//        springWebSocketHandler.sendMessageToUsers(new TextMessage(Thread.currentThread().getName()+": start! >>>>>>>>>>>>>>>>>>>"));
//        task.setThreadName(Thread.currentThread().getName());
//        task.setTaskStatus(TaskStatus.RUNNING);
//        taskRepository.save(task);
//        Map<String, String> maps = ObjectToMap.setConditionMap(cancerStudy);
//
//        // Call R
//        CodeMsg codeMsg = rCall(code, maps);
//
//        // 代码执行失败不保存结果
//        if(codeMsg.getParam()!=null){
//            CancerStudy processCancerStudy = new CancerStudy();
//            User user = new User();
//            user.setId(-1);
//            processCancerStudy.setCancerId(cancerStudy.getCancerId());
//            processCancerStudy.setDataOriginId(cancerStudy.getDataOriginId());
//            processCancerStudy.setStudyId(cancerStudy.getStudyId());
//            DataCategory dataCategory = workflowService.findAndCheckByEnName(codeMsg.getParam()[0]);
//            processCancerStudy.setDataCategoryId(dataCategory.getId());
//            processCancerStudy.setAbsolutePath(codeMsg.getParam()[1]);
//            cancerStudyService.saveCancerStudy(processCancerStudy,user);
//            task.setIsSuccess(true);
//        }
//        task.setIsSuccess(false);
//        BeanUtils.copyProperties(codeMsg,task);
//        task.setTaskStatus(TaskStatus.FINISH);
//        taskRepository.save(task);
//        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//        springWebSocketHandler.sendMessageToUsers(new TextMessage(Thread.currentThread().getName()+": end! <<<<<<<<<<<<<<"));
//    }
//
//    private CodeMsg rCall(Code code, Map<String,String> maps){
//        CodeMsg codeMsg = new CodeMsg();
//        RCaller rcaller = RCaller.create();
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        rcaller.redirectROutputToStream(byteArrayOutputStream);
//        RCode rcode = RCode.create();
//        rcaller.setRCode(rcode);
//        for (String key: maps.keySet()){
//            rcode.addString(key,maps.get(key));
//        }
//
//        rcode.R_source(code.getAbsolutePath());
//
//        rcode.addRCode("res <- main()");
//
//        try {
//            rcaller.runAndReturnResultOnline("res");
//
//            /**
//             * c("DEG","/home/wangyang/Public/bioinfo/LiverCancerM6A/result/miRNA_deg.tsv")
//             * return workflow and absolute path
//             */
//            String[] param = rcaller.getParser().getAsStringArray("res");
//            codeMsg.setParam(param);
//            codeMsg.setRunMsg(byteArrayOutputStream.toString());
//            //TODO test
//            System.out.println(byteArrayOutputStream);
//            TextMessage textMessage = new TextMessage(byteArrayOutputStream.toByteArray());
//            springWebSocketHandler.sendMessageToUsers(textMessage);
//            return codeMsg;
//        } catch (Exception e) {
//            codeMsg.setRunMsg(byteArrayOutputStream.toString());
//            codeMsg.setException(e.getMessage());
//            springWebSocketHandler.sendMessageToUsers(new TextMessage(byteArrayOutputStream.toByteArray()));
//            springWebSocketHandler.sendMessageToUsers(new TextMessage(e.getMessage()));
//            e.printStackTrace();
//            return codeMsg;
//        }finally {
//            rcaller.stopRCallerOnline();
//        }
//    }
//
//
//
//    @Override
//    public void rCodePlot(Integer id, Map<String,String> maps){
//        Code code = findById(id);
//        String absolutePath = code.getAbsolutePath();
//        RConnection connection=null;
//
//        try {
//            connection = new RConnection();
////            OOBMessage oobMessage = new OOBMessage(springWebSocketHandler);
////            connection.setOOB(oobMessage);
////            REXP cap = connection.capabilities();
////            if (cap == null) {
////                System.err.println("ERROR: Rserve is not running in OCAP mode");
////                return;
////            }
////            connection.eval("library(httpgd)");
////            connection.eval("library(ggplot2)");
////            for (String key: maps.keySet()){
////                connection.assign(key,maps.get(key));
////            }
////            REXP eval = connection.eval("hgd_inline({plot.new(); source(\"" +  absolutePath+ "\") })");
////            String s = eval.asString();
//
//            String testCode = "{message('Hello!'); print(R.version.string) }";
////            RList occ = new RList(new REXP[] { cap, new REXPString(testCode) });
////            REXP res = connection.callOCAP(new REXPLanguage(occ));
//
////            final String s = res.asString();
////            springWebSocketHandler.sendMessageToUsers(new TextMessage(s));
//        } catch (RserveException  e) {
//            e.printStackTrace();
//            throw new BioinfoException(e.getMessage());
//        } catch (REngineException e) {
//            e.printStackTrace();
//            throw new BioinfoException(e.getMessage());
//        } finally {
//            if(connection!=null){
//                connection.close();
//            }
//        }
//    }

    @Override
    public List<Code> initData(String filePath) {
        return super.initData(filePath, CodeParam.class);
    }
}

