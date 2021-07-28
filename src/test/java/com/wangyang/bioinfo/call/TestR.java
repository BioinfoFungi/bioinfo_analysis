package com.wangyang.bioinfo.call;

import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.util.ObjectToMap;
import org.junit.Test;
import org.rosuda.JRI.RConsoleOutputStream;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import java.util.Map;

public class TestR {

    @Test
    public void test() throws REngineException, REXPMismatchException {
        RConnection c = new RConnection();
        c.eval("library(httpgd)");
        c.eval("library(maftools)");


        REngineStdOutput engineStdOutput =  new REngineStdOutput();
        engineStdOutput.RShowMessage(c,"456465465");
//        c.eval("laml.maf = system.file('extdata', 'tcga_laml.maf.gz', package = 'maftools')");
//        c.eval("laml.clin = system.file('extdata', 'tcga_laml_annot.tsv', package = 'maftools') ");
        c.parseAndEval("laml = read.maf(maf = '/home/wangyang/R/x86_64-pc-linux-gnu-library/4.1/maftools/extdata/tcga_laml.maf.gz')");

        String string = c.eval("hgd_inline({plot.new();oncoplot(maf = laml)})").asString();
        System.out.printf("");
    }

    @Test
    public void test1(){
        CancerStudy cancerStudy = new CancerStudy();
        cancerStudy.setStudyId(2);
        cancerStudy.setDescription("sddd");
        Map<String, String> stringObjectMap = ObjectToMap.setConditionMap(cancerStudy);
        System.out.printf("");

    }
}
