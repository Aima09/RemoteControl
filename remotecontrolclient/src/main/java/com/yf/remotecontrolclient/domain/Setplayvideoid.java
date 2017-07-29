package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class Setplayvideoid implements Serializable {
    private String cmd;
    private int videoid;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getVideoid() {
        return videoid;
    }

    public void setVideoid(int videoid) {
        this.videoid = videoid;
    }

    @Override
    public String toString() {
        return "Setplayvideoid [cmd=" + cmd + ", videoid=" + videoid + "]";
    }
}
