package server.yf.com.remotecontrolserver_as.localminaserver;


import server.yf.com.remotecontrolserver_as.localminaserver.library.encoderdecoder.MessageCodecFactory;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;

/**
 * Created by sujuntao on 2017/7/1.
 * ;
 */

public class LocalMinaSocketAcceptor {

    private NioSocketAcceptor acceptor;
    private InetSocketAddress socketAddress;

    public boolean start() {
        if (null == acceptor) {
            acceptor = new NioSocketAcceptor();
            //自己地址
            socketAddress = new InetSocketAddress(MouseService.equipment.getIp(), 18888);
            acceptor.getSessionConfig().setReadBufferSize(2048 * 10);
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
            acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageCodecFactory()));
            acceptor.setHandler(new IoServerHandler());
            acceptor.setReuseAddress(true);
        }
        if (isStart()){
            System.out.println("本地服务已启动");
            return true;
        } else {
            System.out.println("本地服务未启动");
            return bind();
        }
    }

    private boolean bind(){
        try {
            acceptor.bind(socketAddress);
            System.out.println("启动本地服务成功");
            return true;
        } catch (IOException e) {
            System.out.println("启动本地服务失败");
            e.printStackTrace();
            return false;
        }
    }

    public boolean isStart(){
        return null != acceptor && acceptor.isActive();
    }

    /*
    * 本地服务在收到手机本地控制时，应该反馈信息到所有连线的客户端
    * */
    public void send(Object msg){
        Collection<IoSession> sessions = acceptor.getManagedSessions().values();
        List<IoSession> ioSessions = new ArrayList<>(sessions);
        for (IoSession session : ioSessions) {
            session.write(msg);
        }
    }

    public void close() {
        try {
            if (null != acceptor){
                acceptor.unbind();
                acceptor.dispose();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
