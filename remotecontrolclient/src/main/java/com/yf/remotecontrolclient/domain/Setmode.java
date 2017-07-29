package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class Setmode implements Serializable {
    private String cmd;
    private int mode;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
