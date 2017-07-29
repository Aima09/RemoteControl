package com.yf.remotecontrolclient.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.yf.remotecontrolclient.CommonConstant;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.minaclient.tcp.RemoteServerManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sujuntao on 2017/7/20.
 */

public class SettingsActivity extends BaseActivity {


    @BindView(R.id.sc_isremot)
    Switch scIsremot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initspinner();
        scIsremot.setChecked(CommonConstant.LINE_TYPE == CommonConstant.LINE_TYPE_REMOTE);
        scIsremot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CommonConstant.LINE_TYPE = CommonConstant.LINE_TYPE_REMOTE;
                } else {
                    CommonConstant.LINE_TYPE = CommonConstant.LINE_TYPE_LOCAL;
                }
                RemoteServerManager.getInstance().startRemoteServer();
            }
        });
    }
}
