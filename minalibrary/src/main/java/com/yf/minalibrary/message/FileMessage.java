package com.yf.minalibrary.message;

public class FileMessage extends BaseMessage {

    private String fileName = "";
    private int fileSize = 0;
    private byte[] fileContent;
    private String use;
    //显示用的
    private Integer currenSize;
    public FileMessage(String senderId, String receiverId, int messageType, String fileName, int fileSize, byte[] fileContent,String use) {
        super(senderId, receiverId, messageType);
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileContent = fileContent;
        this.use=use;
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

    public Integer getCurrenSize() {
        return currenSize;
    }

    public void setCurrenSize(Integer currenSize) {
        this.currenSize = currenSize;
    }
}
