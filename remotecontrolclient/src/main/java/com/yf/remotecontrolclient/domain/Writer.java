package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class Writer implements Serializable {
    private String cmd;
    private String data;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
