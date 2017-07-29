package com.yf.remotecontrolclient.service;


import java.io.Serializable;

import com.yf.remotecontrolclient.domain.Action;
import com.yf.remotecontrolclient.domain.Boot;
import com.yf.remotecontrolclient.domain.Gateway;
import com.yf.remotecontrolclient.domain.Palpitation;
import com.yf.remotecontrolclient.domain.Position;
import com.yf.remotecontrolclient.domain.Writer;

public interface MouseBusinessService extends Serializable {
    //udp收到信息的广播
    public final static String DAO_UDP_UDPSERVER = "com.yf.client.dao.udp.UDPServer";
    //tcp收到信息的广播
    public final static String DAO_TCPIP_TCPIPSERVER = "com.yf.client.dao.tcpip.TCPIPServer";

    /**
     * 找到设备之后 回应(udp)
     */
    public void echoEquipment(Gateway gateway);

    /**
     * 连接设备（tcp）
     *
     * @param Boot
     */
    public void linkEquipment(Boot boot);

    /**
     * @param position
     */
    //@Deprecated
    public void sendPosition(Position position);

    /**
     * 发送动作（鼠标或键盘的一些动作）
     */
    public void sendAction(Action action);


    /**
     * 发送心跳
     *
     * @param Palpitation
     */
    public void sendPalpitation(Palpitation palpitation);


    /**
     * 发送writer
     *
     * @param writer
     */
    public void sendWriter(Writer writer);

}
