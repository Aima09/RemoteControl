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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.adapt.FileCategoryAdapter;
import com.yf.remotecontrolclient.domain.FileCategory;
import com.yf.remotecontrolclient.domain.FileCategoryList;
import com.yf.remotecontrolclient.domain.OpenFileCategory;
import com.yf.remotecontrolclient.domain.OpenZyglq;
import com.yf.remotecontrolclient.service.FileBusinessService;
import com.yf.remotecontrolclient.service.ZyglqBusinessService;
import com.yf.remotecontrolclient.service.imp.FileBusinessServiceImpl;
import com.yf.remotecontrolclient.service.imp.ZyglqBusinessServiceImpl;

public class FileActivity extends BaseActivity implements
        AdapterView.OnItemClickListener {
    public static String TAG = "FileActivity";
    private ListView listView;
    private FileCategoryAdapter fileCategoryAdapter;
    public final static String MBROADCASTRECEIVER = "com.yf.client.activity.FileActivity.file";
    private List<FileCategory> fileCategories = new ArrayList<FileCategory>();//列表
    private FileCategoryList fileCategoryList;//文件List
    private FileBusinessService fileBusinessService;//
    int total;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String cmd = intent.getStringExtra(FileBusinessServiceImpl.CMD);
            if (cmd.equals("BSopenZyglq")) {//打开成功
                //请求列表
                fileCategories.clear();
                FileCategoryList fileCategoryList = new FileCategoryList();
                fileCategoryList.setCmd("BSgetfileCategoryList");
                fileCategoryList.setPageIndex(0);
                fileCategoryList.setPageSize(2);
                fileBusinessService.sendBSgetFileCategoryList(fileCategoryList);
                //获取分类列表
            } else if (cmd.equals("BSgetfileCategoryList")) {
                fileCategoryList = (FileCategoryList) intent
                        .getSerializableExtra("fileCategoryList");
                if (fileCategoryList == null) {
                    return;
                }
                total = fileCategoryList.getTotal();
//				Toast.makeText(getApplicationContext(), total+"", Toast.LENGTH_LONG).show();
                List<FileCategory> list = fileCategoryList.getFileCategories();
                fileCategories.addAll(list);
                Message message = Message.obtain();
                handler.sendEmptyMessage(0);
            } else if (cmd.equals("BSgetfile")) {
//				file = (File) intent
//						.getSerializableExtra("BSgetfile");
//				Message message = Message.obtain();
//				handler.sendEmptyMessage(1);
            } else if (cmd.equals("BSopenFileCategory")) {
                Intent intent2 = new Intent(getApplicationContext(), FileShowListActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent2.putExtra("imagePosition", imagePosition);
                startActivity(intent2);
            }
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    fileCategoryAdapter.notifyDataSetChanged();
                    if (fileCategoryList == null) {
                        return;
                    }
                    if (fileCategories.size() <= total) {
                        FileCategoryList fileCategoryList = new FileCategoryList();
                        fileCategoryList.setCmd("BSgetfileCategoryList");
                        fileCategoryList.setPageIndex(fileCategories.size());
                        fileCategoryList.setPageSize(2);
                        fileBusinessService.sendBSgetFileCategoryList(fileCategoryList);
                    }
                    break;
                case 1:
//				textView.setText(file.getFileName());
//				((TextView)findViewById(R.id.id_mainTopBarTextView1)).setText(file.getFileName().replace("/mnt/", ""));
            }
        }
    };
    private TextView textView;
    private Integer imagePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_category_list);
        initspinner();
        textView = (TextView) findViewById(R.id.id_mainTopBarTextView);
        listView = (ListView) findViewById(R.id.lv_file_category_list);
        fileCategoryAdapter = new FileCategoryAdapter();
        fileCategoryAdapter.setFileCategories(fileCategories);
        listView.setAdapter(fileCategoryAdapter);
        listView.setOnItemClickListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MBROADCASTRECEIVER);
        registerReceiver(mBroadcastReceiver, filter);

        //资源管理器
        OpenZyglq openZyglq = new OpenZyglq();
        openZyglq.setCmd("BSopenZyglq");
        ZyglqBusinessService zyglqBusinessService = new ZyglqBusinessServiceImpl();
        zyglqBusinessService.sendBsOpenZyglq(openZyglq);

        fileBusinessService = new FileBusinessServiceImpl();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        imagePosition = position;
        OpenFileCategory openFileCategory = new OpenFileCategory();
        openFileCategory.setCmd("BSopenFileCategory");
        Integer integer = new Integer(position);
        openFileCategory.setFileCategoryId(integer);
        fileBusinessService.sendBSgetFileCategoryList(openFileCategory);
    }

    @Override
    protected void onResume() {
//		imageFolders.clear();
//		Log.i(TAG, "MediaFileActivity.onResume");
//		ImageFolderList imageFolderList = new ImageFolderList();
//		imageFolderList.setCmd("BSgetimageFolderList");
//		imageFolderList.setPageSize(2);
//		imageFolderList.setPageIndex(0);
//		imageBusinessService.sendBsgetFolderList(imageFolderList);
//		listView.setOnItemClickListener(this);
//		fileCategories.clear();
//		OpenZyglq openZyglq=new OpenZyglq();
//		openZyglq.setCmd("BSopenZyglq");
//		ZyglqBusinessService zyglqBusinessService=new ZyglqBusinessServiceImpl();
//		zyglqBusinessService.sendBsOpenZyglq(openZyglq);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
//		files.clear();
//		files=null;
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }
}
