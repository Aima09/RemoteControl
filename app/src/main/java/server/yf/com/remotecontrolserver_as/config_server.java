package server.yf.com.remotecontrolserver_as;


import android.os.Build;
import android.provider.Settings;
import android.util.Log;

public class config_server {
	public static final String TAG="config_server";
	public static boolean isMymachine(){
//		Log.i(TAG,"DEVICE="+android.os.Build.DEVICE);
//		Log.d(TAG, "Build.SERIAL = " + Build.SERIAL);
		return "BM206".equals(Build.DEVICE);
	}
}
