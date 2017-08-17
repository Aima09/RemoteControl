package com.yf.remotecontrolclient.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.yf.remotecontrolclient.CommonConstant;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.minaclient.tcp.RemoteServerManager;
import com.yf.remotecontrolclient.util.SpUtil;

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
        scIsremot.setChecked(SpUtil.getInt(getApplication(),CommonConstant.LINK_TYPE_KEY,CommonConstant.LINE_TYPE_REMOTE) == CommonConstant.LINE_TYPE_REMOTE);
        scIsremot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int linkTyp;
                if (isChecked) {
                    linkTyp= CommonConstant.LINE_TYPE_REMOTE;
                } else {
                    linkTyp = CommonConstant.LINE_TYPE_LOCAL;
                }
                SpUtil.putInt(getApplication(),CommonConstant.LINK_TYPE_KEY,linkTyp);
                RemoteServerManager.getInstance().startRemoteServer();
            }
        });
    }
}
