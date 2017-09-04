package com.yf.remotecontrolclient.media.interfaces;


import com.yf.remotecontrolclient.media.model.Media;

/**
 * Created by wuhuai on 2015/11/28.
 */
public interface MusicControl {
    void play();

    void start();

    void pause();

    void stop();

    boolean isPlaying();

    void seekTo(int curProgress);

    int getCurrentPosition();

    //时长
    int getDuration();

    Media getCurrentMedia();

    //获得状态
    int getPlayState();

    //模式
    int getPlayMode();
}
