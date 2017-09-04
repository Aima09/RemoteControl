package com.yf.minalibrary.message;

import com.yf.minalibrary.common.MessageType;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseMessage implements Serializable {

    public String senderId = "";         // 信息发送端
    public String receiverId = "";       // 接收端ID号
    public int messageType;
    public String time;

    public BaseMessage(String senderId, String receiverId, int messageType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageType = messageType;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        time = sdf.format(new Date());
    }

    @Override public String toString() {
        return "BaseMessage{" +
                "senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", messageType='" + messageType + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
