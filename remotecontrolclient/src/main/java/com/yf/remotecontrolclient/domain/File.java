package com.yf.remotecontrolclient.domain;

import java.io.Serializable;

public class File implements Serializable {
    private String cmd;
    private Integer id;
    private String fileName;
    private String filePath;

    public File() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public File(String cmd, String fileName, String filePath) {
        super();
        this.cmd = cmd;
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
