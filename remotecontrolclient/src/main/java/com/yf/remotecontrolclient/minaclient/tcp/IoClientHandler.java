package com.yf.remotecontrolclient.minaclient.tcp;

import android.util.Log;

import com.yf.minalibrary.common.CmdType;
import com.yf.minalibrary.common.DeviceType;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.BaseMessage;
import com.yf.minalibrary.message.CmdMessage;
import com.yf.minalibrary.message.CmdMessage.CmdBean;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;


/**
 * Created by wuhuai on 2016/10/25 .
 * ;
 */

public class IoClientHandler extends IoHandlerAdapter {

    public void messageReceived(IoSession session, Object message) throws Exception {
        BaseMessage baseMessage = (BaseMessage) message;
        String dataType = baseMessage.getMessageType();
        switch (dataType) {
            case MessageType.MESSAGE_CMD:
                CmdMessage cmdMessage = (CmdMessage) baseMessage;
                MinaCmdManager.getInstance().disposeCmd(cmdMessage);
                break;
            case MessageType.MESSAGE_FILE:
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
        Log.d("IoClientHandler", "远程连接发生异常： {" + cause.getMessage() + "}");
        super.exceptionCaught(session, cause);
    }

    @Override public void sessionOpened(IoSession session) throws Exception {
        Log.d("IoClientHandler", "sessionOpened");
        super.sessionOpened(session);
    }

    @Override public void sessionCreated(IoSession session) throws Exception {
        Log.d("IoClientHandler", "sessionCreated");
        super.sessionCreated(session);
    }

    @Override public void sessionClosed(IoSession session) throws Exception {
        Log.d("IoClientHandler", "sessionClosed");
        super.sessionClosed(session);
    }

    @Override public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        Log.d("IoClientHandler", "sessionIdle");
        CmdBean cmdBean = new CmdBean(ServerDataDisposeCenter.getLocalSenderId(), "", CmdType.CMD_HEARTBEAT, DeviceType.DEVICE_TYPE_PHONE, "");
        CmdMessage cmdMessage = new CmdMessage(MessageType.MESSAGE_CMD, cmdBean);
        session.write(cmdMessage);
        super.sessionIdle(session, status);
    }

    @Override public void inputClosed(IoSession session) throws Exception {
        Log.d("IoClientHandler", "inputClosed");
        super.inputClosed(session);
    }
}
