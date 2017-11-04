package com.yf.minalibrary.encoderdecoder;

import android.util.Log;

import com.google.gson.Gson;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.CmdMessage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.nio.charset.Charset;

/**
 * Created by wuhuai on 2016/11/12 .
 * ;
 */

public class CmdMessageDecoder extends BaseMessageDecoder implements MessageDecoder {
    private String TAG = "CmdMessageDecoder";
    Gson mGson;
    public CmdMessageDecoder(){
        mGson=new Gson();
    }
    /**
     * 这个方法相当于是预读取，用于判断是否是可用的解码器。
     * A. MessageDecoderResult.NOT_OK：表示这个解码器不适合解码数据，然后检查其它解码器，如果都不满足会抛异常；
     * B. MessageDecoderResult.NEED_DATA：表示当前的读入的数据不够判断是否能够使用这个解码器解码，
     * 然后再次调用decodable()方法检查其它解码器，如果都是NEED_DATA,则等待下次输入；
     * C. MessageDecoderResult.OK： 表示这个解码器可以解码读入的数据，
     * 然后则调用MessageDecoder 的decode()方法。
     * 这里注意decodable()方法对参数IoBuffer in 的任何操作在方法结束之后，
     * 都会复原，也就是你不必担心在调用decode()方法时，
     * position 已经不在缓冲区的起始位置。
     *
     * @param ioSession
     * @param in
     * @return
     */
    @Override public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        Log.i(TAG,"cmd");
        return super.deadWork(MessageType.MESSAGE_CMD, in);
    }

    /**
     * 关注decode()方法即可。
     *
     * @param ioSession
     * @param in
     * @param outPut
     * @return
     * @throws Exception
     */
    @Override public  MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput outPut) throws Exception {
        Log.i(TAG,"cmd1");
        try {

            if (in.remaining() < 8) {
                return MessageDecoderResult.NEED_DATA;
            }
            in.mark();
            in.getInt();//类型这里用不到
            int messageLength = in.getInt();
            in.reset();
            if (in.remaining() < messageLength) {
                return MessageDecoderResult.NEED_DATA;
            }
            in.getInt();in.getInt();//跳过两个
            outPut.write(mGson.fromJson(in.getString(messageLength, Charset.forName("UTF-8").newDecoder()), CmdMessage.class));
        } catch (Exception e) {
            e.printStackTrace();
            return MessageDecoderResult.NOT_OK;
        }
        return MessageDecoderResult.OK;
    }

    /**
     * finishDecode()方法可以用于处理在IoSession 关闭时剩余的未读取数据，
     * 一般这个方法并不会被使用到，除非协议中未定义任何标识数据什么时候截止的约定，
     * 譬如：Http 响应的Content-Length 未设定，那么在你认为读取完数据后，关闭TCP连接（IoSession 的关闭）后，
     * 就可以调用这个方法处理剩余的数据，当然你也可以忽略调剩余的数据。同样的，一般情况下，
     * 我们只需要继承适配器ProtocolDecoderAdapter，关注decode()方法即可。
     *
     * @param ioSession
     * @param protocolDecoderOutput
     * @throws Exception
     */
    @Override public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}