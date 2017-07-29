package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class Setopenfileid implements Serializable {
    private String cmd;
    private Integer fileid;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Integer getFileid() {
        return fileid;
    }

    public void setFileid(Integer fileid) {
        this.fileid = fileid;
    }
}