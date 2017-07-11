package server.yf.com.remotecontrolserver_as.LanMina;

import android.os.Environment;
import android.util.Log;

import server.yf.com.remotecontrolserver_as.LanMina.library.common.CmdType;
import server.yf.com.remotecontrolserver_as.LanMina.library.common.DeviceType;
import server.yf.com.remotecontrolserver_as.LanMina.library.common.MessageType;
import server.yf.com.remotecontrolserver_as.LanMina.library.message.BaseMessage;
import server.yf.com.remotecontrolserver_as.LanMina.library.message.CmdMessage;
import server.yf.com.remotecontrolserver_as.LanMina.library.message.FileMessage;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;

import server.yf.com.remotecontrolserver_as.CommonConstant;
import server.yf.com.remotecontrolserver_as.dao.TcpAnalyzerImpl;
import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;


/**
 * Created by wuhuai on 2016/10/25 .
 * ;
 */
public class IoServerHandler extends IoHandlerAdapter {
    private final Logger log = LoggerFactory.getLogger(IoServerHandler.class);
    private MinaSocketConnector minaSocketConnector;
    //当前连接的客户端session
    public static IoSession currenSession;
    public IoServerHandler(MinaSocketConnector minaSocketConnector) {
        this.minaSocketConnector = minaSocketConnector;
    }

    /*private void transmit(IoSession session){
        // 拿到所有的客户端Session
        Collection<IoSession> sessions = session.getService().getManagedSessions().values();
        List<IoSession> ioSessions = new ArrayList<>(sessions);
        ioSessions.remove(session);
        // 向注册客户端发送所有在线设备信息
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ioSessions.size(); i++) {
            IoSession ioSession = ioSessions.get(i);
            ClientInfo info = (ClientInfo) ioSession.getAttribute(CLIENTINFO);
            builder.append(info.getName()).append(":").append(info.getId());
            if (i != ioSessions.size() - 1) {
                builder.append(",");
            }
        }
    }*/

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        System.out.println("IoServerHandler 服务器发生异常：");
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
//        session.getService().getManagedSessions();
        CommonConstant.LINE_TYPE=1;
        currenSession=session;
        BaseMessage baseMessage = (BaseMessage) message;
        String dataType = baseMessage.getMessageType();
        switch (dataType) {
            case MessageType.MESSAGE_CMD:
                CmdMessage cmdMessage = (CmdMessage) baseMessage;
                Log.d("LanIoServerHandler", "内容：" + cmdMessage.getCmdBean().getCmdContent());
                TcpAnalyzerImpl.getInstans().analy(cmdMessage.getCmdBean().getCmdContent().getBytes(),null);
//                MinaCmdManager.getInstance().disposeCmd(cmdMessage);
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
        String clientIP = ((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress();
        session.setAttribute("KEY_SESSION_CLIENT_IP", clientIP);
        System.out.println("IoServerHandler 创建一个新连接：{" + session.getRemoteAddress() + "}");
    }
    long time=System.currentTimeMillis();
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("IoServerHandler 当前连接{" + session.getRemoteAddress() + "}处于空闲状态：{" + status + "}");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        minaSocketConnector.setSession(session);
        System.out.println("IoServerHandler 打开一个session：{" + session.getId() + "}#{" + session.getBothIdleCount() + "}");
        currenSession=session;
    }

}
