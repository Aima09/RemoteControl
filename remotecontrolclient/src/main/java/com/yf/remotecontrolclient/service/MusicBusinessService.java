package com.yf.remotecontrolclient.service;


import com.yf.remotecontrolclient.domain.GetSongStuatus;
import com.yf.remotecontrolclient.domain.Setmode;
import com.yf.remotecontrolclient.domain.SetplaysongProgress;
import com.yf.remotecontrolclient.domain.Setplaysongid;
import com.yf.remotecontrolclient.domain.Setplaystatus;
import com.yf.remotecontrolclient.domain.Setvolumeadd;
import com.yf.remotecontrolclient.domain.SongList;

public interface MusicBusinessService {
    public void sendBsgetSongList(SongList songList);

    public void sendBssetplaysongid(Setplaysongid setplaysongid);

    public void sendBssetvolumeadd(Setvolumeadd setvolumeadd);

    public void sendBssetplaystatus(Setplaystatus setplaystatus);

    public void sendMediaMode(Setmode setmode);

    public void sendSetplaysongProgress(SetplaysongProgress setplaysongProgress);
    public void sendBsgetSongstatus(GetSongStuatus getSongStuatus);
}
