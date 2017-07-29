package com.yf.remotecontrolclient.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.MediaPlayMode;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.adapt.MusicAdapter;
import com.yf.remotecontrolclient.domain.Setmode;
import com.yf.remotecontrolclient.domain.Setplaysongid;
import com.yf.remotecontrolclient.domain.Setplaystatus;
import com.yf.remotecontrolclient.domain.Setvolumeadd;
import com.yf.remotecontrolclient.domain.Song;
import com.yf.remotecontrolclient.domain.SongList;
import com.yf.remotecontrolclient.service.MusicBusinessService;
import com.yf.remotecontrolclient.service.imp.MusicBusinessServiceImpl;

import static com.yf.remotecontrolclient.R.drawable.et_media_mode_circulate_n;

public class MediaMusicActivity extends BaseActivity implements View.OnClickListener {
    //	public final String TAG = "MediaMusicActivity";
    private int total;
    private MusicBusinessService musicBusinessService;
    SongList songList;
    public final static String MBROADCASTRECEIVER = "com.yf.client.activity.MediaMisicActivity.mBroadcastReceiver.song";
    private List<Song> songs = new ArrayList<Song>();
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String cmd = intent.getStringExtra(MusicBusinessServiceImpl.CMD);
            Log.i("MediaMusicActivity", cmd);
            if (cmd.equals("BSgetsonglist")) {
                songList = (SongList) intent
                        .getSerializableExtra("BSgetsonglist");
                if (songList == null) {
                    return;
                }
//				Log.i(TAG, songList.toString());
                total = songList.getTotal();
//				Toast.makeText(getApplicationContext(), total+"", Toast.LENGTH_LONG).show();
                List<Song> list = songList.getSongList();
                songs.addAll(list);
                Message message = Message.obtain();
                handler.sendEmptyMessage(0);
            } else if (cmd.equals("BSsetplaystatus")) {
                Log.i("MediaMusicActivity", "BSsetplaystatus");
                Message message = Message.obtain();
                message.what = 1;
                message.obj = intent.getSerializableExtra("setplaystatus");
                handler.sendMessage(message);
            } else if (cmd.equals("BSsetmode")) {
                Log.i("MediaMusicActivity", "BSsetmode");
                Message message = Message.obtain();
                message.what = 2;
                message.obj = intent.getSerializableExtra("setmode");
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
                    if (songs == null) {
                        return;
                    }
                    if (songs.size() <= total) {
                        SongList songList = new SongList();
                        songList.setCmd("BSgetsonglist");
                        songList.setPageSize(2);
                        songList.setPageIndex(songs.size());
                        musicBusinessService.sendBsgetSongList(songList);
                    }
                    //listView.setAdapter(adapter);
                    break;
                case 1:
                    Setplaystatus setplaystatus = (Setplaystatus) msg.obj;
                    String status = setplaystatus.getStatus();
//				client收到：{"cmd":"BSsetplaystatus","status":"play"}
//				client收到：{"cmd":"BSsetplaystatus","status":"stop"}
                    if (status.equals("play")) {
                        startPause.setImageResource(R.drawable.video_button_pause);
                    } else {
                        startPause.setImageResource(R.drawable.video_button_play);
                    }
                    break;
                case 2:
                    Setmode setmode = (Setmode) msg.obj;
                    //改变状态图片

                    switch (setmode.getMode()) {
                        case MediaPlayMode.RANDOM_PLAY_MODE:
                            repeatMode.setImageResource(R.drawable.et_media_mode_random_n);
                            break;
                        case MediaPlayMode.ALL_PLAY_MODE:
                            repeatMode.setImageResource(et_media_mode_circulate_n);
                            break;
                        case MediaPlayMode.SINGLE_PLAY_MODE:
                            repeatMode.setImageResource(R.drawable.et_media_mode_single_n);
                            break;
                    }
                    repeatMode.setImageResource(R.drawable.et_media_mode_random_n);
                    break;
            }
        }
    };
    private MusicAdapter adapter;
    private ListView listView;
    private ImageView volumeMinus;
    private ImageView previous;
    private ImageView startPause;
    private ImageView next;
    private ImageView repeatMode;
    private ImageView volumeAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        initspinner();
        initView();
        adapter = new MusicAdapter();
        adapter.setSongs(songs);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MBROADCASTRECEIVER);
        registerReceiver(mBroadcastReceiver, filter);
