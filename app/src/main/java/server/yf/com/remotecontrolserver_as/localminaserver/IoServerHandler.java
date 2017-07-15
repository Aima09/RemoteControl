package server.yf.com.remotecontrolserver_as.localminaserver;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import server.yf.com.remotecontrolserver_as.CommonConstant;
import server.yf.com.remotecontrolserver_as.localminaserver.library.common.MessageType;
import server.yf.com.remotecontrolserver_as.localminaserver.library.message.BaseMessage;
import server.yf.com.remotecontrolserver_as.localminaserver.library.message.CmdMessage;


/**
 * Created by wuhuai on 2016/10/25 .
 * ;
 */

public class IoServerHandler extends IoHandlerAdapter {

    public static final String BOTH_IDLE_TIMES = "Both_Idle_Times";

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        System.out.println("IoServerHandler 本地 mina 服务发生异常：" + cause.toString());
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String datetime = sdf.format(new Date());
        CommonConstant.LINE_TYPE=1;
        BaseMessage baseMessage = (BaseMessage) message;
        baseMessage.setTime(datetime);
        String dataType = baseMessage.getMessageType();
        switch (dataType) {
            case MessageType.MESSAGE_CMD:
                CmdMessage cmdMessage = (CmdMessage) baseMessage;
                LocalMinaCmdManager.getInstance().disposeCmd(cmdMessage);
                break;
            case MessageType.MESSAGE_FILE:
                break;
            case MessageType.MESSAGE_TEXT:
                break;
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        System.out.println("IoServerHandler 服务器发送消息： {" + message + "}");
    }

    @Override
    public void sessionClosed(final IoSession session) throws Exception {
        System.out.println( "IoServerHandler 关闭当前session：{" + session.getId() + "}#{" + session.getRemoteAddress() + "}");
        CloseFuture closeFuture = session.closeOnFlush();
        closeFuture.addListener(new IoFutureListener<IoFuture>() {
            public void operationComplete(IoFuture future) {
                if (future instanceof CloseFuture) {
                    ((CloseFuture) future).setClosed();
                    System.out.println( "IoServerHandler sessionClosed CloseFuture setClosed-->{" + future.getSession().getId() + "}");
                }
            }
        });
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("IoServerHandler 创建一个新连接：{" + session.getRemoteAddress() + "}");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("IoServerHandler 当前连接{" + session.getRemoteAddress() + "}处于空闲状态：{" + status + "}");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("IoServerHandler 打开一个session：{" + session.getId() + "}#{" + session.getBothIdleCount() + "}");
    }

}
