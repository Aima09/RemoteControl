package com.yf.minalibrary.encoderdecoder;

import com.google.gson.Gson;
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

public class TextMessageDecoder extends BaseMessageDecoder implements MessageDecoder {

    @Override public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        return super.deadWork(MessageType.MESSAGE_TEXT, in);
    }

    @Override public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput outPut) throws Exception {
        try {
            in.getInt();//类型这里用不到
            if (in.remaining() < 4) {
                return MessageDecoderResult.NEED_DATA;
            }
            int messageLength = in.getInt();
            if (in.remaining() < messageLength) {
                return MessageDecoderResult.NEED_DATA;
            }
            byte[] mybuffer = new byte[messageLength];
            in.get(mybuffer);
            String a = new String(mybuffer, 0, mybuffer.length, "UTF-8");
            Gson gson = new Gson();
            TextMessage textMessage = gson.fromJson(a, TextMessage.class);
            outPut.write(textMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return MessageDecoderResult.NOT_OK;
        }
        return MessageDecoderResult.OK;
    }

    @Override public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}