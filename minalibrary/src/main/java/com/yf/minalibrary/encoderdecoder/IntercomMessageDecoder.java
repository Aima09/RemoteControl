package com.yf.minalibrary.encoderdecoder;

import com.google.gson.Gson;
import com.yf.minalibrary.common.BeanUtil;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.CmdMessage;
import com.yf.minalibrary.message.IntercomMessage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by wuhuai on 2016/11/12 .
 * ;
 */

public class IntercomMessageDecoder implements MessageDecoder {

    @Override public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        System.out.println("IntercomMessageDecoder" + " 解码器选择");
        if (in.remaining() < 4) {
            return MessageDecoderResult.NEED_DATA;
        }
        try {
            int messageLength = in.getInt();
            System.out.println("IntercomMessageDecoder 录音总长度 messageLength = " + messageLength);
            if (in.remaining() < messageLength) {
                return MessageDecoderResult.NEED_DATA;
            } else {
                String a = in.getString(messageLength, BeanUtil.UTF_8.newDecoder());
                System.out.println("IntercomMessageDecoder 得到的录音内容  = " + a);
                System.out.println("IntercomMessageDecoder 得到的录音长度  = " + a.getBytes(BeanUtil.UTF_8).length);
                Gson gson = new Gson();
                IntercomMessage intercomMessage = gson.fromJson(a, IntercomMessage.class);
                if (intercomMessage.messageType.equals(MessageType.MESSAGE_INTERCOM)) {
                    return MessageDecoderResult.OK;
                }
            }
        } catch (Exception e) {
            System.out.println("IntercomMessageDecoder decodable 解码出错 = " + e.toString());
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
            IntercomMessage intercomMessage = gson.fromJson(a, IntercomMessage.class);
            System.out.println("IntercomMessageDecoder " + intercomMessage.toString());
            outPut.write(intercomMessage);
        } catch (Exception e) {
            System.out.println("IntercomMessageDecoder decode 解码出错 = " + e.toString());
            e.printStackTrace();
            return MessageDecoderResult.NOT_OK;
        }
        return MessageDecoderResult.OK;
    }

    @Override public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}