package server.yf.com.remotecontrolserver_as.LanMina.library.encoderdecoder;

import com.google.gson.Gson;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import server.yf.com.remotecontrolserver_as.LanMina.library.message.CmdMessage;

/**
 * Created by wuhuai on 2016/11/12 .
 * ;
 */

public class CmdMessageDecoder implements MessageDecoder {

    @Override public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        System.out.println("CmdMessageDecoder" + " 解码器选择");

        if(in.remaining()<4)//这里很关键，网上很多代码都没有这句，是用来当拆包时候剩余长度小于4的时候的保护，不加就出错咯
        {
            System.out.println("CmdMessageDecoder" + " 需要数据");
            return MessageDecoderResult.NEED_DATA;
        }
        try {
            if (in.remaining() > 1) {

                in.mark();//标记当前位置，以便reset
                int length =in.getInt(in.position());

                if(length > in.remaining()-4){//如果消息内容不够，则重置，相当于不读取size
                    System.out.println("package notenough  left="+in.remaining()+" length="+length);
                    in.reset();
                    System.out.println("CmdMessageDecoder" + " NEED_DATA2");
                    return MessageDecoderResult.NEED_DATA;//接收新数据，以拼凑成完整数据
                }else{
                    return MessageDecoderResult.OK;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MessageDecoderResult.NOT_OK;
    }

    @Override public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput outPut) throws Exception {
        System.out.println("package ="+in.toString());
        int length= in.getInt();

        byte[] bytes = new byte[length];
        in.get(bytes, 0, length);
        String str = new String(bytes,"UTF-8");
        Gson gson = new Gson();
        CmdMessage cmdMessage = gson.fromJson(str,CmdMessage.class);
        outPut.write(cmdMessage);
        return MessageDecoderResult.OK;//这里有两种情况1：没数据了，那么就结束当前调用，有数据就再次调用
    }

    @Override public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
