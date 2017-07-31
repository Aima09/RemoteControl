package com.yf.minalibrary.message;

import com.yf.minalibrary.common.FileHelper;

import java.io.File;
import java.io.IOException;

public class FileMessage extends BaseMessage {

    private FileBean fileBean;

    public FileMessage(String senderId, String receiverId, String messageType, FileBean fileBean) {
        super(senderId, receiverId, messageType);
        this.fileBean = fileBean;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    @Override public String toString() {
        return "FileMessage{" +
                "senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", messageType='" + messageType + '\'' +
                ", time='" + time + '\'' +
                ", fileBean=" + fileBean +
                '}';
    }

    public static class FileBean {
        private String fileName = "";
        private int fileSize = 0;
        private byte[] fileContent;

        public FileBean(String filePath) {
            File file = new File(filePath);
            if (file.exists()){
                FileHelper helper = new FileHelper();
                fileName = file.getName();
                fileSize = (int) file.length();
                try {
                    fileContent = helper.getContent(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public FileBean(File file) {
            if (file.exists()){
                FileHelper helper = new FileHelper();
                fileName = file.getName();
                fileSize = (int) file.length();
                try {
                    fileContent = helper.getContent(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

        @Override public String toString() {
            return "TextBean{" +
                    ", fileName='" + fileName + '\'' +
                    ", fileSize=" + fileSize +
                    '}';
        }
    }
}
