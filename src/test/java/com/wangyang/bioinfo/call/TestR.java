package com.wangyang.bioinfo.call;

import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class TestR {

    @Test
    public void test() throws REngineException, REXPMismatchException {
        RConnection c = new RConnection();
        c.eval("library(httpgd)");
        c.eval("library(maftools)");
//        c.eval("laml.maf = system.file('extdata', 'tcga_laml.maf.gz', package = 'maftools')");
//        c.eval("laml.clin = system.file('extdata', 'tcga_laml_annot.tsv', package = 'maftools') ");
        c.parseAndEval("laml = read.maf(maf = '/home/wangyang/R/x86_64-pc-linux-gnu-library/4.1/maftools/extdata/tcga_laml.maf.gz')");

        String string = c.eval("hgd_inline({plot.new();oncoplot(maf = laml)})").asString();
        System.out.printf("");
    }
}
