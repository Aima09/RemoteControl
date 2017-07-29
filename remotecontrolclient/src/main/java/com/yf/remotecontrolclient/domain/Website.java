package com.yf.remotecontrolclient.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by sujuntao on 2017/6/23.
 */

public class Website implements Serializable {
    private String url;
    private Date Date;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }
}
