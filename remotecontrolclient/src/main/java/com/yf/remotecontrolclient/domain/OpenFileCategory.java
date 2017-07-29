package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class OpenFileCategory implements Serializable {
    private String cmd;
    private Integer fileCategoryId;

    public Integer getFileCategoryId() {
        return fileCategoryId;
    }

    public void setFileCategoryId(Integer fileCategoryId) {
        this.fileCategoryId = fileCategoryId;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

}
