package com.yf.remotecontrolserver.localminaserver.library.encoderdecoder;

import com.google.gson.Gson;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.nio.charset.Charset;

import com.yf.remotecontrolserver.localminaserver.library.common.BeanUtil;
import com.yf.remotecontrolserver.localminaserver.library.common.MessageType;
import com.yf.remotecontrolserver.localminaserver.library.message.BaseMessage;


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
                IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
                //String strOut = DateSecret.encryptDES(message.toString());//别看这里的处理，这里是我的数据包加密算法~你可以直接拿message.toString当数据
                Gson Gson=new Gson();
                String data=Gson.toJson(message);
                buf.putInt(data.getBytes(Charset.forName("utf-8")).length);
                buf.putString(data,Charset.forName("utf-8").newEncoder());
                buf.flip();
                outPut.write(buf);
                break;
            case MessageType.MESSAGE_FILE:
            case MessageType.MESSAGE_TEXT:
                String gsonMsg1 = gson.toJson(message);
                System.out.println("BaseMessageEncoder gsonMsg = " + gsonMsg1);
                buffer.putInt(gsonMsg1.length());
                buffer.putString(gsonMsg1, BeanUtil.UTF_8.newEncoder());
                buffer.flip();
                outPut.write(buffer);
                break;
        }
        System.out.println("BaseMessageEncoder " + "编码完成");
    }
}
