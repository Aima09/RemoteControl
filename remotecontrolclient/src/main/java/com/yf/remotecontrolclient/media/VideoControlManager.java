package com.yf.remotecontrolclient.media;


import com.yf.remotecontrolclient.media.model.Media;

import java.util.List;


/**
 * Created by xuie on 16-9-9.
 */
public class VideoControlManager {

    private static VideoControlManager instance = new VideoControlManager();

    private VideoControlManager() {
        mediaSource = MediaSource.getInstance();
    }

    public static VideoControlManager getInstance() {
        return instance;
    }

    private MediaSource mediaSource;

    private int currentIndex = -1;

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        if (currentIndex != -1)
            this.currentIndex = currentIndex;
    }

    public String changToPreviousVideo() {
        if (currentIndex > 0) {
            currentIndex--;
        } else {
            currentIndex = getVideoListSize() - 1;
        }
        return getCurVideoPath();
    }

    public String changToNextVideo() {
        if (currentIndex < getVideoListSize() - 1) {
            currentIndex++;
        } else {
            currentIndex = 0;
        }
        return getCurVideoPath();
    }

    public String getCurVideoPath() {
        List<Media> videos = mediaSource.getVideoList();
        if (currentIndex < 0 || currentIndex > videos.size() - 1)
            currentIndex = 0;

        if (!videos.isEmpty() && currentIndex > -1 && videos.size() > currentIndex) {
            return videos.get(currentIndex).getPath();
        }
        return null;
    }

    private int getVideoListSize() {
        List<Media> videos = mediaSource.getVideoList();
        return videos.isEmpty() ? 0 : videos.size();
    }

}
