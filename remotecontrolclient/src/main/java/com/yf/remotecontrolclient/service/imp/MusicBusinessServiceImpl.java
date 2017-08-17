package com.yf.remotecontrolclient.service.imp;


import com.google.gson.Gson;
import com.yf.remotecontrolclient.domain.GetSongStuatus;
import com.yf.remotecontrolclient.domain.Setmode;
import com.yf.remotecontrolclient.domain.SetplaysongProgress;
import com.yf.remotecontrolclient.domain.Setplaysongid;
import com.yf.remotecontrolclient.domain.Setplaystatus;
import com.yf.remotecontrolclient.domain.Setvolumeadd;
import com.yf.remotecontrolclient.domain.SongList;
import com.yf.remotecontrolclient.minaclient.tcp.MinaMessageManager;
import com.yf.remotecontrolclient.service.MusicBusinessService;
import com.yf.remotecontrolclient.util.JsonAssistant;

public class MusicBusinessServiceImpl implements MusicBusinessService{
    public static final String CMD = "cmd";
    private JsonAssistant jsonAssistant;

    public MusicBusinessServiceImpl() {
        super();
        jsonAssistant = new JsonAssistant();
    }

    @Override
    public void sendBsgetSongList(SongList songList) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createGetSongList(songList));
    }

    @Override
    public void sendBssetplaysongid(Setplaysongid setplaysongid) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createSetplaysongid(setplaysongid));
    }

    @Override
    public void sendBssetvolumeadd(Setvolumeadd setvolumeadd) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createSetvolumeadd(setvolumeadd));
    }

    @Override
    public void sendBssetplaystatus(Setplaystatus setplaystatus) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createSetplaystatus(setplaystatus));
    }


    @Override
    public void sendMediaMode(Setmode setmode) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createSetmode(setmode));
    }

    @Override public void sendSetplaysongProgress(SetplaysongProgress setplaysongProgress) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createSetplaysongProgress(setplaysongProgress));
    }
     public void sendBsgetSongstatus(GetSongStuatus getSongStuatus){

            MinaMessageManager.getInstance()
                    .sendControlCmd(jsonAssistant.createGetSongStuatus(getSongStuatus));

        }
}
