package server.yf.com.remotecontrolserver_as.domain;

import java.io.Serializable;
public class Boot implements Serializable{
	private String cmd;
	private String devid;
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getDevid() {
		return devid;
	}
	public void setDevid(String devid) {
		this.devid = devid;
	}
	@Override
	public String toString() {
		return "Boot [cmd=" + cmd + ", devid=" + devid + "]";
	}
}
