package com.yf.minalibrary.message;

public class CmdMessage extends BaseMessage {

    private CmdBean cmdBean;

    public CmdMessage(String messageType, CmdBean cmdBean) {
        super(messageType);
        this.cmdBean = cmdBean;
    }

    public CmdMessage(String messageType) {
        super(messageType);
    }

    public CmdBean getCmdBean() {
        return cmdBean;
    }

    public void setCmdBean(CmdBean cmdBean) {
        this.cmdBean = cmdBean;
    }

    @Override public String toString() {
        return "CmdMessage{" +
                "cmdBean=" + cmdBean +
                '}';
    }

    /**
     * Created by wuhuai on 2016/11/11 .
     * ;
     */

    public static class CmdBean {
        private String senderId;         // 信息发送端
        private String receiverId;       // 接收端ID号
        private String cmdType ;
        private String cmdContent;

        public CmdBean(String cmdType, String cmdContent) {
            this.cmdType = cmdType;
            this.cmdContent = cmdContent;
        }

        public CmdBean(String senderId, String receiverId, String cmdType, String cmdContent) {
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.cmdType = cmdType;
            this.cmdContent = cmdContent;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public String getCmdType() {
            return cmdType;
        }

        public void setCmdType(String cmdType) {
            this.cmdType = cmdType;
        }

        public String getCmdContent() {
            return cmdContent;
        }

        public void setCmdContent(String cmdContent) {
            this.cmdContent = cmdContent;
        }


        @Override public String toString() {
            return "CmdBean{" +
                    "senderId='" + senderId + '\'' +
                    ", receiverId='" + receiverId + '\'' +
                    ", cmdType='" + cmdType + '\'' +
                    ", cmdContent='" + cmdContent + '\'' +
                    '}';
        }
    }
}
