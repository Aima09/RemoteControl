package com.yf.remotecontrolclient.media.interfaces;


import com.yf.remotecontrolclient.media.model.Media;

/**
 * Created by wuhuai on 2016/4/25 .
 */
public interface MusicListener {

    void updateMedia(Media media);

    void updateMediaPlayerPosition(int position);

    void updateMediaPlayState(int state);

}
