package server.yf.com.remotecontrolserver_as.LanMina.library.message;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import server.yf.com.remotecontrolserver_as.LanMina.library.common.MessageType;

public class BaseMessage implements Serializable {

    private String messageType = MessageType.MESSAGE_INVALID;
    private String time;

    public BaseMessage(String messageType) {
        this.messageType = messageType;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        time = sdf.format(new Date());
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override public String toString() {
        return "BaseMessage{" +
                "messageType='" + messageType + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
