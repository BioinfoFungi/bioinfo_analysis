package com.wangyang.bioinfo.txt;

import com.wangyang.bioinfo.pojo.support.Annotation;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/9
 */
public interface IDataInputService {
    List<Annotation> readGeneData(String filePath, String geneType);
}
