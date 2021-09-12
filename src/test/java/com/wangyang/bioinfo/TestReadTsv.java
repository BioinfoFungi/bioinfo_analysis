package com.wangyang.bioinfo;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import com.wangyang.bioinfo.txt.IDataInputService;
import com.wangyang.bioinfo.pojo.entity.MRNA;
import com.wangyang.bioinfo.pojo.support.Annotation;
import com.wangyang.bioinfo.service.IMRNAService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/8
 * https://github.com/uniVocity/univocity-parsers/blob/master/src/test/java/com/univocity/parsers/examples/samples/CsvSearchExample.java
 */
//@SpringBootTest
public class TestReadTsv {
//    @Autowired
    IDataInputService dataInputService;

//    @Autowired
    IMRNAService imrnaService;

//    @Test
    public void test1() throws FileNotFoundException {
        TsvParserSettings settings = new TsvParserSettings();
//        settings.selectIndexes(0);
//        RowListProcessor rowProcessor = new RowListProcessor();
        RowListProcessor rowProcessor = new RowListProcessor() {
            @Override
            public void rowProcessed(String[] row, ParsingContext context) {
//                System.out.println(row);
                if (row[6].equals("protein_coding")) {
                    super.rowProcessed(row, context);
                }
            }
        };
        settings.setProcessor(rowProcessor);
        settings.setHeaderExtractionEnabled(true);

        TsvParser parser = new TsvParser(settings);
///home/wy/Downloads/TCGA_ACC_Counts.tsv
        FileInputStream inputStream = new FileInputStream("/home/wy/Downloads/gencode.gene.info.v22.tsv");

//        List<String[]> allRows = parser.parseAll(inputStream);
        parser.parse(inputStream);
//        String[] headers = rowProcessor.getHeaders();
        List<String[]> rows = rowProcessor.getRows();

//        String[] strings = parser.parseLine("1");
        System.out.println();

    }

//    @Test
    public void test2() throws FileNotFoundException {
        BeanListProcessor<MRNA> rowProcessor = new BeanListProcessor<MRNA>(MRNA.class){
            @Override
            public void beanProcessed(MRNA bean, ParsingContext context) {
                if(bean.getGeneType().equals("protein_coding")){
                    super.beanProcessed(bean, context);
                }
            }
        };

        TsvParserSettings settings = new TsvParserSettings();
        settings.setProcessor(rowProcessor);
        settings.setHeaderExtractionEnabled(true);
        TsvParser parser = new TsvParser(settings);
        FileInputStream inputStream = new FileInputStream("/home/wy/Downloads/gencode.gene.info.v22.tsv");
        parser.parse(inputStream);
        List<MRNA> beans = rowProcessor.getBeans();

        System.out.println();
    }

//    @Test
    public void test3(){
        List<Annotation> annotations = dataInputService.readGeneData("/home/wy/Downloads/gencode.gene.info.v22.tsv","protein_coding");
        System.out.println();
    }

//    @Test
    public void test4(){
//        imrnaService.initData("/home/wy/Downloads/gencode.gene.info.v22.tsv");
    }
}

