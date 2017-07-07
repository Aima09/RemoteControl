package server.yf.com.remotecontrolserver_as.LanMina.library.message;

import android.util.Base64;

import java.io.File;
import java.io.IOException;

import server.yf.com.remotecontrolserver_as.LanMina.library.common.FileHelper;

public class FileMessage extends BaseMessage {

    private FileBean fileBean;

    public FileMessage(String messageType) {
        super(messageType);
    }

    public FileMessage(String messageType, FileBean fileBean) {
        super(messageType);
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
                "fileBean=" + fileBean +
                '}';
    }

    public static class FileBean {
        private String senderId = "";         // 信息发送端
        private String receiverId = "";       // 接收端ID号
        private String fileName = "";
        private int fileSize = 0;
        private byte[] fileContent;

        public FileBean() {
        }

        public FileBean(String filePath) {
            File file = new File(filePath);
            FileHelper helper = new FileHelper();
            fileName = file.getName();
            fileSize = (int) file.length();
            try {
                fileContent = helper.getContent(file);
                String a = Base64.encodeToString(fileContent,Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
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
            return "senderId=" + senderId +
                    ",receiverId=" + receiverId +
                    ",fileName=" + fileName+
                    ",fileSize=" + fileSize;
        }
    }
}
