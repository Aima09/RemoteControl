package com.yf.remotecontrolserver.localminaserver.library.encoderdecoder;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

import com.yf.remotecontrolserver.localminaserver.library.message.BaseMessage;

public class MessageProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    public MessageProtocolCodecFactory(boolean server) {
        if (server) {
//            super.addMessageDecoder(FileMessageDecoder.class);

            super.addMessageDecoder(CmdMessageDecoder.class);
//            super.addMessageDecoder(TextMessageDecoder.class);
            super.addMessageEncoder(BaseMessage.class, BaseMessageEncoder.class);
        } else {
//            super.addMessageDecoder(FileMessageDecoder.class);
            super.addMessageDecoder(CmdMessageDecoder.class);
//            super.addMessageDecoder(TextMessageDecoder.class);
            super.addMessageEncoder(BaseMessage.class, BaseMessageEncoder.class);
        }
    }
}
