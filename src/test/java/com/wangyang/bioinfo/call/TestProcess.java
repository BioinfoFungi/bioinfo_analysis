package com.wangyang.bioinfo.call;

import com.github.rcaller.FunctionCall;
import com.github.rcaller.FunctionParameter;
import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

/**
 * @author wangyang
 * @date 2021/7/21
 */
public class TestProcess {
//    @Test
    public void test1() throws Exception {
        Process process = Runtime.getRuntime().exec("/usr/bin/R --vanilla ");
        OutputStream outputStream = process.getOutputStream();
        InputStream stream = process.getInputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        outputStreamWriter.write("33+44");
        outputStream.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(stream), 20480);
        String str;
        while ((str = in.readLine()) != null) {
            System.out.println(str);
        }

    }
//    @Test
    public void test2() throws IOException {
        RCaller rcaller = RCaller.create();


        RCode code = RCode.create();
        code.addRCode("b<-1:10");
        code.addRCode("m<-mean(b)");

        rcaller.runAndReturnResultOnline("m");

    }

//    @Test
    public void test3() throws IOException {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(" /usr/bin/R --vanilla");
            final InputStream is1 = process.getInputStream();
            final InputStream is2 = process.getErrorStream();
            OutputStream outputStream = process.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write("33+44");
            outputStream.flush();
//            process.waitFor();
            new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(is2), 20480);
                    String str;
                    while ((str = in.readLine()) != null) {

                            System.out.println(str);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(is1), 20480);
                    String str;
                    while ((str = in.readLine()) != null) {

                            System.out.println(str);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }

//    @Test
    public void test4() throws IOException {
        String line;
        Scanner scan = new Scanner(System.in);
        ProcessBuilder builder = new ProcessBuilder("/bin/bash");
        builder.redirectErrorStream(true);
        Process process = builder.start();
        OutputStream stdin = process.getOutputStream ();
        InputStream stderr = process.getErrorStream ();
        InputStream stdout = process.getInputStream ();

        BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

//        String input = scan.nextLine();
//        input += "\n";
//        writer.write(input);
//        writer.flush();
//
//        input = scan.nextLine();
//        input += "\n";
//        writer.write(input);
//        writer.flush();
        Thread T=new Thread(new Runnable() {

            @Override
            public void run() {
                while(true)
                {
                    String input = scan.nextLine();
                    input += "\n";
                    try {
                        writer.write(input);
                        writer.flush();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        } );
        T.start();
        while ((line = reader.readLine ()) != null) {
            System.out.println ("Stdout: " + line);
        }

//        while (scan.hasNext()) {
//            String input = scan.nextLine();
//            if (input.trim().equals("exit")) {
//                // Putting 'exit' amongst the echo --EOF--s below doesn't work.
//                writer.write("exit\n");
//            } else {
//                writer.write("((" + input + ") && echo --EOF--) || echo --EOF--\n");
//            }
//            writer.flush();
//
//            line = reader.readLine();
//            while (line != null && ! line.trim().equals("--EOF--")) {
//                System.out.println ("Stdout: " + line);
//                line = reader.readLine();
//            }
//            if (line == null) {
//                break;
//            }
//        }
    }
    private final static double delta = 1.0 / 1000.0;
//    @Test
    public void test6(){
        RCaller caller = RCaller.create();
        RCode code = RCode.create();

        code.addDoubleArray("x", new double[]{1, 2, 3, 4, 5});
        code.addDoubleArray("y", new double[]{2, 4, 6, 8, 10});
        code.addRCode("mydata <- as.data.frame(cbind(x,y))");

        FunctionCall fc = new FunctionCall();
        fc.setFunctionName("lm");
        fc.addParameter(new FunctionParameter("formula", "y~x", FunctionParameter.PARAM_OBJECT));
        fc.addParameter(new FunctionParameter("data", "mydata", FunctionParameter.PARAM_OBJECT));

        code.addFunctionCall("RegressResult", fc);
        caller.setRCode(code);

        caller.runAndReturnResult("RegressResult");
        double[] coefs = caller.getParser().getAsDoubleArray("coefficients");

    }
}
