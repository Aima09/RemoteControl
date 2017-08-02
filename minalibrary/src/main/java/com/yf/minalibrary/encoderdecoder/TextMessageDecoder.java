package com.yf.minalibrary.encoderdecoder;

import com.google.gson.Gson;
import com.yf.minalibrary.common.BeanUtil;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.TextMessage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by wuhuai on 2016/11/12 .
 * ;
 */

public class TextMessageDecoder implements MessageDecoder {

    @Override public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        System.out.println("TextMessageDecoder" + " 解码器选择");
        if (in.remaining() < 4) {
            return MessageDecoderResult.NEED_DATA;
        }
        try {
            int messageLength = in.getInt();
            System.out.println("TextMessageDecoder TEXT总长度 messageLength = " + messageLength);
            if (messageLength <= 0){
                return MessageDecoderResult.NOT_OK;
            }
            if (in.remaining() < messageLength) {
                return MessageDecoderResult.NEED_DATA;
            } else {
                String a = in.getString(messageLength, BeanUtil.UTF_8.newDecoder());
                System.out.println("TextMessageDecoder 得到的TEXT内容  = " + a);
                System.out.println("TextMessageDecoder 得到的TEXT长度  = " + a.getBytes(BeanUtil.UTF_8).length);
                Gson gson = new Gson();
                TextMessage textMessage = gson.fromJson(a, TextMessage.class);
                if (textMessage.messageType.equals(MessageType.MESSAGE_TEXT)) {
                    return MessageDecoderResult.OK;
                }
            }
        } catch (Exception e) {
            System.out.println("TextMessageDecoder decodable 解码出错 = " + e.toString());
            e.printStackTrace();
            return MessageDecoderResult.NOT_OK;
        }
        return MessageDecoderResult.NOT_OK;
    }

    @Override public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput outPut) throws Exception {
        try {
            int messageLength = in.getInt();
            String a = in.getString(messageLength, BeanUtil.UTF_8.newDecoder());
            Gson gson = new Gson();
            TextMessage textMessage = gson.fromJson(a, TextMessage.class);
            System.out.println("TextMessageDecoder " + textMessage.toString());
            outPut.write(textMessage);
        } catch (Exception e) {
            System.out.println("TextMessageDecoder decode 解码出错 = " + e.toString());
            e.printStackTrace();
            return MessageDecoderResult.NOT_OK;
        }
        return MessageDecoderResult.OK;
    }

    @Override public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}