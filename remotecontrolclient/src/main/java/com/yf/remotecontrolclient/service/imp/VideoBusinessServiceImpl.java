package com.yf.remotecontrolclient.service.imp;


import com.yf.remotecontrolclient.domain.Setplayvideoid;
import com.yf.remotecontrolclient.domain.Setvideoplaystatus;
import com.yf.remotecontrolclient.domain.Setvideovolumeadd;
import com.yf.remotecontrolclient.domain.VideoList;
import com.yf.remotecontrolclient.minaclient.tcp.MinaMessageManager;
import com.yf.remotecontrolclient.service.VideoBusinessService;
import com.yf.remotecontrolclient.util.JsonAssistant;

public class VideoBusinessServiceImpl implements VideoBusinessService {
    public static final String CMD = "cmd";
    private JsonAssistant jsonAssistant;

    public VideoBusinessServiceImpl() {
        super();
        jsonAssistant = new JsonAssistant();
    }

    @Override
    public void sendBsgetVideoList(VideoList videoList) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createGetVideoList(videoList));
    }

    @Override
    public void sendBssetplayvideoid(Setplayvideoid setplayvideoid) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createSetplayvideoid(setplayvideoid));
    }

    @Override
    public void sendBssetVideovolumeadd(Setvideovolumeadd setvideovolumeadd) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createSetvideovolumeadd(setvideovolumeadd));
    }

    @Override
    public void sendBssetvideoplaystatus(Setvideoplaystatus setvideoplaystatus) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createSetvideoplaystatus(setvideoplaystatus));
    }

}
