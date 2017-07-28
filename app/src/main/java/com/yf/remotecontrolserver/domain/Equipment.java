package com.yf.remotecontrolserver.domain;

public class Equipment {
    private String cmd;
    private String devid;
    private String ip;

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "Equipment [cmd=" + cmd + ", devid=" + devid + ", ip=" + ip
                + "]";
    }

}
