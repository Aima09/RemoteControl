package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class FileCategory implements Serializable {
    private String cmd;
    //0音乐 1视频 2图片 3文挡  4压缩包
    private Integer fileType;
    private String title;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
