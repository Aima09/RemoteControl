package com.yf.minalibrary.common;

import org.apache.mina.core.session.AttributeKey;

/**
 * Created by wuhuai on 2016/11/24 .
 * ;
 */

public class ClientInfo {
    public static final AttributeKey CLIENTINFO = new AttributeKey(ClientInfo.class, "ClientInfo");

    private String id;
    private String name;
    private String psd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPsd() {
        return psd;
    }

    public void setPsd(String psd) {
        this.psd = psd;
    }
}
