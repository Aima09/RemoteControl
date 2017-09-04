package com.yf.remotecontrolclient.media;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.yf.remotecontrolclient.WeakHandler;
import com.yf.remotecontrolclient.media.interfaces.MusicControl;
import com.yf.remotecontrolclient.media.model.Media;
import com.yf.remotecontrolclient.media.model.MediaPlayMode;
import com.yf.remotecontrolclient.media.model.MusicPlayState;

import java.io.IOException;



public class MusicService extends Service implements MusicControl,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener {

    private static final String TAG = "MusicService";

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends WeakHandler<MusicService> {
        MyHandler(MusicService owner) {
            super(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getOwner().mMusicCallbackManager
                    .exeUpdateMediaPlayPosition(getOwner().getCurrentPosition());
            if (getOwner().isPlaying()) {
                sendEmptyMessageDelayed(0, 1000);
            }
        }
    }

    private MediaPlayer mMediaPlayer;
    private MusicControlManager mMusicControlManager;
    private MusicCallbackManager mMusicCallbackManager;
    private int mPlayerState = MusicPlayState.PAUSE;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);

        mMusicControlManager = MusicControlManager.getInstance();
        mMusicControlManager.provideMusicPlayer(this);
        mMusicCallbackManager = MusicCallbackManager.getInstance();
        mMusicCallbackManager.provideMusicPlayer(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }

    }

    @Override
    public void onLowMemory() {
        Log.d("MusicControlService", "onLowMemory be called");
        super.onLowMemory();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion");
        //TODO
        //Media
        if (MediaPlayMode.getMusicPlayMode() == MediaPlayMode.SINGLE_PLAY_MODE) {//单曲循环
            //currentIndex不需要更改直接播放
            mMusicControlManager.play();
            return;
        }

        mMusicControlManager.next();
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "onError");
        mMusicControlManager.next();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mMediaPlayer.isPlaying())
            mPlayerState = MusicPlayState.PLAY;
        updateMediaPlayState();
        Log.d(TAG, "onPrepared");
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.d(TAG, "onSeekComplete");
    }

    @Override
    public void play() {
        Log.d(TAG, "play");
        Media media = mMusicControlManager.getCurrentMedia();
        if (media == null) {
            Log.d(TAG, "play: media is null");
            return;
        }
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(media.getPath());
            mMediaPlayer.prepare();
            start();
            updateMedia();
            updateMediaPlayPosition();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        Log.d(TAG, "start");
        mMediaPlayer.start();
        if (mMediaPlayer.isPlaying())
            mPlayerState = MusicPlayState.PLAY;
        updateMediaPlayPosition();
        updateMediaPlayState();
    }

    @Override
    public void pause() {
        Log.d(TAG, "pause");
        mMediaPlayer.pause();
        mPlayerState = MusicPlayState.PAUSE;
        updateMediaPlayState();
        updateMediaPlayPosition();
    }

    @Override
    public void stop() {
        Log.d(TAG, "stop");
        mMediaPlayer.stop();
        mPlayerState = MusicPlayState.STOP;
        updateMediaPlayState();
        updateMediaPlayPosition();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(int curProgress) {
        mMediaPlayer.seekTo(curProgress);
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public Media getCurrentMedia() {
        return mMusicControlManager.getCurrentMedia();
    }

    @Override
    public int getPlayState() {
        return mPlayerState;
    }

    @Override
    public int getPlayMode() {
        return 0;
    }

    private void updateMediaPlayState() {
        mMusicCallbackManager.exeUpdateMediaPlayState(mPlayerState);
    }

    private void updateMediaPlayPosition() {
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessage(0);
    }

    private void updateMedia() {
        mMusicCallbackManager.exeUpdateMedia(getCurrentMedia());
    }
}
