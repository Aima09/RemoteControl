package com.yf.remotecontrolclient.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.intercom.InterService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class IntercomActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "IntercomActivity";

    private static final int RC_AUDIO_RECORD_PERM = 123;

    @AfterPermissionGranted(RC_AUDIO_RECORD_PERM)
    public void audioRecordTask() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.RECORD_AUDIO)) {
            initAudio();
        } else {
            EasyPermissions.requestPermissions(this, "get permissions",
                    RC_AUDIO_RECORD_PERM, Manifest.permission.RECORD_AUDIO);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        initAudio();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
    }

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
        audioRecordTask();
    }

    private void initAudio() {
        // 启动Service
        Log.d(TAG, "--------->>>>>>>>>>>> initAudio");
        Intent intent = new Intent(this, InterService.class);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, InterService.class));
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
