package com.yf.remotecontrolclient.service.imp;


import android.util.Log;

import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.domain.GetSongStuatus;
import com.yf.remotecontrolclient.domain.Pmmedia;
import com.yf.remotecontrolclient.domain.Setmode;
import com.yf.remotecontrolclient.domain.SetplaysongProgress;
import com.yf.remotecontrolclient.domain.Setplaysongid;
import com.yf.remotecontrolclient.domain.Setplaystatus;
import com.yf.remotecontrolclient.domain.Setvolumeadd;
import com.yf.remotecontrolclient.domain.SongList;
import com.yf.remotecontrolclient.media.model.Media;
import com.yf.remotecontrolclient.minaclient.tcp.MinaMessageManager;
import com.yf.remotecontrolclient.service.MusicBusinessService;
import com.yf.remotecontrolclient.util.GsonUtil;
import com.yf.remotecontrolclient.util.IpUtil;
import com.yf.remotecontrolclient.util.JsonAssistant;

import java.net.URLEncoder;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.yf.remotecontrolclient.activity.fragment.MediaMusicRemotListFragment.songList;

public class MusicBusinessServiceImpl implements MusicBusinessService{
    public static final String CMD = "cmd";
    private JsonAssistant jsonAssistant;

    public MusicBusinessServiceImpl() {
        super();
        jsonAssistant = new JsonAssistant();
    }

    @Override
    public void sendBsgetSongList(SongList songList) {
        String oldS=jsonAssistant.createGetSongList(songList);
        String newS=GsonUtil.GsonString(songList);
        Log.i("MusicB","oldS"+oldS);
        Log.i("MusicB","newS"+newS);
        MinaMessageManager.getInstance()
                .sendControlCmd(oldS);
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

    @Override public void sendBsMusicFile(String path,String use) {
        MinaMessageManager.getInstance()
                .sendFile(path,use);
    }

    @Override public void sendBsTsMusicFile(Media media) {
        try {

            Pmmedia pmmedia = new Pmmedia();
            pmmedia.setCmd("Bspmmedia");
            pmmedia.setAbulmId(media.getAbulmId());
            pmmedia.setArtist(media.getArtist());
            pmmedia.setDisplayName(media.getDisplayName());
            pmmedia.setDuration(media.getDuration());
            pmmedia.setId(media.getId());
            pmmedia.setSize(media.getSize());
            pmmedia.setTitle(media.getTitle());
            pmmedia.setPath("http://" + IpUtil.getLocalIpAddress(App.getAppContext()) + ":8089?path=" + URLEncoder.encode(media.getPath(), "UTF-8"));
            Log.i("MusicBusinessServiceImpl", "sendBsTsMusicFile: "+pmmedia.getPath());
            MinaMessageManager.getInstance()
                    .sendControlCmd(jsonAssistant.createPmMedia(pmmedia));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
