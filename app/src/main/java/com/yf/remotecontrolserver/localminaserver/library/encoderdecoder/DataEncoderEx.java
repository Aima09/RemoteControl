package com.yf.remotecontrolserver.localminaserver.library.encoderdecoder;

import com.google.gson.Gson;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;

/**
 * Created by sujuntao on 2017/7/5.
 */

public class DataEncoderEx extends ProtocolEncoderAdapter {

    /* (non-Javadoc)
         * @see org.apache.mina.filter.codec.ProtocolEncoder#encode(org.apache.mina.core.session.IoSession, Java.lang.Object, org.apache.mina.filter.codec.ProtocolEncoderOutput)
         */
    public void encode(IoSession session, Object message,
                       ProtocolEncoderOutput out) throws Exception {
        System.out.println(message);
        IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
        //String strOut = DateSecret.encryptDES(message.toString());//别看这里的处理，这里是我的数据包加密算法~你可以直接拿message.toString当数据
        Gson Gson=new Gson();
        String data=Gson.toJson(message);
        buf.putInt(data.getBytes(Charset.forName("utf-8")).length);
        buf.putString(data,Charset.forName("utf-8").newEncoder());
        buf.flip();
        out.write(buf);
    }
}
