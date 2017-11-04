package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class ImageFolder implements Serializable {
    private String cmd;
    private Integer id;
    private String name;
    private String folderNumber;
    private String b;//存放第一张图片的内容地方
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolderNumber() {
        return folderNumber;
    }

    public void setFolderNumber(String folderNumber) {
        this.folderNumber = folderNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
    //没有b
    @Override public String toString() {
        return "ImageFolder{" +
                "cmd='" + cmd + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", folderNumber='" + folderNumber + '\'' +
                '}';
    }
}
