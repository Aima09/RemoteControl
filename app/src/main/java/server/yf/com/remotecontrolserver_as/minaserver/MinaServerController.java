package server.yf.com.remotecontrolserver_as.minaserver;

/**
 * Created by wuhuai on 2017/6/23 .
 * ;
 */

public interface MinaServerController {
    void send(Object message);
    void start();
    void close();
}
