package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class OpenImage implements Serializable {
    private String cmd;
    private String imageFileName;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
