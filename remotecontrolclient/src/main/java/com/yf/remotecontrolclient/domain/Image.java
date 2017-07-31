package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class Image implements Serializable {
    private Integer id;
    private String name;
    private String folderName;

    //缩略图片内容
    private String b;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
}
