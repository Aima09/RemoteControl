package com.yf.remotecontrolclient.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.adapt.ImageAdapter;
import com.yf.remotecontrolclient.domain.Action;
import com.yf.remotecontrolclient.domain.Image;
import com.yf.remotecontrolclient.domain.ImageList;
import com.yf.remotecontrolclient.domain.OpenImage;
import com.yf.remotecontrolclient.service.ImageBusinessService;
import com.yf.remotecontrolclient.service.imp.ImageBusinessServiceImpl;
import com.yf.remotecontrolclient.service.imp.MouseBusinessServiceImpl;
import com.yf.remotecontrolclient.service.imp.MusicBusinessServiceImpl;

/**
 * 用于显示文件夹的图片activity
 *
 * @author sujuntao
 */
public class MediaImageActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ImageAdapter imageAdapter;
    public final static String MBROADCASTRECEIVER = "com.yf.client.activity.MediaImageActivity.image";
    private List<Image> Images = new ArrayList<Image>();
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
//				Log.i(TAG, ImageList.toString());
                total = imageList.getTotal();
//				Toast.makeText(getApplicationContext(), total+"", Toast.LENGTH_LONG).show();
                List<Image> list = imageList.getImageList();
                Images.addAll(list);
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
                    imageAdapter.notifyDataSetChanged();
                    if (Images == null) {
                        return;
                    }
                    if (Images.size() <= total) {
                        ImageList ImageList = new ImageList();
                        ImageList.setCmd("BSgetimageList");
                        ImageList.setPageSize(2);
                        ImageList.setPageIndex(Images.size());
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
        imageAdapter.setImages(Images);
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
        super.onDestroy();
    }

    boolean isOpenImage = false;

    @Override
    public void onBackPressed() {
//		if(isOpenImage)
//			back();
        isOpenImage = false;
        super.onBackPressed();
    }

    private void back() {
        Action action = new Action();
        action.setCmd("KEY");
        action.setData(String.valueOf(KeyEvent.KEYCODE_BACK));
        new MouseBusinessServiceImpl().sendAction(action);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        OpenImage openImage = new OpenImage();
        Image image = Images.get(position);
        openImage.setCmd("BSopenImage");
        openImage.setImageFileName(image.getName());
        //发送打开图片命令
        ImageBusinessService businessService = new ImageBusinessServiceImpl();
        businessService.senBsopenImage(openImage);
        isOpenImage = true;
    }

}
