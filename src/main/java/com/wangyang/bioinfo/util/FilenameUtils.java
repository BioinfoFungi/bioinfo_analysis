package com.wangyang.bioinfo.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Calendar;

/**
 * @author wangyang
 * @date 2021/6/21
 */
public class FilenameUtils {
    public static String getExtension(@NonNull String filename) {
        Assert.hasText(filename, "Filename must not be blank");

        // Find the last slash
        int separatorLastIndex = StringUtils.lastIndexOf(filename, File.separatorChar);

        if (separatorLastIndex == filename.length() - 1) {
            return StringUtils.EMPTY;
        }

        if (separatorLastIndex >= 0 && separatorLastIndex < filename.length() - 1) {
            filename = filename.substring(separatorLastIndex + 1);
        }

        // Find last dot
        int dotLastIndex = StringUtils.lastOrdinalIndexOf(filename,".",2);
        if(dotLastIndex==-1){
            dotLastIndex = StringUtils.lastOrdinalIndexOf(filename,".",1);
        }
        if (dotLastIndex < 0) {
            return StringUtils.EMPTY;
        }

        return filename.substring(dotLastIndex + 1);
    }
    public static String getBasename(@NonNull String filename) {
        Assert.hasText(filename, "Filename must not be blank");

        // Find the last slash
        int separatorLastIndex = StringUtils.lastIndexOf(filename, File.separatorChar);

        if (separatorLastIndex == filename.length() - 1) {
            return StringUtils.EMPTY;
        }

        if (separatorLastIndex >= 0 && separatorLastIndex < filename.length() - 1) {
            filename = filename.substring(separatorLastIndex + 1);
        }

        // Find last dot
        int dotLastIndex = StringUtils.lastIndexOf(filename, '.');

        if (dotLastIndex < 0) {
            return filename;
        }

        return filename.substring(0, dotLastIndex);
    }

    public static String relativePath(@NonNull String relativePath) {
        Assert.hasText(relativePath, "relativePath must not be blank");
        String workDir = CacheStore.getValue("workDir");
        if(relativePath.contains(workDir)){
            return relativePath.replace(workDir+"/","");
        }
        int separatorLastIndex = StringUtils.lastIndexOf(relativePath, File.separatorChar);

        if (separatorLastIndex == relativePath.length() - 1) {
            return StringUtils.EMPTY;
        }

        if (separatorLastIndex >= 0 && separatorLastIndex < relativePath.length() - 1) {
            relativePath = relativePath.substring(separatorLastIndex + 1);
        }

        return relativePath;
    }
    public static String randomName(){
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month =date.get(Calendar.MONTH);
        int second = date.get(Calendar.SECOND);
        int day = date.get(Calendar.DAY_OF_MONTH);
        int millisecond = date.get(Calendar.MILLISECOND);
        String viewName = String.valueOf(year)+String.valueOf(month)+String.valueOf(day)+String.valueOf(second)+String.valueOf(millisecond);
        return viewName;
    }
}
