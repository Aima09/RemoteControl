package com.yf.remotecontrolclient.minaclient.tcp;

import org.apache.mina.core.session.IoSession;

/**
 * Created by wuhuai on 2017/6/23 .
 * ;
 */

public interface MinaServerController {
    void send(Object message);
    void getSessionSend(Object message);
}
