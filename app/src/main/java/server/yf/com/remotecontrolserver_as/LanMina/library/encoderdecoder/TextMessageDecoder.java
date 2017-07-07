package server.yf.com.remotecontrolserver_as.LanMina.library.encoderdecoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.nio.charset.CharacterCodingException;

import server.yf.com.remotecontrolserver_as.LanMina.library.common.BeanUtil;
import server.yf.com.remotecontrolserver_as.LanMina.library.common.MessageType;

/**
 * Created by wuhuai on 2016/11/12 .
 * ;
 */

public class TextMessageDecoder implements MessageDecoder {

    private AttributeKey CONTEXT = new AttributeKey(getClass(), "context");

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        TextMessageDecoder.Context context = getContext(session);
        System.out.println("TextMessageDecoder " + "decodable exe  context.messageType = " + context.messageType);
        if (context.messageType.equals(MessageType.MESSAGE_INVALID)) {
            if (in.remaining() < 4) {
                return MessageDecoderResult.NEED_DATA;
            }
            try {
                int messageTypeLength = in.getInt();
                String messageType = in.getString(messageTypeLength, BeanUtil.UTF_8.newDecoder());
                if (messageType.equals(MessageType.MESSAGE_TEXT)) {
                    return MessageDecoderResult.OK;
                }
            } catch (CharacterCodingException e) {
                e.printStackTrace();
            }

            return MessageDecoderResult.NOT_OK;
        } else {
            if (context.messageType.equals(MessageType.MESSAGE_TEXT)) {
                return MessageDecoderResult.OK;
            } else {
                return MessageDecoderResult.NOT_OK;
            }
        }
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       ProtocolDecoderOutput outPut) throws Exception {
        return MessageDecoderResult.OK;
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput outPut)
            throws Exception {
    }

    private TextMessageDecoder.Context getContext(IoSession session) {
        TextMessageDecoder.Context context = (TextMessageDecoder.Context) session.getAttribute(CONTEXT);
        if (context == null) {
            context = new TextMessageDecoder.Context();
            session.setAttribute(CONTEXT, context);
        }
        return context;
    }

    private class Context {
        boolean initMessageType = false;
        String messageType = MessageType.MESSAGE_INVALID;
        int count = 0;
        String fileBeanHead;
        String senderId;
        String receiverId;
        String fileName;
        int fileSize = 0;
        byte[] byteFile;

        void reset() {
            initMessageType = false;
            messageType = MessageType.MESSAGE_INVALID;
            count = 0;
            fileBeanHead = null;
            senderId = null;
            receiverId = null;
            fileName = null;
            fileSize = 0;
            byteFile = null;
        }
    }

}
