package com.yf.remotecontrolserver.download;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.yf.remotecontrolserver.R;
import com.yf.remotecontrolserver.common.ui.MainActivity;
import com.yf.remotecontrolserver.util.StreamUtil;

public class DownloadBusinessServiceImpl implements DownloadBusinessService {
    public static final String TAG = "DownloadBusinessServiceImpl";
    private String mVersionDes;
    private int mLocalVersionCode;

    private String mDownloadUrl = "";
    private PackageManager pm;
    private MainActivity activity;
    private Handler mHandler;

    public DownloadBusinessServiceImpl(MainActivity activity, Handler mHandler) {
        this.activity = activity;
        this.mHandler = mHandler;
        initData();
    }

    private void initData() {
        //1,应用版本名称
//		tv_version_name.setText("版本名称:"+getVersionName());
        //检测(本地版本号和服务器版本号比对)是否有更新,如果有更新,提示用户下载(member)
        //2,获取本地版本号
        mLocalVersionCode = getVersionCode();
        //3,获取服务器版本号(客户端发请求,服务端给响应,(json,xml))
        //http://www.oxxx.com/update74.json?key=value  返回200 请求成功,流的方式将数据读取下来
        //json中内容包含:
        /* 更新版本的版本名称
		 * 新版本的描述信息
		 * 服务器版本号
		 * 新版本apk下载地址*/
        checkVersion();
    }

    /**
     * 返回版本号
     *
     * @return 非0 则代表获取成功
     */
    private int getVersionCode() {
        pm = activity.getPackageManager();
        //2,从包的管理者对象中,获取指定包名的基本信息(版本名称,版本号),传0代表获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(activity.getPackageName(), 0);
            //3,获取版本名称
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 弹出对话框,提示用户更新
     */
    @Override
    public void showUpdateDialog() {
        //对话框,是依赖于activity存在的
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //设置左上角图标
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("版本更新");
        //设置描述内容
//		builder.setMessage(mVersionDes);

        //积极按钮,立即更新
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载apk,apk链接地址,downloadUrl
                downloadApk();
            }
        });

        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消对话框,进入主界面
//				enterHome();
            }
        });

        //点击取消事件监听
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //即使用户点击取消,也需要让其进入应用程序主界面
//				enterHome();
                dialog.dismiss();
            }
        });

        builder.show();
    }

    protected void downloadApk() {
        //apk下载链接地址,放置apk的所在路径

        //1,判断sd卡是否可用,是否挂在上
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //2,获取sd路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "RemoteControlServer.apk";
            //3,发送请求,获取apk,并且放置到指定路径
            HttpUtils httpUtils = new HttpUtils();
            //4,发送请求,传递参数(下载地址,下载应用放置位置)
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功(下载过后的放置在sd卡中apk)
                    Log.i(TAG, "下载成功");
                    File file = responseInfo.result;
                    //提示用户安装
                    installApk(file);
                }

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    Log.i(TAG, "下载失败");
                    //下载失败
                }

                //刚刚开始下载方法
                @Override
                public void onStart() {
                    Log.i(TAG, "刚刚开始下载");
                    super.onStart();
                }

                //下载过程中的方法(下载apk总大小,当前的下载位置,是否正在下载)
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    Log.i(TAG, "下载中........");
                    Log.i(TAG, "total = " + total);
                    Log.i(TAG, "current = " + current);
                    super.onLoading(total, current, isUploading);
                }
            });
        }
    }

    private void installApk(File file) {
        //系统应用界面,源码,安装apk入口
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
		/*//文件作为数据源
		intent.setData(Uri.fromFile(file));
		//设置安装的类型
		intent.setType("application/vnd.android.package-archive");*/
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * 检测版本号
     */
    private void checkVersion() {
        new Thread() {
            public void run() {
                //发送请求获取数据,参数则为请求json的链接地址
                //http://192.168.13.99:8080/update74.json	测试阶段不是最优
                //仅限于模拟器访问电脑tomcat
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    //1,封装url地址
                    URL url = new URL("http://139.196.98.226:8080/RemoteControlServer.json");
                    //2,开启一个链接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //3,设置常见请求参数(请求头)

                    //请求超时
                    connection.setConnectTimeout(2000);
                    //读取超时
                    connection.setReadTimeout(2000);

                    //默认就是get请求方式,
//					connection.setRequestMethod("POST");

                    //4,获取请求成功响应码
                    if (connection.getResponseCode() == 200) {
                        //5,以流的形式,将数据获取下来
                        InputStream is = connection.getInputStream();
                        //6,将流转换成字符串(工具类封装)
                        String json = StreamUtil.streamToString(is);
                        Log.i(TAG, json);
                        //7,json解析
                        JSONObject jsonObject = new JSONObject(json);

                        //debug调试,解决问题
                        String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");

                        //日志打印
                        Log.i(TAG, versionName);
                        Log.i(TAG, mVersionDes);
                        Log.i(TAG, versionCode);
                        Log.i(TAG, mDownloadUrl);

                        //8,比对版本号(服务器版本号>本地版本号,提示用户更新)
                        if (mLocalVersionCode < Integer.parseInt(versionCode)) {
                            //提示用户更新,弹出对话框(UI),消息机制
                            msg.what = MainActivity.UPDATE_VERSION;
                        } else {
                            //进入应用程序主界面
                            msg.what = MainActivity.ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = MainActivity.URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = MainActivity.IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = MainActivity.JSON_ERROR;
                } finally {
                    //指定睡眠时间,请求网络的时长超过4秒则不做处理
                    //请求网络的时长小于4秒,强制让其睡眠满4秒钟
                    mHandler.sendMessage(msg);
                }
            }

            ;
        }.start();
    }
}
