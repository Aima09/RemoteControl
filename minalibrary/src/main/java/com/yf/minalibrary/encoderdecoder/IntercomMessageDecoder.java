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

public class IntercomMessageDecoder extends BaseMessageDecoder implements MessageDecoder {

    @Override public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        return super.deadWork(MessageType.MESSAGE_INTERCOM,in);
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