package com.yf.remotecontrolclient.minaclient.udp;

import android.util.Log;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class UdpClientHander extends IoHandlerAdapter {


    public void messageReceived(IoSession session, Object message) throws Exception {
        String content = message.toString();
        Log.d("UdpClientHander", "==============");
        Log.d("UdpClientHander", "content = " + content);
    }

    public void messageSent(IoSession session, Object message) throws Exception {
        Log.d("UdpClientHander", "messageSent 客户端发送消息：" + message);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        Log.d("UdpClientHander", "服务器发生异常： {" + cause.getMessage() + "}");
    }

    @Override public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        Log.d("UdpClientHander", "sessionOpened");
    }

    @Override public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        Log.d("UdpClientHander", "sessionCreated");
    }

    @Override public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        Log.d("UdpClientHander", "sessionClosed");
    }

    @Override public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        Log.d("UdpClientHander", "sessionIdle");
    }

    @Override public void inputClosed(IoSession session) throws Exception {
        super.inputClosed(session);
        Log.d("UdpClientHander", "inputClosed");
    }


}
