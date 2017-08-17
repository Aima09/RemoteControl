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
import com.yf.remotecontrolclient.adapt.ImageAdapter;
import com.yf.remotecontrolclient.domain.Image;
import com.yf.remotecontrolclient.domain.ImageList;
import com.yf.remotecontrolclient.domain.OpenImage;
import com.yf.remotecontrolclient.service.ImageBusinessService;
import com.yf.remotecontrolclient.service.imp.ImageBusinessServiceImpl;
import com.yf.remotecontrolclient.service.imp.MusicBusinessServiceImpl;

import static android.media.CamcorderProfile.get;

/**
 * 用于显示文件夹的图片activity
 *
 * @author sujuntao
 */
public class MediaImageActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ImageAdapter imageAdapter;
    public final static String MBROADCASTRECEIVER = "com.yf.client.activity.MediaImageActivity.image";
    private ImageList imageList;
    private ImageBusinessService imageBusinessService;
    int total;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String cmd = intent.getStringExtra(MusicBusinessServiceImpl.CMD);
            if (cmd.equals("BSgetimageList")) {
                imageList = (ImageList) intent
                        .getSerializableExtra("getimageList");
                if (imageList == null) {
                    return;
                }
                total = imageList.getTotal();
                List<Image> list = imageList.getImageList();
                imageAdapter.addAndrefresh(list);
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
                    imageAdapter.notifyDataSetChanged();
                    if (imageAdapter.getDataCount() <= total) {
                        ImageList ImageList = new ImageList();
                        ImageList.setCmd("BSgetimageList");
                        ImageList.setPageSize(2);
                        ImageList.setPageIndex(imageAdapter.getDataCount());
                        imageBusinessService.sendBSgetImageList(ImageList);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        initspinner();
        listView = (ListView) findViewById(R.id.image_list_view);
        imageAdapter = new ImageAdapter();
        imageAdapter.setImages(new ArrayList<Image>());
        listView.setAdapter(imageAdapter);

        IntentFilter filter = new IntentFilter();
        filter.addAction(MBROADCASTRECEIVER);
        registerReceiver(mBroadcastReceiver, filter);

        imageBusinessService = new ImageBusinessServiceImpl();

        ImageList ImageList = new ImageList();
        ImageList.setCmd("BSgetimageList");
        ImageList.setPageSize(2);
        ImageList.setPageIndex(0);
        imageBusinessService.sendBSgetImageList(ImageList);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        imageAdapter.clear();
        imageAdapter=null;
        listView=null;
        System.gc();
        super.onDestroy();
    }

    boolean isOpenImage = false;

    @Override
    public void onBackPressed() {
        isOpenImage = false;
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        OpenImage openImage = new OpenImage();
        Image image = imageAdapter.getImages().get(position);
        openImage.setCmd("BSopenImage");
        openImage.setImageFileName(image.getName());
        //发送打开图片命令
        ImageBusinessService businessService = new ImageBusinessServiceImpl();
        businessService.senBsopenImage(openImage);
        isOpenImage = true;
    }

}
