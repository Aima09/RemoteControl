package com.yf.remotecontrolclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.intercom.InterService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntercomActivity extends BaseActivity {


    @BindView(R.id.start_speak) Button startSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intercom);
        ButterKnife.bind(this);

        startSpeak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startSpeak.setText("正在对讲");
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        startSpeak.setText("按下对讲");
                        stopRecord();
                        break;
                }
                return false;
            }
        });
    }

    private void startRecord() {
        Intent intent = new Intent(this, InterService.class);
        intent.putExtra("record", true);
        startService(intent);
    }

    private void stopRecord() {
        Intent intent = new Intent(this, InterService.class);
        intent.putExtra("record", false);
        startService(intent);
    }
}
