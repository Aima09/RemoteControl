package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

/**
 * Created by sujuntao on 2017/8/15 .
 */

public class GetSongStuatus implements Serializable {
    private String cmd;
    private String playstatus;//播放状态
    private Integer playmode;  //模式
    private Integer playprogress;//进度
    private Long duration;

    @Override public String toString() {
        return "GetSongStuatus{" +
                "cmd='" + cmd + '\'' +
                ", playstatus='" + playstatus + '\'' +
                ", playmode=" + playmode +
                ", playprogress=" + playprogress +
                ", duration=" + duration +
                '}';
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getPlaystatus() {
        return playstatus;
    }

    public void setPlaystatus(String playstatus) {
        this.playstatus = playstatus;
    }

    public Integer getPlaymode() {
        return playmode;
    }

    public void setPlaymode(Integer playmode) {
        this.playmode = playmode;
    }

    public Integer getPlayprogress() {
        return playprogress;
    }

    public void setPlayprogress(Integer playprogress) {
        this.playprogress = playprogress;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
