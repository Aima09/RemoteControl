package com.yf.remotecontrolclient.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.adapt.FileShowListAdapter;
import com.yf.remotecontrolclient.domain.File;
import com.yf.remotecontrolclient.domain.FileShowList;
import com.yf.remotecontrolclient.domain.OpenFile;
import com.yf.remotecontrolclient.service.ZyglqBusinessService;
import com.yf.remotecontrolclient.service.imp.FileBusinessServiceImpl;
import com.yf.remotecontrolclient.service.imp.ZyglqBusinessServiceImpl;

public class FileShowListActivity extends BaseActivity {
    private ZyglqBusinessService zyglqBusinessService;
    private ListView lvFileList;
    private FileShowListAdapter showListAdapter;
    private List<File> files = new ArrayList<File>();
    int total;
    public static String MBROADCASTRECEIVER = "com.yf.client.activity.FileShowListActivity.mBroadcastReceiver";
    private FileShowList fileShowList;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String cmd = intent.getStringExtra(FileBusinessServiceImpl.CMD);
            if (cmd.equals("BSfileShowList")) {
                fileShowList = (FileShowList) intent
                        .getSerializableExtra("fileShowList");
                if (fileShowList == null) {
                    return;
                }
                total = fileShowList.getTotal();
                List<File> list = fileShowList.getFiles();
                files.addAll(list);
                Message message = Message.obtain();
                handler.sendEmptyMessage(0);
            }
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    showListAdapter.notifyDataSetChanged();
                    if (fileShowList == null) {
                        return;
                    }
                    if (files.size() <= total) {
                        FileShowList fileShowList = new FileShowList();
                        fileShowList.setCmd("BSfileShowList");
                        fileShowList.setPageIndex(files.size());
                        fileShowList.setPageSize(2);
                        zyglqBusinessService.sendBsFileShowList(fileShowList);
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_show_list);
        initspinner();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MBROADCASTRECEIVER);
        registerReceiver(mBroadcastReceiver, filter);
        lvFileList = (ListView) findViewById(R.id.lv_file_list);
        zyglqBusinessService = new ZyglqBusinessServiceImpl();
        showListAdapter = new FileShowListAdapter();
        showListAdapter.setFiles(files);
        showListAdapter.setImagePosition(getIntent().getIntExtra("imagePosition", 0));
        lvFileList.setAdapter(showListAdapter);
        //请求显示文件列表
        FileShowList fileShowList = new FileShowList();
        fileShowList.setCmd("BSfileShowList");
        fileShowList.setPageIndex(0);
        fileShowList.setPageSize(2);
        zyglqBusinessService.sendBsFileShowList(fileShowList);

        lvFileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                OpenFile openFile = new OpenFile();
                openFile.setCmd("BSopenFile");
                openFile.setFileId(position);
                zyglqBusinessService.sendBsOpenFile(openFile);
            }
        });

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        File file = new File();
        //finish IFileExplorer的CommonActivity
        file.setCmd("BSfinishIFileExplorerCommonActivity");
        zyglqBusinessService.sendBSfinishIFileExplorerCommonActivity(file);
    }

}
