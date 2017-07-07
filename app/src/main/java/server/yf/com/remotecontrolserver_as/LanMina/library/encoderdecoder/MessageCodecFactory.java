package server.yf.com.remotecontrolserver_as.LanMina.library.encoderdecoder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * Created by sujuntao on 2017/7/5.
 */

public class MessageCodecFactory implements ProtocolCodecFactory {
    private final DataEncoderEx encoder;
    private final DataDecoderEx decoder;

    public MessageCodecFactory() {
        encoder = new DataEncoderEx();
        decoder = new DataDecoderEx();
    }

    /* (non-Javadoc)
     * @see org.apache.mina.filter.codec.ProtocolCodecFactory#getDecoder(org.apache.mina.core.session.IoSession)
     */
    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }

    /* (non-Javadoc)
     * @see org.apache.mina.filter.codec.ProtocolCodecFactory#getEncoder(org.apache.mina.core.session.IoSession)
     */
    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }
}