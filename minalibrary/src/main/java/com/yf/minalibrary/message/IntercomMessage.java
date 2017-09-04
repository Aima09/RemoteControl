package com.yf.minalibrary.message;

/**
 * Created by wuhuai on 2017/7/28 .
 * .
 */

public class IntercomMessage extends BaseMessage {

    private String intercomContent;

    public IntercomMessage(String senderId, String receiverId, int messageType, String intercomContent) {
        super(senderId, receiverId, messageType);
        this.intercomContent=intercomContent;
    }

    @Override public String toString() {
        return "IntercomMessage{" +
                "senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", messageType='" + messageType + '\'' +
                ", time='" + time + '\'' +
                ", intercomBean=" +
                '}';
    }

    public String getIntercomContent() {
        return intercomContent;
    }

    public void setIntercomContent(String intercomContent) {
        this.intercomContent = intercomContent;
    }
}
