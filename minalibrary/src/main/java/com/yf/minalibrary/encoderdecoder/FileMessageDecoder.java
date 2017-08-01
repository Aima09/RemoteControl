package com.yf.minalibrary.encoderdecoder;

import com.google.gson.Gson;
import com.yf.minalibrary.common.BeanUtil;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.FileMessage;
import com.yf.minalibrary.message.FileMessage.FileBean;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;


public class FileMessageDecoder implements MessageDecoder {

    private AttributeKey CONTEXT = new AttributeKey(getClass(), "context");

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        System.out.println("FileMessageDecoder" + " 解码器选择");
        Context context = getContext(session);
        if (context.messageType.equals(MessageType.MESSAGE_INVALID)) {
            if (in.remaining() < 4) {
                return MessageDecoderResult.NEED_DATA;
            }
            try {
                int messageLength = in.getInt();
                System.out.println("FileMessageDecoder 文件信息内容总长度 messageLength = " + messageLength);
                if (in.remaining() < messageLength){
                    return MessageDecoderResult.NEED_DATA;
                } else {
                    String a = in.getString(messageLength, BeanUtil.UTF_8.newDecoder());
                    System.out.println("FileMessageDecoder 得到的文件信息内容  = " + a);
                    System.out.println("FileMessageDecoder 得到的文件信息内容长度  = " + a.getBytes(BeanUtil.UTF_8).length);
                    Gson gson = new Gson();
                    FileMessage fileMessage = gson.fromJson(a, FileMessage.class);
                    if (fileMessage.messageType.equals(MessageType.MESSAGE_FILE)) {
                        return MessageDecoderResult.OK;
                    }
                }
            } catch (Exception e) {
                System.out.println("FileMessageDecoder" + " decodable 解码器出错："+ e.toString());
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
        try {
            if (!context.initFlag) {
                int messageLength = in.getInt();
                context.fileBeanHead = in.getString(messageLength, BeanUtil.UTF_8.newDecoder());
                String[] fbs = context.fileBeanHead.split(",");
                if (fbs.length == 6) {
                    context.senderId = fbs[0].split("=").length > 1 ? fbs[0].split("=")[1] : "";
                    context.receiverId = fbs[1].split("=").length > 1 ? fbs[1].split("=")[1] : "";
                    context.messageType = fbs[2].split("=").length > 1 ? fbs[2].split("=")[1] : MessageType.MESSAGE_INVALID;
                    context.fileName = fbs[4].split("=").length > 1 ? fbs[4].split("=")[1] : "";
                    context.fileSize = fbs[5].split("=").length > 1 ? Integer.valueOf(fbs[5].split("=")[1]) : 0;
                    context.byteFile = new byte[context.fileSize];
                }
                context.initFlag = true;
            }
            int count = context.count;
            while (in.hasRemaining()) {
                byte b = in.get();
                context.byteFile[count] = b;
                if (count == context.fileSize - 1) {
                    break;
                }
                count++;
            }
            context.count = count;
            session.setAttribute(CONTEXT, context);
            if (context.count == context.fileSize - 1) {
                FileBean bean = new FileBean(context.fileName,context.fileSize,context.byteFile);
                FileMessage message = new FileMessage(context.senderId,context.receiverId,context.messageType,bean);
                outPut.write(message);
                context.reset();
            }
        } catch (Exception e) {
            System.out.println("FileMessageDecoder decode 解码出错 = " + e.toString());
            e.printStackTrace();
            return MessageDecoderResult.NOT_OK;
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
        boolean initFlag = false;
        String messageType = MessageType.MESSAGE_INVALID;
        int count = 0;
        String fileBeanHead;
        String senderId;
        String receiverId;
        String fileName;
        int fileSize = 0;
        byte[] byteFile;

        void reset() {
            initFlag = false;
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
