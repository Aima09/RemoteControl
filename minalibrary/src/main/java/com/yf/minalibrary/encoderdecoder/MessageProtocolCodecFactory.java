package com.yf.minalibrary.encoderdecoder;

import com.yf.minalibrary.message.BaseMessage;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

public class MessageProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    public MessageProtocolCodecFactory() {
            super.addMessageDecoder(FileMessageDecoder.class);
            super.addMessageDecoder(CmdMessageDecoder.class);
            super.addMessageDecoder(IntercomMessageDecoder.class);
//            super.addMessageDecoder(TextMessageDecoder.class);
            super.addMessageEncoder(BaseMessage.class, BaseMessageEncoder.class);
    }
}
