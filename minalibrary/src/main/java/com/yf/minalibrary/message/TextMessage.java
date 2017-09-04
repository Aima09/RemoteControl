package com.yf.minalibrary.message;

public class TextMessage extends BaseMessage {
    String textContent;
    public TextMessage(String senderId, String receiverId, int messageType, String textContent) {
        super(senderId, receiverId, messageType);
        this.textContent = textContent;
    }
}
