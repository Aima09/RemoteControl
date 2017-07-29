package com.yf.minalibrary.encoderdecoder;

import android.util.Log;

import com.yf.minalibrary.common.BeanUtil;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.IntercomMessage;
import com.yf.minalibrary.message.IntercomMessage.IntercomBean;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;


public class IntercomMessageDecoder implements MessageDecoder {

    private AttributeKey CONTEXT = new AttributeKey(getClass(), "context");

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        System.out.println("IntercomMessageDecoder" + " 解码器选择");
        Context context = getContext(session);
        if (context.messageType.equals(MessageType.MESSAGE_INVALID)) {
            if (in.remaining() < 4) {
                return MessageDecoderResult.NEED_DATA;
            }
            try {
                int messageLength = in.getInt();
                System.out.println("IntercomMessageDecoder 录音信息内容总长度 messageLength = " + messageLength);
                if (in.remaining() < messageLength){
                    return MessageDecoderResult.NEED_DATA;
                } else {
                    String a = in.getString(messageLength, BeanUtil.UTF_8.newDecoder());
                    System.out.println("IntercomMessageDecoder 得到的录音信息内容  = " + a);
                    System.out.println("IntercomMessageDecoder 得到的录音信息内容长度  = " + a.getBytes(BeanUtil.UTF_8).length);
                    String[] strings = a.split(",");
                    if (strings.length > 4){
                        if (strings[0].split("=").length > 1){
                            if (strings[0].split("=")[1].equals(MessageType.MESSAGE_INTERCOM)) {
                                return MessageDecoderResult.OK;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("IntercomMessageDecoder" + " decodable 解码器出错："+ e.toString());
                e.printStackTrace();
                return MessageDecoderResult.NOT_OK;
            }
            return MessageDecoderResult.NOT_OK;
        } else {
            if (context.messageType.equals(MessageType.MESSAGE_INTERCOM)) {
                return MessageDecoderResult.OK;
            } else {
                return MessageDecoderResult.NOT_OK;
            }
        }
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       ProtocolDecoderOutput outPut) throws Exception {
        Context context = getContext(session);
        Log.d("IntercomMessageDecoder", "decode exe");
        if (!context.initFlag) {
            int messageHeadLength = in.getInt();
            context.messageHead = in.getString(messageHeadLength, BeanUtil.UTF_8.newDecoder());
            String[] mh = context.messageHead.split(",");
            Log.d("IntercomMessageDecoder", "fbs.length = " + mh.length);
            if (mh.length == 5) {
                Log.d("IntercomMessageDecoder", "fbs[0].length = " + mh[0].split("=").length);
                context.messageType = mh[0].split("=").length > 1 ? mh[0].split("=")[1]:MessageType.MESSAGE_INVALID;
                context.senderId = mh[2].split("=").length > 1 ? mh[2].split("=")[1]:"";
                context.receiverId = mh[3].split("=").length > 1 ? mh[3].split("=")[1]:"";
                context.intercomContentLength = mh[4].split("=").length > 1 ? Integer.valueOf(mh[4].split("=")[1]):0;
                context.intercomContent = new byte[context.intercomContentLength];
            }
            context.initFlag = true;
            Log.d("IntercomMessageDecoder", "intercomContent = " + context.messageHead);
        }
        int count = context.count;
        while (in.hasRemaining()) {
            byte b = in.get();
            context.intercomContent[count] = b;
            if (count == context.intercomContentLength - 1) {
                Log.d("IntercomMessageDecoder", "count0:" + count);
                break;
            }
            count++;
        }
        context.count = count;
        Log.d("IntercomMessageDecoder", "count1:" + count);
        session.setAttribute(CONTEXT, context);
        if (context.count == context.intercomContentLength - 1) {
            IntercomBean intercomBean = new IntercomBean(context.senderId,context.receiverId,context.intercomContent);
            IntercomMessage intercomMessage = new IntercomMessage(context.messageType,intercomBean);
            outPut.write(intercomMessage);
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
        boolean initFlag = false;
        String messageType = MessageType.MESSAGE_INVALID;
        int count = 0;
        String messageHead;
        String senderId;
        String receiverId;
        int intercomContentLength = 0;
        byte[] intercomContent;

        void reset() {
            initFlag = false;
            messageType = MessageType.MESSAGE_INVALID;
            count = 0;
            messageHead = null;
            senderId = null;
            receiverId = null;
            intercomContentLength = 0;
            intercomContent = null;
        }
    }

}
