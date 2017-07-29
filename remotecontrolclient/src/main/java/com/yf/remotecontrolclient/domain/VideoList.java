package com.yf.remotecontrolclient.domain;

import java.io.Serializable;
import java.util.List;

public class VideoList implements Serializable {
    private String cmd;
    private int pageSize;//多少条数据
    private int pageIndex;//从索引几开始
    private int total;//共多少视频
    private List<Video> VideoList;

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

    public List<Video> getVideoList() {
        return VideoList;
    }

    public void setVideoList(List<Video> videoList) {
        VideoList = videoList;
    }

    @Override
    public String toString() {
        return "VideoList [cmd=" + cmd + ", pageSize=" + pageSize
                + ", pageIndex=" + pageIndex + ", total=" + total
                + ", VideoList=" + VideoList + "]";
    }

}
