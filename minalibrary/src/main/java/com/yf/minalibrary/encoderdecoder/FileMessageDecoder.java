package com.yf.minalibrary.encoderdecoder;

import android.util.Log;

import com.yf.minalibrary.common.BeanUtil;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.FileMessage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.nio.charset.CharacterCodingException;


public class FileMessageDecoder implements MessageDecoder {

    private AttributeKey CONTEXT = new AttributeKey(getClass(), "context");

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        Context context = getContext(session);
        Log.d("FileMessageDecoder", "decodable exe  context.messageType = " + context.messageType);
        if (context.messageType.equals(MessageType.MESSAGE_INVALID)) {
            if (in.remaining() < 4) {
                return MessageDecoderResult.NEED_DATA;
            }
            try {
                int messageTypeLength = in.getInt();
                String messageType = in.getString(messageTypeLength, BeanUtil.UTF_8.newDecoder());
                if (messageType.equals(MessageType.MESSAGE_FILE)) {
                    return MessageDecoderResult.OK;
                }
            } catch (CharacterCodingException e) {
                e.printStackTrace();
                return MessageDecoderResult.NOT_OK;
            }
            return MessageDecoderResult.NOT_OK;
        } else {
            if (context.messageType.equals(MessageType.MESSAGE_FILE)) {
                return MessageDecoderResult.OK;
            } else {
                return MessageDecoderResult.NOT_OK;
            }
        }
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       ProtocolDecoderOutput outPut) throws Exception {
        Context context = getContext(session);
        Log.d("FileMessageDecoder", "decode exe");
        if (!context.initMessageType) {
            int messageTypeLength = in.getInt();
            context.messageType = in.getString(messageTypeLength, BeanUtil.UTF_8.newDecoder());
            int fileBeanHeadLength = in.getInt();
            context.fileBeanHead = in.getString(fileBeanHeadLength, BeanUtil.UTF_8.newDecoder());
            String[] fbs = context.fileBeanHead.split(",");
            Log.d("FileMessageDecoder", "fbs.length = " + fbs.length);
            if (fbs.length == 4) {
                Log.d("FileMessageDecoder", "fbs[0].length = " + fbs[0].split("=").length);
                context.senderId = fbs[0].split("=")[1];
                context.receiverId = fbs[1].split("=")[1];
                context.fileName = fbs[2].split("=")[1];
                context.fileSize = Integer.valueOf(fbs[3].split("=")[1]);
                context.byteFile = new byte[context.fileSize];
            }
            context.initMessageType = true;
            Log.d("FileMessageDecoder", "fileBeanHeadString = " + context.fileBeanHead);
        }
        int count = context.count;
        while (in.hasRemaining()) {
            byte b = in.get();
            context.byteFile[count] = b;
            if (count == context.fileSize - 1) {
                Log.d("FileMessageDecoder", "count0:" + count);
                break;
            }
            count++;
        }
        context.count = count;
        Log.d("FileMessageDecoder", "count1:" + count);
        session.setAttribute(CONTEXT, context);
        if (context.count == context.fileSize - 1) {
            FileMessage.FileBean bean = new FileMessage.FileBean();
            bean.setSenderId(context.senderId);
            bean.setReceiverId(context.receiverId);
            bean.setFileName(context.fileName);
            Log.d("FileMessageDecoder", "FileName = " + bean.getFileName());
            bean.setFileSize(context.fileSize);
            bean.setFileContent(context.byteFile);
            FileMessage message = new FileMessage(context.messageType,bean);
            outPut.write(message);
            context.reset();
        }
        return MessageDecoderResult.OK;
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput outPut)
            throws Exception {
    }

    private Context getContext(IoSession session) {
        Context context = (Context) session.getAttribute(CONTEXT);
        if (context == null) {
            context = new Context();
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
