package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class Setplaysongid implements Serializable {
    private String cmd;
    private int songid;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getSongid() {
        return songid;
    }

    public void setSongid(int songid) {
        this.songid = songid;
    }

}
