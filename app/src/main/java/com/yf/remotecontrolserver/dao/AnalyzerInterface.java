package com.yf.remotecontrolserver.dao;
/**
 * 此类调用解析数据
 * 并分发
 * @author sujuntao
 * 
 */
public interface AnalyzerInterface {
	/**
	 * 所有的数据的中转
	 * @param buffer
	 */
	public void analy(byte[] buffer,String receiverId);
}
