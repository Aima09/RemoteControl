package com.yf.remotecontrolclient;

/**
 * Created by wuhuai on 2016/3/7 .
 * .
 */
public class MediaPlayMode {

    public static final int ALL_PLAY_MODE = 5;           // 循环播放所有媒体模式
    public static final int SINGLE_PLAY_MODE = 6;        // 单曲播放模式
    public static final int RANDOM_PLAY_MODE = 7;        // 随机播放模式

    //当前音乐播放模式 默认循环播放所有歌曲模式
    private static int VIDEO_PLAY_MODE = ALL_PLAY_MODE;
    //当前视频播放模式 默认循环播放所有视频模式
    private static int MUSIC_PLAY_MODE = ALL_PLAY_MODE;

    public static int getMusicPlayMode() {
        return MUSIC_PLAY_MODE;
    }

    public static void setMusicPlayMode(int musicPlayMode) {
        MUSIC_PLAY_MODE = musicPlayMode;
    }

    public static int getVideoPlayMode() {
        return VIDEO_PLAY_MODE;
    }

    public static void setVideoPlayMode(int videoPlayMode) {
        VIDEO_PLAY_MODE = videoPlayMode;
    }
}
