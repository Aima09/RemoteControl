package com.yf.minalibrary.encoderdecoder;

import com.yf.minalibrary.message.BaseMessage;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

public class MessageProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    public MessageProtocolCodecFactory() {
        super.addMessageDecoder(TextMessageDecoder.class);
        super.addMessageDecoder(CmdMessageDecoder.class);
        super.addMessageDecoder(IntercomMessageDecoder.class);
        super.addMessageDecoder(FileMessageDecoder.class);

        super.addMessageEncoder(BaseMessage.class, BaseMessageEncoder.class);
    }
}
