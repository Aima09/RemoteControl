package com.yf.minalibrary.message;

/**
 * Created by wuhuai on 2017/7/28 .
 * .
 */

public class IntercomMessage extends BaseMessage {

    private IntercomBean intercomBean;

    public IntercomMessage(String senderId, String receiverId, String messageType, IntercomBean intercomBean) {
        super(senderId, receiverId, messageType);
        this.intercomBean = intercomBean;
    }

    public IntercomBean getIntercomBean() {
        return intercomBean;
    }

    public void setIntercomBean(IntercomBean intercomBean) {
        this.intercomBean = intercomBean;
    }

    @Override public String toString() {
        return "IntercomMessage{" +
                "senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", messageType='" + messageType + '\'' +
                ", time='" + time + '\'' +
                ", intercomBean=" + intercomBean +
                '}';
    }

    public static class IntercomBean {

        private String intercomContent;

        public IntercomBean(String intercomContent) {
            this.intercomContent = intercomContent;
        }

        public String getIntercomContent() {
            return intercomContent;
        }

        public void setIntercomContent(String intercomContent) {
            this.intercomContent = intercomContent;
        }

        @Override public String toString() {
            return "IntercomBean{" +
                    "intercomContent='" + intercomContent + '\'' +
                    '}';
        }
    }

}
