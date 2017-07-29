package com.yf.remotecontrolclient.domain;

import java.io.Serializable;
import java.util.List;

public class ImageFolderList implements Serializable {
    private String cmd;
    private int pageSize;//多少条数据
    private int pageIndex;//从索引几开始
    private int total;//共多少个文件
    private List<ImageFolder> imageFolderList;

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

    public List<ImageFolder> getImageFolderList() {
        return imageFolderList;
    }

    public void setImageFolderList(List<ImageFolder> imageFolderList) {
        this.imageFolderList = imageFolderList;
    }

}
