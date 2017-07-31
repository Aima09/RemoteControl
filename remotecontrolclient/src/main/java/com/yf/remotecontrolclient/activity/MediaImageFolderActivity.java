package com.yf.remotecontrolclient.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.adapt.ImageFolderAdapter;
import com.yf.remotecontrolclient.domain.ImageFolder;
import com.yf.remotecontrolclient.domain.ImageFolderList;
import com.yf.remotecontrolclient.service.ImageBusinessService;
import com.yf.remotecontrolclient.service.imp.ImageBusinessServiceImpl;

public class MediaImageFolderActivity extends BaseActivity implements
        AdapterView.OnItemClickListener {
    public static String TAG = "MediaImageFolderActivity";
    private ListView listView;
    private ImageFolderAdapter imageAdapter;
    public final static String MBROADCASTRECEIVER = "com.yf.client.activity.MediaImageActivity.image.folder";
    private List<ImageFolder> imageFolders = new ArrayList<ImageFolder>();
    private ImageFolderList imageFolderList;
    private ImageBusinessService imageBusinessService;
    int total;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String cmd = intent.getStringExtra(ImageBusinessServiceImpl.CMD);
            if (cmd.equals("BSgetimageFolderList")) {
                imageFolderList = (ImageFolderList) intent
                        .getSerializableExtra("getimageFolderList");
                if (imageFolderList == null) {
                    return;
                }
                total = imageFolderList.getTotal();
                List<ImageFolder> list = imageFolderList.getImageFolderList();
                imageFolders.addAll(list);
                Message message = Message.obtain();
                handler.sendEmptyMessage(0);
            }else if(cmd.equals("BSopenImageFolder")){
                startActivity(new Intent(getApplicationContext(),
                        MediaImageActivity.class));
            }
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    imageAdapter.notifyDataSetChanged();
                    if (imageFolders == null) {
                        return;
                    }
                    if (imageFolders.size() <= total) {
                        ImageFolderList imageFolderList = new ImageFolderList();
                        imageFolderList.setCmd("BSgetimageFolderList");
                        imageFolderList.setPageSize(2);
                        imageFolderList.setPageIndex(imageFolders.size());
                        imageBusinessService.sendBsgetFolderList(imageFolderList);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_folder_list);
        initspinner();
        listView = (ListView) findViewById(R.id.image_list_view);
        imageAdapter = new ImageFolderAdapter();
        imageAdapter.setImageFolders(imageFolders);
        listView.setAdapter(imageAdapter);

        IntentFilter filter = new IntentFilter();
        filter.addAction(MBROADCASTRECEIVER);
        registerReceiver(mBroadcastReceiver, filter);

        imageBusinessService = new ImageBusinessServiceImpl();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ImageFolder imageFolder=new ImageFolder();
        imageFolder.setCmd("BSopenImageFolder");
        imageFolder.setId(position);
        imageFolder.setName(imageFolders.get(position).getName());
        imageBusinessService.sendBsopenFolder(imageFolder);
    }

    @Override
    protected void onResume() {
        imageFolders.clear();
        Log.i(TAG, "MediaImageFolderActivity.onResume");
        ImageFolderList imageFolderList = new ImageFolderList();
        imageFolderList.setCmd("BSgetimageFolderList");
        imageFolderList.setPageSize(2);
        imageFolderList.setPageIndex(0);
        imageBusinessService.sendBsgetFolderList(imageFolderList);
        listView.setOnItemClickListener(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        imageFolders.clear();
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }
}
