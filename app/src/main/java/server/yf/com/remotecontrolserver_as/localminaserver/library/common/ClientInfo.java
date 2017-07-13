package server.yf.com.remotecontrolserver_as.localminaserver.library.common;

import org.apache.mina.core.session.AttributeKey;

/**
 * Created by wuhuai on 2016/11/24 .
 * ;
 */

public class ClientInfo {
    public static final AttributeKey CLIENTINFO = new AttributeKey(ClientInfo.class, "ClientInfo");

    private String id;
    private String clientType;

    public ClientInfo() {
    }

    public ClientInfo(String id, String clientType) {
        this.id = id;
        this.clientType = clientType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
