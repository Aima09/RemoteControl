package com.yf.remotecontrolserver.domain;

import java.io.Serializable;

/**
 * 这个类封装了心跳
 * @author sujuntao
 *
 */
public class Palpitation implements Serializable{
	private String cmd;
	private String ip;
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
}

