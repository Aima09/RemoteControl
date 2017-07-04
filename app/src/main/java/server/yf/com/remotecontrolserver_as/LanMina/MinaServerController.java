package server.yf.com.remotecontrolserver_as.LanMina;

/**
 * Created by sujuntao on 2017/7/1.
 */

public interface MinaServerController {
    void send(Object message);
    void start();
    void close();
}
