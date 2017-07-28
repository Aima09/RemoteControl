package com.yf.remotecontrolserver.domain;

import java.io.Serializable;

/**
 * @author sujuntao
 */
public class Gateway implements Serializable {
    private String key;
    private String gwID;
    private String gwIp;
    private Integer gwPort;
    private Integer gwTcpPort;
    private Integer gwUdpPort;

    public Integer getGwUdpPort() {
        return gwUdpPort;
    }

    public void setGwUdpPort(Integer gwUdpPort) {
        this.gwUdpPort = gwUdpPort;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGwID() {
        return gwID;
    }

    public void setGwID(String gwID) {
        this.gwID = gwID;
    }

    public String getGwIp() {
        return gwIp;
    }

    public void setGwIp(String gwIp) {
        this.gwIp = gwIp;
    }

    public Integer getGwPort() {
        return gwPort;
    }

    public void setGwPort(Integer gwPort) {
        this.gwPort = gwPort;
    }

    public Integer getGwTcpPort() {
        return gwTcpPort;
    }

    public void setGwTcpPort(Integer gwTcpPort) {
        this.gwTcpPort = gwTcpPort;
    }

    @Override
    public String toString() {
        return "Gateway [key=" + key + ", gwID=" + gwID + ", gwIp=" + gwIp
                + ", gwPort=" + gwPort + ", gwTcpPort=" + gwTcpPort
                + ", gwUdpPort=" + gwUdpPort + "]";
    }

}