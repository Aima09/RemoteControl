package server.yf.com.remotecontrolserver_as.LanMina.library;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.LineDelimiter;

import java.nio.charset.Charset;

/**
 * Created by wuhuai on 2016/10/20 .
 * ;
 */

public class CharsetEncoder implements ProtocolEncoder {
//    private final static Logger log = LoggerFactory.getLogger(CharsetEncoder.class);
    private final static Charset charset = Charset.forName("UTF-8");

    @Override
    public void dispose(IoSession session) throws Exception {
//        log.info("#############dispose############");
    }

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
//        log.info("#############�ַ�����############");
        IoBuffer buff = IoBuffer.allocate(100).setAutoExpand(true);
        buff.putString(message.toString(), charset.newEncoder());
        // put ��ǰϵͳĬ�ϻ��з�
        buff.putString(LineDelimiter.DEFAULT.getValue(), charset.newEncoder());
        // Ϊ��һ�ζ�ȡ������׼��
        buff.flip();

        out.write(buff);
    }
}
