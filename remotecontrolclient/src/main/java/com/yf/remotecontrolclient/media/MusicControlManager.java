package com.yf.remotecontrolclient.media;

import android.util.Log;

import com.yf.remotecontrolclient.media.interfaces.MusicControl;
import com.yf.remotecontrolclient.media.model.Media;
import com.yf.remotecontrolclient.media.model.MediaPlayMode;


/**
 * Created by wuhuai on 2016/4/25 .
 */
public class MusicControlManager {
    private static final String TAG = "MusicControlManager";

    private static MusicControlManager instance;
    private MusicControl musicPlayerControl;
    private MediaSource mediaSource;
    private int currentIndex = -1;

    private MusicControlManager() {
    }

    /**
     * 刷新源
     */
    public void flushMediaSource(){
        this.mediaSource = MediaSource.getInstance();
    }


    public static synchronized MusicControlManager getInstance() {
        if (instance == null) {
            synchronized (MusicControlManager.class) {
                if (instance == null)
                    instance = new MusicControlManager();
            }
        }
        return instance;
    }

    public void provideMusicPlayer(MusicControl musicPlayerControl) {
        Log.d(TAG, "provideMusicPlayer");
        this.musicPlayerControl = musicPlayerControl;
        this.mediaSource = MediaSource.getInstance();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void previous() {
        if (canControl()) {
            currentIndex--;
            if (currentIndex < 0) {
                currentIndex = mediaSource.getMusicList().size() - 1;
            }

            musicPlayerControl.play();
        }
    }

    public void play() {
        if (canControl()) {
            musicPlayerControl.play();
        }
    }

    public void play(int index) {
        Log.d(TAG, "play: 0");
        if (!canControl())
            return;

        currentIndex = index;

        if (currentIndex < 0 || currentIndex > mediaSource.getMusicList().size() - 1) {
            currentIndex = 0;
        }

        Log.d(TAG, "play: " + currentIndex);

        play();
    }

    public void start() {
        if (canControl()) {
            musicPlayerControl.start();
        }
    }

    public void pause() {
        if (canControl()) {
            musicPlayerControl.pause();
        }
    }

    public void stop() {
        if (canControl()) {
            musicPlayerControl.stop();
        }
    }

    public void next() {
        Log.i(TAG, "next");
        if (canControl()) {
            if (MediaPlayMode.getMusicPlayMode() == MediaPlayMode.ALL_PLAY_MODE) {  //全部循环
                if (currentIndex == mediaSource.getMusicList().size() - 1) {//默认循环播放
                    currentIndex = 0;// 第一首
                } else {
                    currentIndex++;
                }
            } else if (MediaPlayMode.getMusicPlayMode() == MediaPlayMode.SINGLE_PLAY_MODE) {//单曲循环
                //currentIndex不需要更改
                if (currentIndex == mediaSource.getMusicList().size() - 1) {//默认循环播放
                    currentIndex = 0;// 第一首
                } else {
                    currentIndex++;
                }
            } else if (MediaPlayMode.getMusicPlayMode() == MediaPlayMode.RANDOM_PLAY_MODE) {  //随机
                currentIndex = (int) (Math.random() * mediaSource.getMusicList().size());//随机播放
            }
            musicPlayerControl.play();
        }
    }

    public boolean isPlaying() {
        if (canControl()) {
            return musicPlayerControl.isPlaying();
        }
        return false;
    }

    public void seekTo(int curProgress) {
        if (canControl()) {
            musicPlayerControl.seekTo(curProgress);
        }
    }

    public int getCurrentPosition() {
        if (canControl()) {
            return musicPlayerControl.getCurrentPosition();
        }
        return 0;
    }

    public Media getCurrentMedia() {
        if (canControl() && currentIndex > -1) {
            return mediaSource.getMusicList().get(currentIndex);
        }
        return null;
    }

    public boolean canControl() {
        if (musicPlayerControl != null) {
            return true;
        }
        Log.e(TAG, "control is null.");
        return false;
    }

}
