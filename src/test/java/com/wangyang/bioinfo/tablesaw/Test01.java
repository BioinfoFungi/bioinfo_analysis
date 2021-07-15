package com.wangyang.bioinfo.tablesaw;

import org.junit.jupiter.api.Test;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.numbers.IntColumnType;
import tech.tablesaw.io.csv.CsvReadOptions;
import tech.tablesaw.io.json.JsonWriteOptions;
import tech.tablesaw.io.json.JsonWriter;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.BoxPlot;
import tech.tablesaw.plotly.api.Histogram;
import tech.tablesaw.plotly.components.Figure;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/11
 */
public class Test01 {

//    @Test
    public void test1() throws IOException {
//        Table t = Table.read().file("/home/wy/Downloads/gencode.gene.info.v22.tsv");
        CsvReadOptions.Builder builder = CsvReadOptions.builder("/home/wy/Downloads/gencode.gene.info.v22.tsv")
                .separator('\t');
        Table t = Table.read().usingOptions(builder);
        int rowCount = t.rowCount();
        int columnCount = t.columnCount();
//        t.addColumns("")
//        t.display()
        System.out.println();

    }
//    @Test
    public void test() throws IOException {
        Table property = Table.read().csv("/home/wy/Downloads/sacramento_real_estate_transactions.csv");
        IntColumn sqft = property.intColumn("sq__ft");
        IntColumn price = property.intColumn("price");

        sqft.set(sqft.isEqualTo(0), IntColumnType.missingValueIndicator());
        price.set(price.isEqualTo(0), IntColumnType.missingValueIndicator());

//        Plot.show(Histogram.create("Distribution of prices", property.numberColumn("price")));
        Figure figure = BoxPlot.create("Prices by property type", property, "type", "price");
        String javascript = figure.asJavascript("ss");
        Plot.show(figure);
//        property.where()
//        property.write().csv("filename.csv");
////        property.write().csv();
//        StringWriter writer = new StringWriter();

//        property
//                .write()
//                .usingOptions(JsonWriteOptions.builder(writer).asObjects(false).header(true).build());
        System.out.println();

    }
//    @Test
    public void test2() throws IOException {
        CsvReadOptions.Builder builder = CsvReadOptions.builder("/home/wy/Downloads/TCGA_ACC_Counts.tsv")
                .separator('\t');
        Table t = Table.read().usingOptions(builder);
        Table transpose = t.first(3).transpose(true, true);
        List<String> columnNames = transpose.columnNames();
//        Figure figure = BoxPlot.create("Prices by property type", transpose, columnNames.get(0), "price");
        Figure figure = Histogram.create("Distribution of prices", transpose.numberColumn(columnNames.get(0)));

        String javascript = figure.asJavascript("ss");
        Plot.show(figure);
        System.out.println();

    }
}
