package com.yf.minalibrary.message;

public class CmdMessage extends BaseMessage {
    private String cmdType;
    private String deviceType;
    private String cmdContent;


    public CmdMessage(String senderId, String receiverId, int messageType, String cmdType,String deviceType,String cmdContent) {
        super(senderId, receiverId, messageType);
        this.cmdType=cmdType;
        this.deviceType=deviceType;
        this.cmdContent=cmdContent;
    }

    @Override public String toString() {
        return "CmdMessage{" +
                "cmdType='" + cmdType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", cmdContent='" + cmdContent + '\'' +
                '}';
    }

    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getCmdContent() {
        return cmdContent;
    }

    public void setCmdContent(String cmdContent) {
        this.cmdContent = cmdContent;
    }
}
