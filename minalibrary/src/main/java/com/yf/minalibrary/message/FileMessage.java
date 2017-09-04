package com.yf.minalibrary.message;

import java.util.Arrays;

public class FileMessage extends BaseMessage {

    private String fileName = "";
    private int fileSize = 0;
    private byte[] fileContent;
    private String use;

    public FileMessage(String senderId, String receiverId, int messageType, String fileName, int fileSize, byte[] fileContent,String use) {
        super(senderId, receiverId, messageType);
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileContent = fileContent;
        this.use=use;
    }

    @Override public String toString() {
        return "FileMessage{" +
                "fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", fileContent=" + Arrays.toString(fileContent) +
                '}';
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }
}
