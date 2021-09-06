package com.wangyang.bioinfo.pojo.support;

import lombok.Data;

import java.util.Comparator;
import java.util.List;

@Data
public class FileTree implements Comparator<FileTree> {
    private String name;

    private String path;

    private Boolean isFile;

    private Boolean editable;

    private List<FileTree> node;

    @Override
    public int compare(FileTree leftFile, FileTree rightFile) {
        if (leftFile.isFile && !rightFile.isFile) {
            return 1;
        }

        if (!leftFile.isFile && rightFile.isFile) {
            return -1;
        }

        return leftFile.getName().compareTo(rightFile.getName());
    }
}
