package server.yf.com.remotecontrolserver_as.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import server.yf.com.remotecontrolserver_as.service.DownloadBusinessService;
import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;
import server.yf.com.remotecontrolserver_as.util.ToastUtil;

public class MainActivity extends Activity {
	private DownloadBusinessService downloadBusinessService;
	/**
	 * 更新新版本的状态码
	 */
	public static final int UPDATE_VERSION = 100;
	/**
	 * 进入应用程序主界面状态码
	 */
	public static final int ENTER_HOME = 101;
	
	/**
	 * url地址出错状态码
	 */
	public static final int URL_ERROR = 102;
	public static final int IO_ERROR = 103;
	public static final int JSON_ERROR = 104;
	private Handler mHandler = new Handler(){
		@Override
		//alt+ctrl+向下箭头,向下拷贝相同代码
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_VERSION:
				//弹出对话框,提示用户更新
				downloadBusinessService.showUpdateDialog();
				break;
			case ENTER_HOME:
				//进入应用程序主界面,activity跳转过程
//				enterHome();
				break;
			case URL_ERROR:
//				ToastUtil.show(getApplicationContext(), "url异常");
//				enterHome();
				break;
			case IO_ERROR:
				ToastUtil.show(getApplicationContext(), "读取异常");
//				enterHome();
				break;
			case JSON_ERROR:
				ToastUtil.show(getApplicationContext(), "json解析异常");
//				enterHome();
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(MainActivity.this, MouseService.class);
		startService(intent);
//		this.downloadBusinessService=new DownloadBusinessServiceImpl(this,mHandler);
		
	    finish();
	}
	
}