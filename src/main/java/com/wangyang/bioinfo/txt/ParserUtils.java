package com.wangyang.bioinfo.txt;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import com.wangyang.bioinfo.pojo.txt.Annotation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/10
 */
public class ParserUtils {

    public static List<Annotation> getAnnotationParser(String geneType, String filePath){
        try {
            try(FileInputStream inputStream = new FileInputStream(filePath);) {
                return getAnnotationParser(geneType,inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Annotation> getAnnotationParser(String geneType, InputStream inputStream){
        AnnotationTsvParser annotationTsvParser = new AnnotationTsvParser(geneType);
        TsvParserSettings settings = new TsvParserSettings();
        settings.setProcessor(annotationTsvParser);
        settings.setHeaderExtractionEnabled(true);
        TsvParser parser = new TsvParser(settings);
        parser.parse(inputStream);
        List<Annotation> beans = annotationTsvParser.getBeans();
        return beans;
    }
}
