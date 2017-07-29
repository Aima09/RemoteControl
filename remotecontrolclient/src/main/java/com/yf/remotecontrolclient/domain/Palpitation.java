package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

/**
 * ������װ������
 *
 * @author sujuntao
 */
public class Palpitation implements Serializable {
    private String cmd;
    private String ip;

    public Palpitation() {
        super();
    }

    public Palpitation(String cmd, String ip) {
        super();
        this.cmd = cmd;
        this.ip = ip;
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
}
