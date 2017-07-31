package com.yf.minalibrary.message;

public class TextMessage extends BaseMessage {

    private TextBean textBean;

    public TextMessage(String senderId, String receiverId, String messageType, TextBean textBean) {
        super(senderId, receiverId, messageType);
        this.textBean = textBean;
    }

    public TextBean getTextBean() {
        return textBean;
    }

    public void setTextBean(TextBean textBean) {
        this.textBean = textBean;
    }

    @Override public String toString() {
        return "TextMessage{" +
                "senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", messageType='" + messageType + '\'' +
                ", time='" + time + '\'' +
                ", textBean=" + textBean +
                '}';
    }

    public static class TextBean {
        private String textContent;

        public TextBean(String textContent) {
            this.textContent = textContent;
        }

        public String getTextContent() {
            return textContent;
        }

        public void setTextContent(String textContent) {
            this.textContent = textContent;
        }
    }

}
