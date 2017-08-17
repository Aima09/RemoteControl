package com.yf.remotecontrolclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yf.remotecontrolclient.CommonConstant;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.adapt.WebsiteAdapter;
import com.yf.remotecontrolclient.domain.OpenBrower;
import com.yf.remotecontrolclient.domain.Website;
import com.yf.remotecontrolclient.domain.WebsiteList;
import com.yf.remotecontrolclient.service.BrowserBussinessService;
import com.yf.remotecontrolclient.service.imp.BrowserBusinessServiceImpl;
import com.yf.remotecontrolclient.util.JsonAssistant;
import com.yf.remotecontrolclient.util.SpUtil;


public class BrowserActivity extends BaseActivity implements AdapterView.OnItemLongClickListener {
    private String TAG="BrowserActivity";
    private EditText editText;
    private Button btn;
    BrowserBussinessService browserBussinessService;
    private RadioButton radioButton;

    private Button btnSbkz, btnSrwz;

    private ImageView imageView;
    private GridView gridView;
    private WebsiteAdapter mAdapter;
    private boolean isShowDelete = false;
    WebsiteList websiteList = new WebsiteList();
    JsonAssistant jsonAssistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);
        jsonAssistant = new JsonAssistant();
        browserBussinessService = new BrowserBusinessServiceImpl();
        initspinner();
        imageView = (ImageView) findViewById(R.id.iv_website_add);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        WebsiteAddActivity.class);
                startActivity(intent);
            }
        });

        gridView = (GridView) findViewById(R.id.gv_website);
        gridView.setOnItemLongClickListener(this);//长按事件监听
        //查
        websiteList=query();
        Log.i(TAG,"查询"+websiteList.getWebsites().size());
        mAdapter = new WebsiteAdapter(BrowserActivity.this, websiteList);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isShowDelete) {
                    delete(position);//删除选中项
                    mAdapter = new WebsiteAdapter(BrowserActivity.this, websiteList);//重新绑定一次adapter
                    gridView.setAdapter(mAdapter);
                } else {
                    websiteList.getWebsites().get(position).setDate(new Date());
                    SpUtil.putString(getApplication(), CommonConstant.BROWSER_WEBSITELIST_JSON, jsonAssistant.createWebsite(websiteList));
                    String url1 = websiteList.getWebsites().get(position).getUrl();
                    OpenBrower openBrower1 = new OpenBrower();
                    openBrower1.setCmd("BSopenBrower");
                    openBrower1.setUrl(url1);
                    browserBussinessService.senBsOpenBrowser(openBrower1);
                }
            }
        });
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        if (isShowDelete) {
            isShowDelete = false;
        } else {
            isShowDelete = true;
            mAdapter.setIsShowDelete(isShowDelete);
        }
        Log.i("------>", "进来了没");

        mAdapter.setIsShowDelete(isShowDelete);//setIsShowDelete()方法用于传递isShowDelete值

        return true;
    }

    @Override
    protected void onResume() {
        websiteList=query();
        gridView = (GridView) findViewById(R.id.gv_website);
        gridView.setOnItemLongClickListener(this);//长按事件监听
        mAdapter = new WebsiteAdapter(BrowserActivity.this, websiteList);
        gridView.setAdapter(mAdapter);
        super.onResume();
    }

    private void delete(int position) {//删除选中项方法
        if (isShowDelete) {
            Log.i(TAG,"isShowDelete="+isShowDelete);
            Log.i(TAG,"position="+position);
            if(position==0||position==1){
                Toast.makeText(getApplicationContext(),"无法删除",Toast.LENGTH_LONG).show();
                isShowDelete = false;
                return;
            }
            websiteList.getWebsites().remove(position);
            isShowDelete = false;
        }//写回数据
        update(websiteList);
    }


    private WebsiteList query(){
        String wjsong = SpUtil.getString(this, CommonConstant.BROWSER_WEBSITELIST_JSON, "");
        websiteList.setWebsites(new ArrayList<Website>());
        if (!TextUtils.isEmpty(wjsong)) {
            websiteList = jsonAssistant.paseWebsiteList(wjsong);
        }
        List<Website> websites=websiteList.getWebsites();
        boolean b=false;//有百度
        boolean b1=false;//有华尔思官网
        if(websites.size()<2){
            Website website1=new Website();
            website1.setUrl("https://www.baidu.com");
            website1.setDate(new Date());
            websites.add(0,website1);
            Website website2=new Website();
            website2.setUrl("http://www.fhwise.com");
            website2.setDate(new Date());
            websites.add(0,website2);
            websiteList.setWebsites(websites);
            update(websiteList);
        }
        return websiteList;
    }
    private void update(WebsiteList websiteList){
        SpUtil.putString(getApplication(), CommonConstant.BROWSER_WEBSITELIST_JSON, jsonAssistant.createWebsite(websiteList));
    }
}
