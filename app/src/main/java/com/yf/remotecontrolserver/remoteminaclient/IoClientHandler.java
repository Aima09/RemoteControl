package com.yf.remotecontrolserver.remoteminaclient;

import android.os.Environment;
import android.util.Log;

import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.BaseMessage;
import com.yf.minalibrary.message.CmdMessage;
import com.yf.minalibrary.message.FileMessage;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.io.File;
import java.io.FileOutputStream;

import com.yf.remotecontrolserver.common.CommonConstant;


/**
 * Created by wuhuai on 2016/10/25 .
 * ;
 */

public class IoClientHandler extends IoHandlerAdapter {

    public void messageReceived(IoSession session, Object message) throws Exception {
        CommonConstant.LINE_TYPE = 2;
        BaseMessage baseMessage = (BaseMessage) message;
        String dataType = baseMessage.getMessageType();
        switch (dataType) {
            case MessageType.MESSAGE_CMD:
                CmdMessage cmdMessage = (CmdMessage) baseMessage;
                ClientMinaCmdManager.getInstance().disposeCmd(cmdMessage);
                break;
            case MessageType.MESSAGE_FILE:
                FileMessage fileMessage = (FileMessage) baseMessage;
                FileMessage.FileBean bean = fileMessage.getFileBean();
                Log.d("IoClientHandler", "Received filename = " + bean.getFileName());
                File file = new File(Environment.getExternalStorageDirectory() + "/tupian");
                boolean b = file.exists();
                if (!b) {
                    b = file.mkdir();
                }
                if (b) {
                    FileOutputStream os = new FileOutputStream(file.getPath() + "/" + bean.getFileName());
                    os.write(bean.getFileContent());
                    os.close();
                }
                break;
            case MessageType.MESSAGE_TEXT:
                break;
        }
    }

    public void messageSent(IoSession session, Object message) throws Exception {
        Log.d("IoClientHandler", "messageSent 客户端发送消息：" + message);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        Log.d("IoClientHandler", "exceptionCaught 发生异常： {" + cause.toString() + "}");
        cause.printStackTrace();
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        Log.d("IoClientHandler", "sessionOpened");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        Log.d("IoClientHandler", "sessionCreated");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        Log.d("IoClientHandler", "sessionClosed");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        Log.d("IoClientHandler", "sessionIdle");
        Log.d("IoClientHandler", "getLocalAddress" + session.getLocalAddress().toString());
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        super.inputClosed(session);
        Log.d("IoClientHandler", "inputClosed");
    }
}
