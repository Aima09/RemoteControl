package com.yf.remotecontrolserver.common.ui;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yf.remotecontrolserver.R;
import com.yf.remotecontrolserver.common.ui.serice.MouseService;
import com.yf.remotecontrolserver.download.DownloadBusinessService;
import com.yf.remotecontrolserver.remoteminaclient.ClientDataDisposeCenter;
import com.yf.remotecontrolserver.remoteminaclient.ClientMinaCmdManager;
import com.yf.remotecontrolserver.remoteminaclient.ClientMinaServer;
import com.yf.remotecontrolserver.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class MainActivity extends Activity {

    @BindView(R.id.qrcode_id) ImageView qrcodeId;
    public static final String ID_HEAD_FLAG = "YF_HUAERSI";
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
    private DownloadBusinessService downloadBusinessService;

    private Handler mHandler = new Handler() {
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Intent intent = new Intent(MainActivity.this, MouseService.class);
        startService(intent);
        qrcodeId.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                stopService(new Intent(MainActivity.this, ClientMinaServer.class));
            }
        });
        ClientMinaCmdManager.getInstance().setMinaCmdCallBack(new ClientMinaCmdManager.MinaCmdCallBack() {
            @Override public void minaCmdCallBack(Object message) {

                String id = "";
                if (message instanceof String) {
                    id = (String) message;
                }
                createQRCode(id);
            }
        });
    }

    @Override protected void onStart() {
        super.onStart();
        createQRCode(ClientDataDisposeCenter.getLocalSenderId());
    }

    String localID;
    private void createQRCode(String id) {
        if (TextUtils.isEmpty(id)){
            return;
        }
        localID = String.format("%s,%s", ID_HEAD_FLAG, id);
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(localID, BGAQRCodeUtil.dp2px(MainActivity.this, 150));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    qrcodeId.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(MainActivity.this, "生成二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}