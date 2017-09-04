package com.yf.remotecontrolserver.remoteminaclient;

import android.os.Environment;
import android.util.Log;

import com.yf.minalibrary.common.CmdType;
import com.yf.minalibrary.common.DeviceType;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.BaseMessage;
import com.yf.minalibrary.message.CmdMessage;
import com.yf.minalibrary.message.FileMessage;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;


import com.yf.minalibrary.message.IntercomMessage;
import com.yf.remotecontrolserver.common.CommonConstant;


/**
 * Created by wuhuai on 2016/10/25 .
 * ;
 */

public class IoClientHandler extends IoHandlerAdapter {

    public void messageReceived(IoSession session, Object message) throws Exception {
        CommonConstant.LINE_TYPE = 2;
        BaseMessage baseMessage = (BaseMessage) message;
        int dataType = baseMessage.messageType;
        switch (dataType) {
            case MessageType.MESSAGE_CMD:
                CmdMessage cmdMessage = (CmdMessage) baseMessage;
                ClientMinaCmdManager.getInstance().disposeCmd(cmdMessage);
                break;
            case MessageType.MESSAGE_FILE:
                FileMessage fileMessage = (FileMessage) baseMessage;
                ClientMinaFileManager.getInstance().disposeFile(fileMessage);
                break;
            case MessageType.MESSAGE_TEXT:
                break;
            case MessageType.MESSAGE_INTERCOM:
                IntercomMessage intercomMessage = (IntercomMessage) baseMessage;
                ClientMinaIntercomManager.getInstance().disposeIntercom(intercomMessage);
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
        CmdMessage cmdMessage = new CmdMessage(ClientDataDisposeCenter.getLocalSenderId(), "", MessageType.MESSAGE_CMD, CmdType.CMD_HEARTBEAT, DeviceType.DEVICE_TYPE_IPAD, "");
        session.write(cmdMessage);
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        super.inputClosed(session);
        Log.d("IoClientHandler", "inputClosed");
    }
}
