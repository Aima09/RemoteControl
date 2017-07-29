package com.yf.remotecontrolclient;

public class CommonConstant {
    /**
     * 存储连接状态的key
     */
    public static final String CONNECTION_KEY = "connection_key";
    /**
     * 存储ip的key
     */
    public static final String IP_KEY = "ip";

    /**
     * 鼠标模式的key
     */
    public static final String MOUSE_MODE_KEY = "mouse_mode_key";

    /**
     * 浏览url
     */
    @Deprecated
    public static final String BROWSER_URL_KEY = "browser_url_key";

    /**
     * 浏览器网站地址ListJson的key
     */
    public static final String BROWSER_WEBSITELIST_JSON = "browser_websiteList_json";

    //广播监听者的action


    /**
     * baseActivity的广播
     */
    public static final String BASEACTIVITY_MBASEACTIVITYBROADCASTRECEIVER = "baseActivity_mBaseActivitybroadcastReceiver";
    /**
     * 1为本地类型(局域网)
     * 2为互联网类型（www通过服务器转发）
     */
    public static int LINE_TYPE = CommonConstant.LINE_TYPE_REMOTE;
    public static final int LINE_TYPE_LOCAL = 1;
    public static final int LINE_TYPE_REMOTE = 2;

}