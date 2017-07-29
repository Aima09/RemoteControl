package com.yf.remotecontrolclient.domain;

import java.io.Serializable;
import java.util.List;

public class FileList implements Serializable {
    private String cmd;
    private int pageSize;//多少条数据
    private int pageIndex;//从索引几开始
    private int total;//共多少文件或文件夹
    private List<File> fileList;
    private String currentPath;//显示在标题中

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    @Override
    public String toString() {
        return "FileList [cmd=" + cmd + ", pageSize=" + pageSize
                + ", pageIndex=" + pageIndex + ", total=" + total
                + ", fileList=" + fileList + ", currentPath=" + currentPath
                + "]";
    }

}