package com.yf.remotecontrolclient.service.imp;


import com.yf.remotecontrolclient.domain.Setmode;
import com.yf.remotecontrolclient.domain.Setplaysongid;
import com.yf.remotecontrolclient.domain.Setplaystatus;
import com.yf.remotecontrolclient.domain.Setvolumeadd;
import com.yf.remotecontrolclient.domain.SongList;
import com.yf.remotecontrolclient.minaclient.tcp.MinaCmdManager;
import com.yf.remotecontrolclient.service.MusicBusinessService;
import com.yf.remotecontrolclient.util.JsonAssistant;

public class MusicBusinessServiceImpl implements MusicBusinessService {
    public static final String CMD = "cmd";
    private JsonAssistant jsonAssistant;

    public MusicBusinessServiceImpl() {
        super();
        jsonAssistant = new JsonAssistant();
    }

    @Override
    public void sendBsgetSongList(SongList songList) {
        MinaCmdManager.getInstance()
                .sendControlCmd(jsonAssistant.createGetSongList(songList));
    }

    @Override
    public void sendBssetplaysongid(Setplaysongid setplaysongid) {
        MinaCmdManager.getInstance()
                .sendControlCmd(jsonAssistant.createSetplaysongid(setplaysongid));
    }

    @Override
    public void sendBssetvolumeadd(Setvolumeadd setvolumeadd) {
        MinaCmdManager.getInstance()
                .sendControlCmd(jsonAssistant.createSetvolumeadd(setvolumeadd));
    }

    @Override
    public void sendBssetplaystatus(Setplaystatus setplaystatus) {
        MinaCmdManager.getInstance()
                .sendControlCmd(jsonAssistant.createSetplaystatus(setplaystatus));
    }


    @Override
    public void sendMediaMode(Setmode setmode) {
        MinaCmdManager.getInstance()
                .sendControlCmd(jsonAssistant.createSetmode(setmode));
    }
}
