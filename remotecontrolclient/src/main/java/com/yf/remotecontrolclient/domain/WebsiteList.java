package com.yf.remotecontrolclient.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sujuntao on 2017/6/23.
 */

public class WebsiteList implements Serializable {
    private List<Website> Websites;

    public List<Website> getWebsites() {
        return Websites;
    }

    public void setWebsites(List<Website> websites) {
        Websites = websites;
    }
}
