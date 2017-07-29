package com.yf.remotecontrolclient.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;

import com.yf.remotecontrolclient.CommonConstant;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.domain.Website;
import com.yf.remotecontrolclient.domain.WebsiteList;
import com.yf.remotecontrolclient.util.JsonAssistant;
import com.yf.remotecontrolclient.util.SpUtil;

/**
 * Created by sujuntao on 2017/6/23.
 */

public class WebsiteAddActivity extends BaseActivity {
    private JsonAssistant jsonAssistant;
    private EditText etBrowser;
    private Button btnBrowserSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        initspinner();
        jsonAssistant = new JsonAssistant();
        //添加
        etBrowser = (EditText) findViewById(R.id.et_browser);
        etBrowser.setText("http://www.");

        btnBrowserSubmit = (Button) findViewById(R.id.btn_browser_submit);
        btnBrowserSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etBrowser.getText().toString();
                WebsiteList websiteList;
                String jsonString = SpUtil.getString(getApplication(), CommonConstant.BROWSER_WEBSITELIST_JSON, "");
                if (TextUtils.isEmpty(jsonString)) {
                    websiteList = new WebsiteList();
                    websiteList.setWebsites(new ArrayList<Website>());
                } else {
                    websiteList = jsonAssistant.paseWebsiteList(jsonString);
                }
                Website website = new Website();
                website.setUrl(s);
                website.setDate(new Date());
                websiteList.getWebsites().add(website);
                //写回
                SpUtil.putString(getApplication(), CommonConstant.BROWSER_WEBSITELIST_JSON, jsonAssistant.createWebsite(websiteList));
                WebsiteAddActivity.this.finish();
            }
        });
    }
}
