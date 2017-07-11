package com.yf.minalibrary.encoderdecoder;

import com.google.gson.Gson;
import com.yf.minalibrary.common.BeanUtil;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.BaseMessage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;


public class BaseMessageEncoder implements MessageEncoder<BaseMessage> {

    private Gson gson;

    public BaseMessageEncoder() {
        gson = new Gson();
    }

    public void encode(IoSession session, BaseMessage message, ProtocolEncoderOutput outPut) throws Exception {
        IoBuffer buffer = IoBuffer.allocate(1000).setAutoExpand(true);
        String messageType = message.getMessageType();
        System.out.println("BaseMessageEncoder " + messageType);
        switch (messageType){
            case MessageType.MESSAGE_CMD:
            case MessageType.MESSAGE_FILE:
            case MessageType.MESSAGE_TEXT:
                try {
                    String gsonMsg = gson.toJson(message);
                    System.out.println("BaseMessageEncoder gsonMsg = " + gsonMsg);
                    buffer.putInt(gsonMsg.length());
                    buffer.putString(gsonMsg, BeanUtil.UTF_8.newEncoder());
                    buffer.flip();
                    outPut.write(buffer);
                } catch (Exception e) {
                    System.out.println("BaseMessageEncoder 编码 error = " + e.toString());
                    e.printStackTrace();
                }
                break;
        }
        System.out.println("BaseMessageEncoder " + "编码完成");
    }
}
