package com.yf.remotecontrolclient.service.imp;


import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.domain.Pmmedia;
import com.yf.remotecontrolclient.domain.Setplayvideoid;
import com.yf.remotecontrolclient.domain.Setvideoplaystatus;
import com.yf.remotecontrolclient.domain.Setvideovolumeadd;
import com.yf.remotecontrolclient.domain.VideoList;
import com.yf.remotecontrolclient.media.model.Media;
import com.yf.remotecontrolclient.minaclient.tcp.MinaMessageManager;
import com.yf.remotecontrolclient.service.VideoBusinessService;
import com.yf.remotecontrolclient.util.IpUtil;
import com.yf.remotecontrolclient.util.JsonAssistant;

import java.net.URLEncoder;

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

    @Override public void sendBsVideoFile(String path,String use) {
        MinaMessageManager.getInstance()
                .sendFile(path,use);
    }

    @Override public void sendBsTsVideoFile(Media media) {
        try {
            Pmmedia pmmedia = new Pmmedia();
            pmmedia.setCmd("BspVideommedia");
            pmmedia.setAbulmId(media.getAbulmId());
            pmmedia.setArtist(media.getArtist());
            pmmedia.setDisplayName(media.getDisplayName());
            pmmedia.setDuration(media.getDuration());
            pmmedia.setId(media.getId());
            pmmedia.setSize(media.getSize());
            pmmedia.setTitle(media.getTitle());
            pmmedia.setPath("http://" + IpUtil.getLocalIpAddress(App.getAppContext()) + ":8089?path=" + URLEncoder.encode(media.getPath(), "UTF-8"));
            MinaMessageManager.getInstance()
                    .sendControlCmd(jsonAssistant.createPmMedia(pmmedia));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
