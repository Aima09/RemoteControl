package com.yf.minalibrary.message;

import com.yf.minalibrary.common.CmdType;
import com.yf.minalibrary.common.DeviceType;

public class CmdMessage extends BaseMessage {

    private CmdBean cmdBean;

    public CmdMessage(String senderId, String receiverId, String messageType, CmdBean cmdBean) {
        super(senderId, receiverId, messageType);
        this.cmdBean = cmdBean;
    }

    public CmdMessage(String messageType, CmdBean cmdBean) {
        super("", "", messageType);
        this.cmdBean = cmdBean;
    }

    public CmdBean getCmdBean() {
        return cmdBean;
    }

    public void setCmdBean(CmdBean cmdBean) {
        this.cmdBean = cmdBean;
    }

    @Override public String toString() {
        return "CmdMessage{" +
                "senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", messageType='" + messageType + '\'' +
                ", time='" + time + '\'' +
                ", cmdBean=" + cmdBean +
                '}';
    }

    /**
     * Created by wuhuai on 2016/11/11 .
     * ;
     */

    public static class CmdBean {
        private String cmdType;
        private String deviceType;
        private String cmdContent;

        public CmdBean(String cmdType, String deviceType, String cmdContent) {
            this.cmdType = cmdType == null ? CmdType.CMD_INVALID : cmdType;
            this.deviceType = deviceType == null ? DeviceType.DEVICE_TYPE_INVALID : deviceType;
            this.cmdContent = cmdContent;
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

        @Override public String toString() {
            return "CmdBean{" +
                    ", cmdType='" + cmdType + '\'' +
                    ", deviceType='" + deviceType + '\'' +
                    ", cmdContent='" + cmdContent + '\'' +
                    '}';
        }
    }
}
