package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class Setvolume implements Serializable {
    private String cmd;
    private int volume;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

}
