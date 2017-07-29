package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class OpenBrower implements Serializable {
    private String cmd;
    private String url;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
