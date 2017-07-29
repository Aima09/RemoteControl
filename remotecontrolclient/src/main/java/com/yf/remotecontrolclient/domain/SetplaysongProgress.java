package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class SetplaysongProgress implements Serializable {
    private String cmd;
    private int songprogress;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getSongprogress() {
        return songprogress;
    }

    public void setSongprogress(int songprogress) {
        this.songprogress = songprogress;
    }

}
