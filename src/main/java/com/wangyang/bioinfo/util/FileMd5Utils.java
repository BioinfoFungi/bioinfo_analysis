package com.wangyang.bioinfo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wangyang
 * @date 2021/7/18
 */
public class FileMd5Utils {
    protected static char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    protected static MessageDigest messageDigest = null;
    static{
        try{
            messageDigest = MessageDigest.getInstance("MD5");
        }catch (NoSuchAlgorithmException e) {
            System.err.println(FileMd5Utils.class.getName()+"初始化失败，MessageDigest不支持MD5Util.");
            e.printStackTrace();
        }
    }
    /**
     * 计算文件的MD5
     * @param fileName 文件的绝对路径
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(String fileName) throws IOException{
        File f = new File(fileName);
        return getFileMD5String(f);
    }

    /**
     * 计算文件的MD5，重载方法
     * @param file 文件对象
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(File file) {
        try {
            FileInputStream in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            messageDigest.update(byteBuffer);
            return bufferToHex(messageDigest.digest());
        } catch (IOException e) {
            e.printStackTrace();
            throw new BioinfoException(e.getMessage());
        }
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

//    public static void main(String[] args) throws IOException {
//        String fileName = "/home/wy/Documents/bioinfoR/data/TCGA_ESCA_clinical.tsv";
////        String str = "d0970714757783e6cf17b26fb8e2298f";
////        String str2 = "ea3ed20b6b101a09085ef09c97da1597";
////        System.out.println(str.equals(str2));
//        System.out.println(getFileMD5String(fileName));
//    }
}
