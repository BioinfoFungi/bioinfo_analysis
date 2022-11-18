package com.wangyang.bioinfo.util;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class File2Tsv {
    public static  <DOMAIN> List<DOMAIN> tsvToBean(Class<DOMAIN> clz,String filePath){
        try {
            try(FileInputStream inputStream = new FileInputStream(filePath)){
                BeanListProcessor<DOMAIN> beanListProcessor = new BeanListProcessor<>(clz);
                TsvParserSettings settings = new TsvParserSettings();
                settings.setProcessor(beanListProcessor);
                settings.setHeaderExtractionEnabled(true);
                TsvParser parser = new TsvParser(settings);
                parser.parse(inputStream);
                List<DOMAIN> beans = beanListProcessor.getBeans();
                inputStream.close();
                return beans;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static  <DOMAIN> List<DOMAIN> tsvToBeanSelect(Class<DOMAIN> clz,String filePath){
        try {
            try(FileInputStream inputStream = new FileInputStream(filePath)){
                BeanListProcessor<DOMAIN> beanListProcessor = new BeanListProcessor<>(clz);
                TsvParserSettings settings = new TsvParserSettings();
//                settings.selectFields("sample","group");
                settings.setProcessor(beanListProcessor);
                settings.setHeaderExtractionEnabled(true);
                TsvParser parser = new TsvParser(settings);
                parser.parse(inputStream);
                List<DOMAIN> beans = beanListProcessor.getBeans();
                inputStream.close();
                return beans;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static  List<String[]>  tsvToList(String filePath){
        try {
            try(FileInputStream inputStream = new FileInputStream(filePath)){
                TsvParserSettings settings = new TsvParserSettings();
                TsvParser parser = new TsvParser(settings);
                List<String[]> list = parser.parseAll(inputStream);
                return list;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
