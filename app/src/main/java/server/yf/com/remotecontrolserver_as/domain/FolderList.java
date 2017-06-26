package server.yf.com.remotecontrolserver_as.domain;

import java.io.Serializable;

public class FolderList implements Serializable{
	private String cmd;
	//文件夹名称
	private String name;
	private Integer fileNumber;
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getFileNumber() {
		return fileNumber;
	}
	public void setFileNumber(Integer fileNumber) {
		this.fileNumber = fileNumber;
	}
}
