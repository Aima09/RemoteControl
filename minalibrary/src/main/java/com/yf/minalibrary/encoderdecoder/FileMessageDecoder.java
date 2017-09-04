package com.yf.minalibrary.encoderdecoder;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.yf.minalibrary.common.BeanUtil;
import com.yf.minalibrary.common.FileMessageConstant;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.FileMessage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.io.File;
import java.io.FileOutputStream;

import static android.R.id.message;

public class FileMessageDecoder extends BaseMessageDecoder implements MessageDecoder {
    private static final String  TAG="FileMessageDecoder";
    private Gson mGson;
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
                in.mark();
                in.getInt();//type类型这里用不到
                int bhLength = in.getInt();//文件头json长度
                in.reset();
                if (in.remaining() < bhLength) {
                    return MessageDecoderResult.NEED_DATA;
                }

                Log.i(TAG,"possion = "+in.position());
                in.position(in.position()+4);
                Log.i(TAG,"possion = "+in.position());
                in.getInt();
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
                context.receiverId=fileMessage.senderId;
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

                count++;

                if(context.use.equals(FileMessageConstant.ON_LINE_MUSIC)){
                    FileMessage message = new FileMessage(context.senderId, context.receiverId, context.messageType,context.fileName, context.fileSize, context.byteFile,context.use);
                    outPut.write(message);
                    return MessageDecoderResult.OK;
                }

            }
            context.count = count;//记录取到哪里了
            session.setAttribute(CONTEXT, context);//写回整个context对象

            //fileMessage里要有类型判断
                //创建文件
//                FileOutputStream os = createFile();
//
//                int fileSize = fileMessage.getFileSize();//文件总长度
//                int i = 0;
//                while (in.hasRemaining() && i < fileSize) {
//                    os.write(in.get());
//                    i++;
//                }
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

    private FileOutputStream createFile() {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/myyinyue");
            if (file.exists()) {//删除文件夹  相当于清楚缓存
                deleteDir(file);
            }
            file.mkdir();
            FileOutputStream os = new FileOutputStream(file.getPath() + "/" + "aa.mp3", true);
            return os;
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    File file = null;
    FileOutputStream os;
    private void writoFile(byte b) {
        try {

            file = new File(Environment.getExternalStorageDirectory() + "/myyinyue");
            boolean b1 = file.exists();

            if (!b1) {
                b1 = file.mkdir();
            }
            if(os==null)
            os=new FileOutputStream(file.getPath() + "/" + "aa.mp3",true);
            if (b1) {
                os.write(b);
                os.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
             //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
