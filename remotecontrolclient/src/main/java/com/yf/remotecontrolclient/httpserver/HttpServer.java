package com.yf.remotecontrolclient.httpserver;

import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by sujuntao on 2017/8/25 .
 */

public class HttpServer extends NanoHTTPD {
    /*这类就是要自定义一些返回值，我在这里定义了700。都属于自定义*/
    public enum Status implements NanoHTTPD.Response.IStatus {
        SWITCH_PROTOCOL(101, "Switching Protocols"),
        NOT_USE_POST(700, "not use post");

        private final int requestStatus;
        private final String description;

        Status(int requestStatus, String description) {
            this.requestStatus = requestStatus;
            this.description = description;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public int getRequestStatus() {
            return 0;
        }
    }

    public HttpServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
//        return newFixedLengthResponse("aaa");
        Map mp = session.getParms();
        return responseVideoStream(session, (String) mp.get("path"));
    }

    public Response responseVideoStream(IHTTPSession session, String path) {
        FileInputStream fis = null;
        File file = null;
        NanoHTTPD.Response.IStatus iStatus = null;
        try {
            file = new File(URLDecoder.decode(path, "UTF-8"));
            iStatus = new NanoHTTPD.Response.IStatus() {
                @Override public String getDescription() {
                    return "ok";
                }

                @Override public int getRequestStatus() {
                    return 200;
                }
            };
            //"/storage/emulated/0/netease/cloudmusic/Music/a1.mp3"
            fis = new FileInputStream(file);
            Log.i("HttpServer", "available" + fis.available() + "");

            return newChunkedResponse(iStatus, file.getName(), fis);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("HttpServer", "error");
        }
        return newFixedLengthResponse("500");
    }
}