package com.yf.remotecontrolclient.media.model;

/**
 * Created by wuhuai on 2016/4/27 .
 */
public class MusicPlayState {
    //暂停播放
    public static final int PAUSE = 0;
    //播放状态
    public static final int PLAY = 1;
    //停止状态
    public static final int STOP = 2;

    //歌曲控制广播
    public static final String SONG_CONTROL_ACTION = "song_control_action";
    //歌曲控制广播 关键字
    public static final String SONG_CONTROL = "song_control";
    //模式切换
    public static final int CONTROL_MODE = 2;
    //上一首
    public static final int CONTROL_PRE = -1;
    //播放或暂停
    public static final int CONTROL_PLAY_OR_PAUSE = 0;
    //下一首
    public static final int CONTROL_NEXT = 1;
}