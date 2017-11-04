package com.yf.minalibrary.encoderdecoder;

import android.util.Log;

import com.google.gson.Gson;
import com.yf.minalibrary.common.FileMessageConstant;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.FileMessage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

public class FileMessageDecoder extends BaseMessageDecoder implements MessageDecoder {
    private static final String  TAG="FileMessageDecoder";
    private AttributeKey CONTEXT = new AttributeKey(getClass(), "context");

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        Log.i(TAG,"file");
        if(getContext(session).initFlag){
            return MessageDecoderResult.OK;
        }
        return super.deadWork(MessageType.MESSAGE_FILE, in);

    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       ProtocolDecoderOutput outPut) throws Exception {
        Log.i(TAG,"file1");
        Context context = getContext(session);
        try {
            if (!context.initFlag) {
                if (in.remaining() < 8) {
                    return MessageDecoderResult.NEED_DATA;
                }
                in.getInt();//type类型这里用不到
                int bhLength = in.getInt();//文件头json长度
                if (in.remaining() < bhLength) {
                    return MessageDecoderResult.NEED_DATA;
                }

                byte[] mybuffer = new byte[bhLength];
                in.get(mybuffer);
                String a = new String(mybuffer, 0, mybuffer.length, "UTF-8");
                Log.i(TAG,"a = "+a);
                Gson gson = new Gson();
                FileMessage fileMessage = gson.fromJson(a, FileMessage.class);//得到文件头
                Log.i(TAG, "decode: "+fileMessage.toString());
                context.fileName=fileMessage.getFileName();
                context.fileSize=fileMessage.getFileSize();
                context.receiverId=fileMessage.receiverId;
                context.senderId=fileMessage.senderId;
                context.messageType=fileMessage.messageType;
                context.byteFile=new byte[context.fileSize];
                context.use=fileMessage.getUse();
                context.initFlag = true;
                Log.i(TAG,"possion = "+in.position());
                //把类型放进去
//                in.putInt(0,MessageType.MESSAGE_FILE);
            }
            int count = context.count;//先取出上次存到哪里了

            while (in.hasRemaining()) {
                byte b = in.get();
                context.byteFile[count] = b;
                if (count == context.fileSize - 1) {
                    break;
                }
                if(context.use.equals(FileMessageConstant.UPLOAD_MUSIC)&&count%(context.fileSize/10)==0||count==context.fileSize-1||count==0){
                    FileMessage message = new FileMessage(context.senderId, context.receiverId, context.messageType,context.fileName, context.fileSize, context.byteFile,FileMessageConstant.ON_LINE_MUSIC);
                    message.setCurrenSize(count);
                    outPut.write(message);
                }
                count++;
            }
            context.count = count;//记录取到哪里了
            session.setAttribute(CONTEXT, context);//写回整个context对象

            if (context.count == context.fileSize - 1) {
                if (context.use.equals(FileMessageConstant.UPLOAD_MUSIC)){
                FileMessage message = new FileMessage(context.senderId, context.receiverId, context.messageType,context.fileName, context.fileSize, context.byteFile,context.use);
                outPut.write(message);}
                context.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MessageDecoderResult.NOT_OK;
        }
        return MessageDecoderResult.OK;
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
        boolean initFlag = false;//是否初始化
        int count = 0;//这个文件取到的位置

        /**
         * 相当于
         */
        int messageType=-1;
        String senderId;
        String receiverId;
        String fileName;
        int fileSize = 0;
        byte[] byteFile;
        String use=null;

        void reset() {
            initFlag = false;
            count = 0;
            senderId = null;
            receiverId = null;
            fileName = null;
            fileSize = 0;
            byteFile = null;
            use=null;
            int messageType=-1;
        }
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput outPut)
            throws Exception {
    }

}
