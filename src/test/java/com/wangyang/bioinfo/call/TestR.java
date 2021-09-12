package com.wangyang.bioinfo.call;

import com.wangyang.bioinfo.pojo.entity.CancerStudy;
import com.wangyang.bioinfo.util.ObjectToCollection;
import org.junit.Test;
import org.rosuda.REngine.*;
//import org.rosuda.REngine.Rserve.OOBInterface;

import java.util.Map;
class TestOOB {//implements OOBInterface {
    /* OOB API is list(source, [context,] output)
       where context is present if io.use.context is set */
    public void oobSend(int code, REXP message) {
        try {
            RList l = message.asList();
            String src = l.at(0).asString();
            // we only implement the following OOB masseges:
            if (src.equals("console.out") ||
                    src.equals("console.err") ||
                    src.equals("console.msg") ||
                    src.equals("stdout") ||
                    src.equals("stderr")) {
                String txt = l.at((l.size() > 2) ? 2 : 1).asString();
                if (src.equals("console.out") || src.equals("stdout"))
                    System.out.print(txt);
                else
                    System.err.print(txt);
            }
        } catch (REXPMismatchException ex) {
            /* if we can't parse it, we ignore it */
        }
    }

    public REXP oobMessage(int code, REXP message) {
        /* we don't implement stdin for now, but it
           would look something like the above */
        return new REXPString("no!\n");
    }
}

public class TestR {

    @Test
    public void test() throws REngineException, REXPMismatchException {

//        RConnection c = new RConnection();
//        // add the OOB handler
//        c.setOOB(new TestOOB());
//        REXP cap = c.capabilities();
//        if (cap == null) {
//            System.err.println("ERROR: Rserve is not running in OCAP mode");
//            return;
//        }
//        // we assume the capability is a single function that we want to call
//        String testCode = "{ message('Hello!'); print(R.version.string) }";
//        RList occ = new RList(new REXP[] { cap, new REXPString(testCode) });
//        REXP res = c.callOCAP(new REXPLanguage(occ));
////        c.eval("library(httpgd)");
////        c.eval("library(ggplot2)");
//        System.out.println("result: " + res);
////        c.eval("library(httpgd)");
////        c.eval("library(maftools)");
////
////
////        REngineStdOutput engineStdOutput =  new REngineStdOutput();
////        engineStdOutput.RShowMessage(c,"456465465");
//////        c.eval("laml.maf = system.file('extdata', 'tcga_laml.maf.gz', package = 'maftools')");
//////        c.eval("laml.clin = system.file('extdata', 'tcga_laml_annot.tsv', package = 'maftools') ");
////        c.parseAndEval("laml = read.maf(maf = '/home/wangyang/R/x86_64-pc-linux-gnu-library/4.1/maftools/extdata/tcga_laml.maf.gz')");
////
////        String string = c.eval("hgd_inline({plot.new();oncoplot(maf = laml)})").asString();
//        System.out.printf("");
    }

    @Test
    public void test1(){
        CancerStudy cancerStudy = new CancerStudy();
        cancerStudy.setStudyId(2);
        cancerStudy.setDescription("sddd");
        Map<String, String> stringObjectMap = ObjectToCollection.setConditionMap(cancerStudy);
        System.out.printf("");

    }
}
