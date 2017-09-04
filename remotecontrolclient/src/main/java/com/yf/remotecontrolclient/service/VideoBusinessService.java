package com.yf.remotecontrolclient.service;


import com.yf.remotecontrolclient.domain.Setplayvideoid;
import com.yf.remotecontrolclient.domain.Setvideoplaystatus;
import com.yf.remotecontrolclient.domain.Setvideovolumeadd;
import com.yf.remotecontrolclient.domain.VideoList;
import com.yf.remotecontrolclient.media.model.Media;

public interface VideoBusinessService {
    public void sendBsgetVideoList(VideoList videoList);

    public void sendBssetplayvideoid(Setplayvideoid setplayvideoid);

    public void sendBssetVideovolumeadd(Setvideovolumeadd setvideovolumeadd);

    public void sendBssetvideoplaystatus(Setvideoplaystatus setvideoplaystatus);

    public void sendBsVideoFile(String path,String use);

    public void sendBsTsVideoFile(Media media);
}
