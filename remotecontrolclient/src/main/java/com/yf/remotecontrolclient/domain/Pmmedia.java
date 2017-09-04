package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

/**
 * Created by sujuntao on 2017/8/25 .
 */

public class Pmmedia implements Serializable{
    private String cmd;
    private long id;
    private long duration;
    private long size;
    private String path;
    private String artist;
    private String title;
    private String displayName;
    private long abulmId;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getAbulmId() {
        return abulmId;
    }

    public void setAbulmId(long abulmId) {
        this.abulmId = abulmId;
    }
}
