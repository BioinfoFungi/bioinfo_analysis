package com.wangyang.bioinfo.tsv;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.ColumnProcessor;
import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
//import joinery.DataFrame;

//import joinery.DataFrame;
//import org.junit.jupiter.api.Test;
//import tech.tablesaw.api.DateColumn;
//import tech.tablesaw.api.StringColumn;
//import tech.tablesaw.api.Table;
//import tech.tablesaw.columns.Column;
//import tech.tablesaw.io.csv.CsvReadOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/7/11
 */
public class Joinery {
//    @Test
//    public void test1() throws IOException {
//
//        long startTime = System.currentTimeMillis();
//        FileInputStream fileInputStream = new FileInputStream("/home/wy/Downloads/TCGA_ACC_Counts.tsv");
//        DataFrame<Object> lists = DataFrame.readCsv(fileInputStream,"\\t");
//        DataFrame<Object> transpose = lists.transpose();
//        Set<Object> columns = transpose.columns();
//        Object o = transpose.get(1, 1);
//        long endTime = System.currentTimeMillis();
//        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
//        System.out.println();
//    }
////    @Test
//    public void test2() throws IOException {
//        long startTime = System.currentTimeMillis();
//        CsvReadOptions.Builder builder = CsvReadOptions.builder("/home/wy/Downloads/TCGA_ACC_Counts.tsv")
//                .separator('\t');
//        Table t = Table.read().usingOptions(builder);
////        Table transpose = t.transpose(true,true);
//
//
//        long endTime = System.currentTimeMillis();
//        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
//        System.out.println();
//    }
////    @Test
//    public void test2_1() throws IOException {
//        long startTime = System.currentTimeMillis();
//        CsvReadOptions.Builder builder = CsvReadOptions.builder("/home/wy/Downloads/TCGA_ACC_Counts.tsv")
//                .separator('\t');
//        Table t = Table.read().usingOptions(builder);
////        Table transpose = t.transpose(true,true);
//
////        bd.map();
//
//
//        Table table = t.where(t.textColumn(0).isEqualTo("ENSG00000000003.13"));
//        Table transpose = table.transpose(true,true);
//        long endTime = System.currentTimeMillis();
//        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
//        System.out.println();
//    }
////    @Test
//    public void test3() throws IOException {
//        long startTime = System.currentTimeMillis();
//        TsvParserSettings settings = new TsvParserSettings();
//        settings.setMaxColumns(10000);
//        TsvParser parser = new TsvParser(settings);
//        FileInputStream inputStream = new FileInputStream("/home/wy/Downloads/TCGA_COAD_Counts.tsv");
//        List<String[]> allRows = parser.parseAll(inputStream);
//        long endTime = System.currentTimeMillis();
//        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
//        System.out.println();
//    }
////    @Test
//    public void test3_2() throws IOException {
//        long startTime = System.currentTimeMillis();
//        TsvParserSettings settings = new TsvParserSettings();
//        settings.setMaxColumns(100000000);
//        ColumnProcessor rowProcessor = new ColumnProcessor(){
//            @Override
//            public void rowProcessed(String[] row, ParsingContext context) {
//                if (row[0].equals("ENSG00000000003.13")) {
//                    super.rowProcessed(row, context);
//
//                }
//            }
//        };
//        settings.setProcessor(rowProcessor);
//        settings.setHeaderExtractionEnabled(true);
//        TsvParser parser = new TsvParser(settings);
//        FileInputStream inputStream = new FileInputStream("/home/wy/Downloads/TCGA_COAD_Counts.tsv");
//        parser.parse(inputStream);
////        if(true) {
////            List<List<String>> values = rowProcessor.getColumnValuesAsList();
////            String[] headers = rowProcessor.getHeaders();
////            DataFrame<String> df = new DataFrame<>(values);
////            String s = df.get(0, 0);
////            DataFrame<String> transpose = df.transpose();
////        }
////            String yp = transpose.get(0, 0);
////            List<String> row = transpose.row(1);
////            List<String> col = transpose.col(0);
////            List<String> list = Arrays.asList(headers).stream().map(item -> {
////                if(item.equals("X1")){
////                    return "X1";
////                }
////                String s1 = item.substring(13, 15);
////                if (s1.equals(11)) {
////                    return "Normal";
////                } else {
////                    return "Tumor";
////                }
////            }).collect(Collectors.toList());
////            System.out.println();
////        }else {
////            Map<String, List<String>> columnValues = new TreeMap<String, List<String>>(rowProcessor.getColumnValuesAsMapOfNames());
////
////        }
//
//        long endTime = System.currentTimeMillis();
//        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
//        System.out.println();
//    }
////    @Test
//    public void test3_1() throws IOException {
//        long startTime = System.currentTimeMillis();
//        TsvParserSettings settings = new TsvParserSettings();
//        RowListProcessor rowProcessor = new RowListProcessor() {
//            @Override
//            public void rowProcessed(String[] row, ParsingContext context) {
////                System.out.println(row);
//                if (row[0].equals("ENSG00000000003.13")) {
//                    super.rowProcessed(row, context);
//                }
//            }
//        };
//        settings.setProcessor(rowProcessor);
//        settings.setHeaderExtractionEnabled(true);
//        TsvParser parser = new TsvParser(settings);
//        FileInputStream inputStream = new FileInputStream("/home/wy/Downloads/TCGA_ACC_Counts.tsv");
//        parser.parse(inputStream);
//        List<String[]> rows = rowProcessor.getRows();
//
//        long endTime = System.currentTimeMillis();
//        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
//        System.out.println();
//    }
//
////    @Test
//    public void test3_3() throws IOException {
//        long startTime = System.currentTimeMillis();
//        TsvParserSettings settings = new TsvParserSettings();
//        RowListProcessor rowProcessor = new RowListProcessor();
//        ConcurrentRowProcessor processor = new ConcurrentRowProcessor(rowProcessor);
//        settings.setProcessor(processor);
//        settings.setHeaderExtractionEnabled(true);
//        TsvParser parser = new TsvParser(settings);
//        FileInputStream inputStream = new FileInputStream("/home/wy/Downloads/TCGA_ACC_Counts.tsv");
//        parser.parse(inputStream);
//        List<String[]> rows = rowProcessor.getRows();
//        long endTime = System.currentTimeMillis();
//        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
//        System.out.println();
//    }
////    @Test
////    public void test4() throws IOException {
////        long startTime = System.currentTimeMillis();
////        File file = new File("/home/wy/Downloads/TCGA_ACC_Counts.tsv");
////        DataFrame users = DataFrame.fromCSV(file, '\t', true);
////        long endTime = System.currentTimeMillis();
////        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
////        System.out.println();
////    }
////    @Test
////    public void test4_1() throws IOException {
////        long startTime = System.currentTimeMillis();
////        File file = new File("/home/wy/Downloads/TCGA_ACC_Counts.tsv");
////        DataFrame users = DataFrame.fromCSV(file, '\t', true);
////        users.getStringColumn("X1").map((value -> value + "_idx_example"));
////
////        long endTime = System.currentTimeMillis();
////        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
////        System.out.println();
////    }
}
