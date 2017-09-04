package com.yf.remotecontrolclient.activity.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yf.minalibrary.common.FileMessageConstant;
import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.activity.MediaVideoActivity;
import com.yf.remotecontrolclient.adapt.VideoAdapter;
import com.yf.remotecontrolclient.domain.Setplayvideoid;
import com.yf.remotecontrolclient.domain.Setvideoplaystatus;
import com.yf.remotecontrolclient.domain.Setvideovolumeadd;
import com.yf.remotecontrolclient.domain.Video;
import com.yf.remotecontrolclient.domain.VideoList;
import com.yf.remotecontrolclient.media.MediaSource;
import com.yf.remotecontrolclient.media.model.Media;
import com.yf.remotecontrolclient.service.MusicBusinessService;
import com.yf.remotecontrolclient.service.VideoBusinessService;
import com.yf.remotecontrolclient.service.imp.MusicBusinessServiceImpl;
import com.yf.remotecontrolclient.service.imp.VideoBusinessServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sujuntao on 2017/8/17 .
 */

public class MediaVideoRemotListFragment extends Fragment implements View.OnClickListener{
    private static String TAG="MediaMusicLocalListFragment";
    private int total;
    private VideoBusinessService mVideoBusinessService;
    VideoList videoList;
     private List<Video> videos = new ArrayList<Video>();
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String cmd = intent.getStringExtra(MusicBusinessServiceImpl.CMD);
            if (cmd.equals("BSgetvideolist")) {
                videoList = (VideoList) intent
                        .getSerializableExtra("BSgetvideolist");
                //{"VideoList":[{"duration":0,"signer":"1920X1080高清韩国美少女.rmvb","videoid":0,"videoname":"1920X1080高清韩国美少女"},{"duration":0,"signer":"1920X1080高清韩国美少女.rmvb","videoid":1,"videoname":"1920X1080高清韩国美少女"}],"cmd":"BSgetvideolist","pageIndex":0,"pageSize":2,"total":5}
                if (videoList == null) {
                    return;
                }
                total = videoList.getTotal();
                List<Video> list = videoList.getVideoList();
                videos.addAll(list);
                Message message = Message.obtain();
                handler.sendEmptyMessage(0);
            } else if (cmd.equals("BSsetvideoplaystatus")) {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = intent.getSerializableExtra("setvideoplaystatus");
                handler.sendMessage(message);
            }
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    if (videos == null) {
                        return;
                    }
                    if (videos.size() <= total) {
                        VideoList videoList = new VideoList();
                        videoList.setCmd("BSgetvideolist");
                        videoList.setPageSize(2);
                        videoList.setPageIndex(videos.size());
                        mVideoBusinessService.sendBsgetVideoList(videoList);
                    }
                    //listView.setAdapter(adapter);
                    break;
                case 1:
                    Setvideoplaystatus setvideoplaystatus = (Setvideoplaystatus) msg.obj;
                    String status = setvideoplaystatus.getStatus();
//				client收到：{"cmd":"BSsetplaystatus","status":"play"}
//				client收到：{"cmd":"BSsetplaystatus","status":"stop"}
                    if (status.equals("play")) {
                        startPause.setImageResource(R.drawable.video_button_pause);
                    } else {
                        startPause.setImageResource(R.drawable.video_button_play);
                    }
                    break;
            }
        }
    };
    private VideoAdapter adapter;
    private ListView listView;
    private ImageView volumeMinus;
    private ImageView previous;
    private ImageView startPause;
    private ImageView next;
    private ImageView repeatMode;
    private ImageView volumeAdd;

    private View view;
    private Activity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_video_remot_list,null);
        activity=getActivity();
        initView();
        initData();
        return  view;
    }

    private void initData() {
        adapter = new VideoAdapter();
        adapter.setVideos(videos);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MediaVideoActivity.MBROADCASTRECEIVER);
        activity.registerReceiver(mBroadcastReceiver, filter);
        listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        mVideoBusinessService = new VideoBusinessServiceImpl();
        VideoList videoList = new VideoList();
        videoList.setCmd("BSgetvideolist");
        videoList.setPageSize(2);
        videoList.setPageIndex(0);
        mVideoBusinessService.sendBsgetVideoList(videoList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //播放那一个视频
                Setplayvideoid setplayvideoid = new Setplayvideoid();
                setplayvideoid.setCmd("BSsetplayvideoid");
                setplayvideoid.setVideoid(position);
                mVideoBusinessService.sendBssetplayvideoid(setplayvideoid);
            }
        });
    }


    //volume_minus  previous start_pause next repeat_mode volume_add
    private void initView() {
        volumeMinus = (ImageView) view.findViewById(R.id.volume_minus);
        volumeMinus.setOnClickListener(this);
        previous = (ImageView) view.findViewById(R.id.previous);
        previous.setOnClickListener(this);
        startPause = (ImageView) view.findViewById(R.id.start_pause);
        startPause.setOnClickListener(this);
        next = (ImageView) view.findViewById(R.id.next);
        next.setOnClickListener(this);
        repeatMode = (ImageView) view.findViewById(R.id.repeat_mode);
        repeatMode.setOnClickListener(this);
        volumeAdd = (ImageView) view.findViewById(R.id.volume_add);
        volumeAdd.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.unregisterReceiver(mBroadcastReceiver);
        videos = null;
    }

    //volume_minus  previous start_pause next repeat_mode volume_add
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.volume_minus:
                //声音
                Setvideovolumeadd setvideovolumeadd = new Setvideovolumeadd();
                setvideovolumeadd.setCmd("BSsetvideovolumeadd");
                setvideovolumeadd.setValume("-");
                mVideoBusinessService.sendBssetVideovolumeadd(setvideovolumeadd);
                break;
            case R.id.previous:
                //上一首
                Setvideoplaystatus setvideoplaystatus = new Setvideoplaystatus();
                setvideoplaystatus.setCmd("BSsetvideoplaystatus");
                setvideoplaystatus.setStatus("previous");
                mVideoBusinessService.sendBssetvideoplaystatus(setvideoplaystatus);
                break;
            case R.id.start_pause:
                Setvideoplaystatus setvideoplaystatus1 = new Setvideoplaystatus();
                setvideoplaystatus1.setCmd("BSsetvideoplaystatus");
                setvideoplaystatus1.setStatus("start_pause");
                mVideoBusinessService.sendBssetvideoplaystatus(setvideoplaystatus1);
                break;
            case R.id.next:
                //下一首
                Setvideoplaystatus setvideoplaystatus2 = new Setvideoplaystatus();
                setvideoplaystatus2.setCmd("BSsetvideoplaystatus");
                setvideoplaystatus2.setStatus("next");
                mVideoBusinessService.sendBssetvideoplaystatus(setvideoplaystatus2);
                break;
            case R.id.repeat_mode:
                break;
            case R.id.volume_add:
                //声音
                Setvideovolumeadd setvideovolumeadd1 = new Setvideovolumeadd();
                setvideovolumeadd1.setCmd("BSsetvideovolumeadd");
                setvideovolumeadd1.setValume("+");
                mVideoBusinessService.sendBssetVideovolumeadd(setvideovolumeadd1);
                break;
        }
    }
}
