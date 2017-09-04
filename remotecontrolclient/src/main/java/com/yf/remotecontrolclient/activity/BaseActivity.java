package com.yf.remotecontrolclient.activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yf.remotecontrolclient.CommonConstant;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.util.SpUtil;

public class BaseActivity extends AppCompatActivity {

    private LinearLayout contentLayout;
    private ImageView chooseRoom;
    private TextView ipaddress;
    private Button remoteBtnBack;
    Handler baseActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ipaddress.setText((String) msg.obj);
        }
    };
    private BroadcastReceiver mBaseActivitybroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//			Log.i(TAG, "广播接收成功");
            String action = intent.getAction();
            Message message = baseActivityHandler.obtainMessage();
            message.obj = intent.getStringExtra("data");
            baseActivityHandler.sendMessage(message);
        }
    };
    private IntentFilter intentFilter;
    private TextView tvTurnSbkz;
    private TextView tvTurnSrwz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
        initTitleBar();
        intentFilter = new IntentFilter();
        intentFilter.addAction(CommonConstant.BASEACTIVITY_MBASEACTIVITYBROADCASTRECEIVER); // 添加要收到的广播
        registerReceiver(mBaseActivitybroadcastReceiver, intentFilter);
        //初始化状态
        Intent intent = new Intent();
        intent.setAction(CommonConstant.BASEACTIVITY_MBASEACTIVITYBROADCASTRECEIVER);
        intent.putExtra("data", SpUtil.getString(this, CommonConstant.CONNECTION_KEY, ""));
        sendBroadcast(intent);
    }

    private void initContentView() {
        ViewGroup content = (ViewGroup) findViewById(android.R.id.content);
        content.removeAllViews();
        contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        content.addView(contentLayout);
        LayoutInflater.from(this).inflate(R.layout.title_bar,
                contentLayout, true);
    }

    public void initTitleBar() {
        chooseRoom = (ImageView) findViewById(R.id.choose_room);
        ipaddress = (TextView) findViewById(R.id.ipaddress);
        remoteBtnBack = (Button) findViewById(R.id.remote_btn_back);
        chooseRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChooseRoomActivity.class));
            }
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        // View customContentView =
        // LayoutInflater.from(this).inflate(layoutResID,null);
        /*
         * this is the same result with View customContentView =
		 * LayoutInflater.from(this).inflate(layoutResID,contentLayout, false);
		 */

        // contentLayout.addView(customContentView,LayoutParams.FILL_PARENT,
        // LayoutParams.FILL_PARENT);
        LayoutInflater.from(this).inflate(layoutResID, contentLayout, true);
    }

    @Override
    public void setContentView(View customContentView) {
        contentLayout.addView(customContentView);
    }

    public void initspinner() {
        final View view1 = findViewById(R.id.my_spinner);
        remoteBtnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (view1.getVisibility() == View.VISIBLE) {
                    view1.setVisibility(View.GONE);
                    remoteBtnBack.setBackgroundResource(R.drawable.public_back_n_right);
                } else {
                    view1.setVisibility(View.VISIBLE);
                    view1.requestFocus();
                    remoteBtnBack.setBackgroundResource(R.drawable.public_back_n_down);
                }
            }
        });

        tvTurnSbkz = (TextView) findViewById(R.id.tv_turn_sbkz);
        tvTurnSbkz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MouseActivity.class));
                view1.setVisibility(View.GONE);
                remoteBtnBack.setBackgroundResource(R.drawable.public_back_n_right);
            }
        });
        tvTurnSrwz = (TextView) findViewById(R.id.tv_turn_srwz);
        tvTurnSrwz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WriteActivity.class));
                view1.setVisibility(View.GONE);
                remoteBtnBack.setBackgroundResource(R.drawable.public_back_n_right);
            }

        });
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mBaseActivitybroadcastReceiver);
        super.onDestroy();
    }
}