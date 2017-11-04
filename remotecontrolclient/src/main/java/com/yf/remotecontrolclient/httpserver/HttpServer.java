package com.yf.remotecontrolclient.httpserver;

import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
        Map<String, String> headers = session.getHeaders();
        String range = null;
        for (String key : headers.keySet()) {
            // Log.e(TAG, key + ":" + headers.get(key) + ", val:" + headers.get(key));
            if ("range".equals(key)) {
                range = headers.get(key); // 读取range header，包含要返回的媒体流位置等信息。
            }
        }
        /*for (Map.Entry<String, String> entry : headers.entrySet()) {
            Log.i("HttpServer", "resq:key = " + entry.getKey() + ",value = " + entry.getValue());
        }*/

        if (mp.get("method") != null) {
            return reaposeDow((String) mp.get("path"));
        }
        return responseVideoStream(session, (String) mp.get("path"), range);
    }

    private Response reaposeDow(String path) {
        try {
            File file = new File(URLDecoder.decode(path, "UTF-8"));String mimeType = null;
            String fileName = file.getName();
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            String m = "application/octet-stream";
           // Response response = newChunkedResponse(Response.Status.CREATED, m, new FileInputStream(file));
            //Content-Disposition	form-data; name="attachment"; filename="1508893759230华尔思最新485通用串行控制通信协议.docx"
            Response response=newFixedLengthResponse(Response.Status.CREATED, m, new FileInputStream(file),file.length());
            /*Log.i("HttpServer","resp:Content-Disposition = "+response.getHeader("Content-Disposition"));
            Log.i("HttpServer","resp:Content-Length = "+response.getHeader("Content-Length"));
            Log.i("HttpServer","resp:Content-Disposition = "+response.getHeader("Content-Disposition"));
            Log.i("HttpServer","resp:Date = "+response.getHeader("Date"));*/
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return newFixedLengthResponse("500");
        }
    }


    public Response responseVideoStream(IHTTPSession session, String path, String rangeHeader) {
        FileInputStream fis = null;
        File file = null;
        NanoHTTPD.Response.IStatus iStatus = null;
        try {
            if (rangeHeader == null) {
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

                String fileName = file.getName();
                String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                String mime = null;
                if (prefix.equals("mp3")) {
//                mime="audio/*";//"audio/mpeg";
                    mime = "audio/mpeg";
                } else if (prefix.equals("mp4")) {
//                mime= "video/*";//"video/mp4";
                    mime = "video/mp4";
                } else {
                    throw new RuntimeException();
                }
                Response res = newFixedLengthResponse(iStatus, mime, fis, fis.available());
                res.setChunkedTransfer(true);
                res.setGzipEncoding(true);
                return res;

            } else {
                String rangeValue = rangeHeader.trim().substring("bytes=".length());
                file = new File(URLDecoder.decode(path, "UTF-8"));
                long fileLength = file.length();
                long start, end;
                if (rangeValue.startsWith("-")) {
                    end = fileLength - 1;
                    start = fileLength - 1
                            - Long.parseLong(rangeValue.substring("-".length()));
                } else {
                    String[] range = rangeValue.split("-");
                    start = Long.parseLong(range[0]);
                    end = range.length > 1 ? Long.parseLong(range[1])
                            : fileLength - 1;
                }
                if (end > fileLength - 1) {
                    end = fileLength - 1;
                }

                InputStream fileInputStream;
                if (start <= end) {
                    long contentLength = end - start + 1;
                    fileInputStream = new FileInputStream(file);
                    fileInputStream.skip(start); //将媒体流跳转到start处，然后返回


                    String fileName = file.getName();
                    String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    String mimeType = null;
                    if (prefix.equals("mp3")) {
//                mimeType="audio/*";//"audio/mpeg";
                        mimeType = "audio/mpeg";
                    } else if (prefix.equals("mp4")) {
//                mimeType= "video/*";//"video/mp4";
                        mimeType = "video/mp4";
                    } else {
                        throw new RuntimeException();
                    }

                    Response response = newChunkedResponse(Response.Status.PARTIAL_CONTENT, mimeType, fileInputStream);
                    response.addHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
                    response.addHeader("Content-Length", contentLength + "");
                    return response;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("HttpServer", "error");
        }
        return newFixedLengthResponse("500");
    }
}