package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class Setplaystatus implements Serializable {
    private String cmd;
    private String status;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
