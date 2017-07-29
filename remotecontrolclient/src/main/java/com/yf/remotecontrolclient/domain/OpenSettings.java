package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class OpenSettings implements Serializable {
    private String cmd;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
