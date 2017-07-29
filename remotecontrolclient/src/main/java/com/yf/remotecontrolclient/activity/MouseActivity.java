package com.yf.remotecontrolclient.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.CommonConstant;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.domain.Action;
import com.yf.remotecontrolclient.domain.Boot;
import com.yf.remotecontrolclient.service.MouseBusinessService;
import com.yf.remotecontrolclient.service.imp.MouseBusinessServiceImpl;
import com.yf.remotecontrolclient.util.BitmapUtil;
import com.yf.remotecontrolclient.util.SpUtil;

public class MouseActivity extends BaseActivity {
    //	protected static final String TAG = "MainActivity";
    // sp存储的ip key值
    private static ImageView view;
    Bitmap bmp;
    Boot boot;
    float x;
    float y;
    // move距离
    float mx = 0;
    float my = 0;
    boolean isMove = false;
    Action act;
    StringBuilder builder;
    int i = 1;
    private static TextView textView;
    public MouseBusinessServiceImpl mouseBusinessService;

    //remote_left  remote_right
    Button remoteLeft, remoteRight;
    // 点击事件
    Action action;

    public void click(View v) {
        action = new Action();
        switch (v.getId()) {
            case R.id.remote_left:// 模式
                action.setCmd("mode");
                action.setData("0");
                SpUtil.putString(App.getAppContext(), CommonConstant.MOUSE_MODE_KEY, "0");
                flushMouseModeUI();
                break;
            case R.id.remote_right:
                action.setCmd("mode");
                action.setData("1");
                SpUtil.putString(App.getAppContext(), CommonConstant.MOUSE_MODE_KEY, "1");
                flushMouseModeUI();
                break;
            case R.id.remote_back:
                action.setCmd("KEY");
                action.setData(String.valueOf(KeyEvent.KEYCODE_DPAD_LEFT));
                break;
            case R.id.remote_forward:
                action.setCmd("KEY");
                action.setData(String.valueOf(KeyEvent.KEYCODE_DPAD_RIGHT));
                break;
            case R.id.remote_home:
                action.setCmd("home");
                action.setData("");
                break;
            case R.id.remote_windows:
                action.setCmd("KEY");
                action.setData(String.valueOf(KeyEvent.KEYCODE_BACK));
                break;
            case R.id.remote_ok:
                action.setCmd("KEY");
                action.setData(String.valueOf(KeyEvent.KEYCODE_ENTER));
                break;
            case R.id.choose_room:
                startActivity(new Intent(this, ChooseRoomActivity.class));
                return;
        }
        mouseBusinessService.sendAction(action);
    }

    private void flushMouseModeUI() {
        String mouseMode = SpUtil.getString(App.getAppContext(), CommonConstant.MOUSE_MODE_KEY, "0");
        if (mouseMode.equals("0")) {
            remoteLeft.setBackgroundResource(R.drawable.btn_remote_lefty);
            remoteRight.setBackgroundResource(R.drawable.btn_remote_right);
        } else if (mouseMode.equals("1")) {
            remoteLeft.setBackgroundResource(R.drawable.btn_remote_left);
            remoteRight.setBackgroundResource(R.drawable.btn_remote_righty);
        }
    }

    @Override
    protected void onDestroy() {
        if (!bmp.isRecycled()) {
            bmp.recycle();   //回收图片所占的内存
            System.gc();  //提醒系统及时回收
        }
        super.onDestroy();
    }

    public static MouseBusinessService ms;

    public static MouseBusinessService getMouseBusinessService() {
        return ms;
    }

    public static MainActivity activity;

    public static MainActivity getMainActivity() {
        return activity;
    }

    // 鼠标操作位置监听
    public void mouseWeizi() {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                int action = ev.getAction();
                act = new Action();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) ev.getX();
                        y = (int) ev.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isMove = true;
                        x = (int) ev.getX();
                        y = (int) ev.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        isMove = true;
                        x = (int) ev.getX();
                        y = (int) ev.getY();
                        break;
                }
                act.setCmd("move");
                builder = new StringBuilder();
                builder.append((int) (x * 1.2)).append(":");
                builder.append((int) (y * 1.2)).append(":");
                builder.append(ev.getAction());
                act.setData(builder.toString());
                mouseBusinessService.sendAction(act);
                return true;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initspinner();
        initView();
        mouseBusinessService = new MouseBusinessServiceImpl();
        remoteLeft = (Button) findViewById(R.id.remote_left);
        remoteRight = (Button) findViewById(R.id.remote_right);

        SpUtil.putString(App.getAppContext(), CommonConstant.MOUSE_MODE_KEY, "0");
        //
        action = new Action();
        action.setCmd("mode");
        action.setData("0");
        mouseBusinessService.sendAction(action);
        flushMouseModeUI();
    }

    private void initView() {
        view = (ImageView) findViewById(R.id.remote_touch_bg);
        int res = R.drawable.remote_touch_bg1;
        bmp = BitmapUtil.readBitMap(this, res);
        view.setBackground(new BitmapDrawable(getResources(), bmp));
        mouseWeizi();
    }
}
