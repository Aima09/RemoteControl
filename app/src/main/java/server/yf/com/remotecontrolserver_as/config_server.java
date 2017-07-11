package server.yf.com.remotecontrolserver_as;


import android.util.Log;

public class config_server {
	public static final String TAG="config_server";
	public static boolean isMymachine(){
//		Log.i(TAG,"DEVICE="+android.os.Build.DEVICE);
		return "BM206".equals(android.os.Build.DEVICE);
	}
}
