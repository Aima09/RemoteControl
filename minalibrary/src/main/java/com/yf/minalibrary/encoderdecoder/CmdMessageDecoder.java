package com.yf.minalibrary.encoderdecoder;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.yf.minalibrary.common.BeanUtil;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.BaseMessage;
import com.yf.minalibrary.message.CmdMessage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.nio.charset.CharacterCodingException;

/**
 * Created by wuhuai on 2016/11/12 .
 * ;
 */

public class CmdMessageDecoder implements MessageDecoder {

    @Override public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        System.out.println("CmdMessageDecoder" + " 解码器选择");
        if (in.remaining() >  0) {
            try {
                String a = in.getString(BeanUtil.UTF_8.newDecoder());
                if (!TextUtils.isEmpty(a)){
                    Gson gson = new Gson();
                    CmdMessage cmdMessage = gson.fromJson(a,CmdMessage.class);
                    if (cmdMessage.getMessageType().equals(MessageType.MESSAGE_CMD)){
                        return MessageDecoderResult.OK;
                    }
                }
            } catch (CharacterCodingException e) {
                e.printStackTrace();
            }
        }
        return MessageDecoderResult.NOT_OK;
    }

    @Override public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput outPut) throws Exception {
        String a = in.getString(BeanUtil.UTF_8.newDecoder());
        Gson gson = new Gson();
        CmdMessage cmdMessage = gson.fromJson(a,CmdMessage.class);
        outPut.write(cmdMessage);
        System.out.println("CmdMessageDecoder " + a);
        return MessageDecoderResult.OK;
    }

    @Override public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
