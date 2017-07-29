package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class Video implements Serializable {
    private int videoid;
    private String videoname;
    private String signer;
    private int duration;

    public int getVideoid() {
        return videoid;
    }

    public void setVideoid(int videoid) {
        this.videoid = videoid;
    }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Video [videoid=" + videoid + ", videoname=" + videoname
                + ", signer=" + signer + ", duration=" + duration + "]";
    }
}
