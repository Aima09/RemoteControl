package com.yf.remotecontrolclient.activity;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.CommonConstant;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.activity.service.MouseService;
import com.yf.remotecontrolclient.dao.SocketManager;
import com.yf.remotecontrolclient.dao.TcpAnalyzerImpl;
import com.yf.remotecontrolclient.dao.tcpip.TCPIPServer;
import com.yf.remotecontrolclient.domain.Boot;
import com.yf.remotecontrolclient.domain.Equipment;
import com.yf.remotecontrolclient.domain.Palpitation;
import com.yf.remotecontrolclient.httpserver.HttpServer;
import com.yf.remotecontrolclient.intercom.InterService;
import com.yf.remotecontrolclient.minaclient.tcp.RemoteServerManager;
import com.yf.remotecontrolclient.service.imp.MouseBusinessServiceImpl;
import com.yf.remotecontrolclient.util.IpUtil;
import com.yf.remotecontrolclient.util.SpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, EasyPermissions.PermissionCallbacks {
    protected static final String TAG = "QRCodeScanActivity";
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    // 定义图标数组
    private int[] imageRes = {R.drawable.timg, R.drawable.text, R.drawable.btn_music,
            R.drawable.vidio, R.drawable.tk, R.drawable.llq,R.drawable.zyglq, R.drawable.timg,
            R.drawable.timg, R.drawable.setting, R.mipmap.ic_talk, R.mipmap.ic_dueros};//R.drawable.zyglq

    // 定义图标下方的名称数组
    private String[] name = {"鼠标控制", "输入文字", "播放音乐",
            "播放视频", "图库", "浏览器", "资源管理", "本地控制",
            "远程控制", "设置", "对讲","人工智能语音"};//"资源管理"

    public MouseBusinessServiceImpl mouseBusinessService;

    private Boot boot;
    public static Handler handler;
    private Button yckzrk;
    private BroadcastReceiver mMainActivityBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Log.i(TAG, "广播接收成功");
            String action = intent.getAction();
            if (action.equals(MouseBusinessServiceImpl.DAO_UDP_UDPSERVER)) {
                // Log.i(TAG, "udp成功启动");
                String cmd = intent
                        .getStringExtra(MouseBusinessServiceImpl.CMD);
                if (cmd.equals("wlinkwulian")) {

                } else if ("palpitation".equals(cmd)) {
                    // 收到心跳
//					Log.i(TAG, "收到");
                    MouseService.palpitationTime = System.currentTimeMillis();
                    // 返回心跳
                    Palpitation palpitation = new Palpitation();
                    palpitation.setCmd("palpitation");
                    palpitation.setIp(IpUtil.getLocalIpAddress(App
                            .getAppContext()));
                    mouseBusinessService.sendPalpitation(palpitation);
                }
            } else if (action
                    .equals(MouseBusinessServiceImpl.DAO_TCPIP_TCPIPSERVER)) {
                String cmd = intent
                        .getStringExtra(MouseBusinessServiceImpl.CMD);
                // Log.i(TAG, "cmd=" + cmd);
                if ("boot".equals(cmd)) {
                    Boot boot = (Boot) intent.getSerializableExtra("boot");
                    Message message = handler.obtainMessage();
                    message.obj = boot;
                    message.what = 0;
                    handler.sendMessage(message);

                    // 先写回到sp
                    SpUtil.putString(App.getAppContext(),
                            CommonConstant.CONNECTION_KEY, "连接成功");
                    Intent intent2 = new Intent();
                    intent2.setAction(CommonConstant.BASEACTIVITY_MBASEACTIVITYBROADCASTRECEIVER);
                    intent2.putExtra("data", "连接成功");
                    sendBroadcast(intent2);
                    // 检测
                    //SocketManager.getSocketManager().check();
                }
            }
        }
    };

    class MainHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    boot = (Boot) msg.obj;
                    // 监听触摸位置
                    // mouseWeizi();
                    mouseBusinessService.linkEquipment(boot);
                    break;
            }
        }

        ;
    }

    private ArrayList<HashMap<String, Object>> lstImageItem;
    private SimpleAdapter saImageItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        initspinner();
        initData();
        initView();
        initBusiness();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    private void initBusiness() {
        // 再开启新的
        SocketManager.getSocketManager().startUdp();

        mouseBusinessService = new MouseBusinessServiceImpl();
        // 自动连接
        RemoteServerManager.getInstance().startRemoteServer();
        startService(new Intent(App.getAppContext(), MouseService.class));
        startService(new Intent(App.getAppContext(), HttpServer.class));
    }

    private void initData() {
        lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < imageRes.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", imageRes[i]);// 添加图像资源的ID
            map.put("ItemText", name[i]);// 按序号做ItemText
            lstImageItem.add(map);
        }
        saImageItems = new SimpleAdapter(this, lstImageItem,// 数据来源
                R.layout.activity_function_gridview_item,// item的XML实现
                // 动态数组与ImageItem对应的子项
                new String[]{"ItemImage", "ItemText"},
                // ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.img_shoukuan, R.id.txt_shoukuan});
    }

    private void initView() {
        String localLinkAdress=SpUtil.getString(getApplication(),CommonConstant.LOCAL_LINK_ADRESS_KEY,null);
        Equipment eq=new Equipment();
        eq.setIp(localLinkAdress);
        if(!TextUtils.isEmpty(localLinkAdress)){
            MouseService.equipment=eq;
        }
        GridView gridview = (GridView) findViewById(R.id.gridview);
        // 添加并且显示
        gridview.setAdapter(saImageItems);
        // 添加消息处理
        gridview.setOnItemClickListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(MouseBusinessServiceImpl.DAO_UDP_UDPSERVER);
        filter.addAction(MouseBusinessServiceImpl.DAO_TCPIP_TCPIPSERVER);
        // 3.注册广播接收者
        registerReceiver(mMainActivityBroadcastReceiver, filter);
        handler = new MainHandler();
//        startService(new Intent(this,));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        switch (position) {
            case 0:
                Intent intent3 = new Intent(getApplicationContext(),
                        MouseActivity.class);
                startActivity(intent3);
                break;
            case 1:
                Intent intent = new Intent(getApplicationContext(),
                        WriteActivity.class);
                startActivity(intent);
                break;
            case 2:
                Intent intent1 = new Intent(getApplicationContext(),
                        MediaMusicActivity.class);
                startActivity(intent1);
                break;
            case 3:
                Intent intent2 = new Intent(getApplicationContext(),
                        MediaVideoActivity.class);
                startActivity(intent2);
                break;
            case 4:
                Intent intent4 = new Intent(getApplicationContext(),
                        MediaImageFolderActivity.class);
                startActivity(intent4);
                break;
            case 5:
                Intent intent5 = new Intent(getApplicationContext(),
                        BrowserActivity.class);
                startActivity(intent5);
                break;
            /*case 6:
                //设置
                OpenSettings openSettings = new OpenSettings();
                openSettings.setCmd("BSopenSettings");
                SettingsBusinessService settingsBusinessService = new SettingsBusinessServiceImpl();
                settingsBusinessService.senBsOpenSettings(openSettings);
                Intent intent6 = new Intent(getApplicationContext(),
                        MouseActivity.class);
                startActivity(intent6);
                break;*/
            case 6:
                Intent intent7 = new Intent(getApplicationContext(),
                        FileActivity.class);
                startActivity(intent7);
                break;
            case 7://本地控制
                startActivity(new Intent(getApplicationContext(), ChooseRoomActivity.class));
                break;
            case 8:
                //远程控制
                Intent intent9 = new Intent(getApplicationContext(),
                        RemoteDevicesListActivity.class);
                startActivity(intent9);
                break;
            case 9://设置
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case 10:
                startActivity(new Intent(getApplicationContext(), IntercomActivity.class));
                break;
            case 11:
                startActivity(new Intent(getApplicationContext(),
                        DcsOAuthActivity.class));
                break;
        }
    }

    //跳转回主页功能
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // Message msg = handler.obtainMessage();
        // msg.obj = "正在寻找。。";
        // msg.what = 1;
        // handler.sendMessage(msg);
        Intent intent = new Intent();
        intent.setAction(CommonConstant.CONNECTION_KEY);
        intent.putExtra("data", "正在寻找。。");
        sendBroadcast(intent);
        unregisterReceiver(mMainActivityBroadcastReceiver);
        TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment,
                TcpAnalyzerImpl.getInstans()).send(
                "{\"cmd\":\"BSexitnetwork\"}".getBytes());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment,
                TcpAnalyzerImpl.getInstans()).stop();
        MouseService.equipment = new Equipment();
        for (int i = 0; i < ChooseRoomActivity.equipments.size(); i++) {
            if (!ChooseRoomActivity.equipments.get(i).getIp()
                    .equals(MouseService.equipment.getIp())) {
                ChooseRoomActivity.equipments.remove(i);
            }
        }
        SocketManager.iscolle = false;

        stopService(new Intent(App.getAppContext(), MouseService.class));
        stopService(new Intent(this, InterService.class));
        super.onDestroy();
    }
}
