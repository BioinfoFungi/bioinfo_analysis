package com.wangyang.bioinfo.txt;

import com.wangyang.bioinfo.pojo.support.Annotation;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/9
 */
@Service
public class DataInputServiceImpl implements IDataInputService {

    @Override
    public  List<Annotation> readGeneData(String filePath, String geneType){
        List<Annotation> annotationList = ParserUtils.getAnnotationParser(geneType, filePath);
        return annotationList;
    }
}
