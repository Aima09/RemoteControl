package com.yf.minalibrary.message;

/**
 * Created by wuhuai on 2017/7/28 .
 * .
 */

public class IntercomMessage extends BaseMessage {

    private IntercomBean intercomBean;

    public IntercomMessage(String messageType) {
        super(messageType);
    }

    public IntercomMessage(String messageType, IntercomBean intercomBean) {
        super(messageType);
        this.intercomBean = intercomBean;
    }

    public IntercomBean getIntercomBean() {
        return intercomBean;
    }

    public void setIntercomBean(IntercomBean intercomBean) {
        this.intercomBean = intercomBean;
    }

    @Override public String toString() {
        return super.toString() + "," + intercomBean.toString();
    }

    public static class IntercomBean {
        private String senderId = "";         // 信息发送端
        private String receiverId = "";       // 接收端ID号
        private int intercomContentLength = 0;
        private byte[] intercomContent;

        public IntercomBean(String senderId, String receiverId, byte[] intercomContent) {
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.intercomContent = intercomContent;
            this.intercomContentLength = intercomContent.length;
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

        public int getIntercomContentLength() {
            return intercomContentLength;
        }

        public void setIntercomContentLength(int intercomContentLength) {
            this.intercomContentLength = intercomContentLength;
        }

        public byte[] getIntercomContent() {
            return intercomContent;
        }

        public void setIntercomContent(byte[] intercomContent) {
            this.intercomContent = intercomContent;
        }

        @Override public String toString() {
            return "senderId=" + senderId +",receiverId=" + receiverId +
                    ",intercomContentLength=" + intercomContentLength;
        }
    }

}
