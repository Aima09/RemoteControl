package server.yf.com.remotecontrolserver_as.service;


import server.yf.com.remotecontrolserver_as.domain.Boot;
import server.yf.com.remotecontrolserver_as.domain.Equipment;
import server.yf.com.remotecontrolserver_as.domain.Palpitation;

public interface MouseBusinessService {
	//udp收到信息的广播
	public final static String DAO_UDP_UDPSERVER="com.yf.server.dao.udp.UDPServer";
	//tcp收到信息的广播
	public final static String DAO_TCPIP_TCPIPSERVER="com.yf.server.dao.tcpip.TCPIPServer";
	//定义的广播
	public final static String MyInputMethodServiceB="android.inputmethodservice.MyInputMethodService";
	
	 
	//action的一些参数
	public static final String SERVICE_MOUSE = "com.yf.mousekey.SERVICE_MOUSE";
	public static final String MOUSE_key = "MOUSE_key";
	public static final String SERVICE_KEY = "com.yf.mousekey.SERVICE_KEY";
	public static final String KEY_key = "KEY_key";
	
	public static final String MOUSE_MODE = "com.yf.mousekey.MOUSE_MODE";
	public static final String MODE_key = "MODE_key";
	
	/**
	 * 发起连接网关
	 *参数equipment带了自己信息
	 */
	public void echoGateway(Equipment equipment);
	/**
	 * 连接设备（tcp） 主动发送boot
	 * @param Boot
	 */
	public void linkGateway(Boot boot);
	/**
	 * 退出
	 */
	public void exit();
	
	public void move(String movedata);
	
	public void mode(String modedata);
	
	public void key(String keydata);
	
	public void home();
	
	
	/**发送心跳
	 * @param //Palpitation
	 */
	public void sendPalpitation(Palpitation palpitation);
}
