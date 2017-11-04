package com.yf.minalibrary.encoderdecoder;

import android.util.Log;

import com.google.gson.Gson;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.BaseMessage;
import com.yf.minalibrary.message.FileMessage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.nio.charset.Charset;


public class BaseMessageEncoder implements MessageEncoder<BaseMessage> {
    public static String TAG="BaseMessageEncoder";
    private Gson mGson;

    public BaseMessageEncoder() {
        mGson = new Gson();
    }

    public void encode(IoSession session, BaseMessage message, ProtocolEncoderOutput outPut) throws Exception {
        IoBuffer buffer = IoBuffer.allocate(1000).setAutoExpand(true);
        int messageType = message.messageType;
        try {
            switch (messageType) {
                case MessageType.MESSAGE_CMD:
                case MessageType.MESSAGE_TEXT:
                case MessageType.MESSAGE_INTERCOM:
                    encodeThree(messageType, buffer,message);
                    break;
                case MessageType.MESSAGE_FILE:
                    encodeFile(messageType, buffer,message);
                    break;
            }
            buffer.flip();
            outPut.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void encodeFile(int messageType, IoBuffer buffer, BaseMessage message) {
        try {
            FileMessage fileMessage = (FileMessage) message;
            //去掉内容变json
            byte[] b = fileMessage.getFileContent();
            fileMessage.setFileContent(null);
            String fileHead = mGson.toJson(fileMessage);
            Log.i(TAG,fileHead);
            byte[] bh = fileHead.getBytes("UTF-8");

            //第一个四字节放类型
            buffer.putInt(messageType);

            //第二个四字节放头长度
            buffer.putInt(bh.length);

            buffer.put(bh);//头的json

            buffer.put(b);//文件byte[]
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 前三个type都调用这个重载的函数
     * @param type
     * @param buffer
     */
    private  void encodeThree(int type, IoBuffer buffer,BaseMessage message){
        try {
            String gsonMsg = mGson.toJson(message);
            byte[] b=gsonMsg.getBytes("UTF-8");
            //第一个四字节 放数据类型
            buffer.putInt(type);
            //第二个四字节 放数据长度
            buffer.putInt(b.length);
            //内容
            buffer.putString(gsonMsg,Charset.forName("UTF-8").newEncoder());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}