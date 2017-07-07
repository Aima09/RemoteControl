package server.yf.com.remotecontrolserver_as.LanMina.library.encoderdecoder;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

import server.yf.com.remotecontrolserver_as.LanMina.library.message.BaseMessage;

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