//		Toast.makeText(getApplicationContext(), "将显示音乐列表", Toast.LENGTH_LONG)
//				.show();
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        musicBusinessService = new MusicBusinessServiceImpl();
        // 获取音乐列表
        SongList songList = new SongList();
        songList.setCmd("BSgetsonglist");
        songList.setPageSize(2);
        songList.setPageIndex(0);
        musicBusinessService.sendBsgetSongList(songList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //播放那一首歌
                Setplaysongid setplaysongid = new Setplaysongid();
                setplaysongid.setCmd("BSsetplaysongid");
                setplaysongid.setSongid(position);
                musicBusinessService.sendBssetplaysongid(setplaysongid);
            }
        });
    }

    //volume_minus  previous start_pause next repeat_mode volume_add
    private void initView() {
        volumeMinus = (ImageView) findViewById(R.id.volume_minus);
        volumeMinus.setOnClickListener(this);
        previous = (ImageView) findViewById(R.id.previous);
        previous.setOnClickListener(this);
        startPause = (ImageView) findViewById(R.id.start_pause);
        startPause.setOnClickListener(this);
        next = (ImageView) findViewById(R.id.next);
        next.setOnClickListener(this);
        repeatMode = (ImageView) findViewById(R.id.repeat_mode);
        repeatMode.setOnClickListener(this);
        volumeAdd = (ImageView) findViewById(R.id.volume_add);
        volumeAdd.setOnClickListener(this);
    }

    //	private void flushUI(boolean isplay){
//		if(isplay==true){
//			
//		}
//	}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        songs = null;
    }

    //volume_minus  previous start_pause next repeat_mode volume_add
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.volume_minus:
                //声音
                Setvolumeadd setvolumeadd = new Setvolumeadd();
                setvolumeadd.setCmd("BSsetvolumeadd");
                setvolumeadd.setValume("-");
                musicBusinessService.sendBssetvolumeadd(setvolumeadd);
                break;
            case R.id.previous:
                //上一首
                Setplaystatus setplaystatus = new Setplaystatus();
                setplaystatus.setCmd("BSsetplaystatus");
                setplaystatus.setStatus("previous");
                musicBusinessService.sendBssetplaystatus(setplaystatus);
                break;
            case R.id.start_pause:
                Setplaystatus setplaystatus1 = new Setplaystatus();
                setplaystatus1.setCmd("BSsetplaystatus");
                setplaystatus1.setStatus("start_pause");
                musicBusinessService.sendBssetplaystatus(setplaystatus1);

			/*Setplaysongid setplaysongid=new Setplaysongid();
            setplaysongid.setCmd("BSsetplaysongid");
			setplaysongid.setSongid(0);*/
//			musicBusinessService.sendBssetplaysongid(setplaysongid);
		/*	JsonAssistant jsonAssistant=new JsonAssistant();
			String data=jsonAssistant.createSetplaysongid(setplaysongid);
			MinaCmdManager.getInstance().sendMusicControlCmd(data);*/
                break;
            case R.id.next:
                //下一首
                Setplaystatus setplaystatus2 = new Setplaystatus();
                setplaystatus2.setCmd("BSsetplaystatus");
                setplaystatus2.setStatus("next");
                musicBusinessService.sendBssetplaystatus(setplaystatus2);
                break;
            case R.id.repeat_mode:
                senMode();
                break;
            case R.id.volume_add:
                Setvolumeadd setvolumeadd1 = new Setvolumeadd();
                setvolumeadd1.setCmd("BSsetvolumeadd");
                setvolumeadd1.setValume("+");
                musicBusinessService.sendBssetvolumeadd(setvolumeadd1);
                break;
        }
    }

    private void senMode() {
        Setmode setmode = new Setmode();
        setmode.setCmd("BSsetmode");

        if (repeatMode.getDrawable().getCurrent().getConstantState().
                equals(ContextCompat.getDrawable(App.getAppContext(), et_media_mode_circulate_n).getConstantState())) {
            repeatMode.setImageResource(R.drawable.et_media_mode_random_n);
            //发送随机模式
            setmode.setMode(MediaPlayMode.RANDOM_PLAY_MODE);
        } else if (repeatMode.getDrawable().getCurrent().getConstantState().
                equals(ContextCompat.getDrawable(App.getAppContext(), R.drawable.et_media_mode_random_n).getConstantState())) {
            repeatMode.setImageResource(R.drawable.et_media_mode_single_n);
            //发送单曲循环模式
            setmode.setMode(MediaPlayMode.SINGLE_PLAY_MODE);
        } else if (repeatMode.getDrawable().getCurrent().getConstantState().
                equals(ContextCompat.getDrawable(App.getAppContext(), R.drawable.et_media_mode_single_n).getConstantState())) {
            //发送全部循环播放模式
            repeatMode.setImageResource(et_media_mode_circulate_n);
            setmode.setMode(MediaPlayMode.ALL_PLAY_MODE);
        }
        musicBusinessService.sendMediaMode(setmode);
    }
}