package com.yf.remotecontrolclient.media;

import com.yf.remotecontrolclient.media.interfaces.MusicControl;
import com.yf.remotecontrolclient.media.interfaces.MusicListener;
import com.yf.remotecontrolclient.media.model.Media;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuie on 16-8-31.
 */
public class MusicCallbackManager {
    private static final String TAG = "MusicCallbackManager";

    private static MusicCallbackManager instance;
    private List<MusicListener> musicListeners = new ArrayList<>();
    private MusicControl musicControl;

    private MusicCallbackManager() {
    }

    public static synchronized MusicCallbackManager getInstance() {
        if (instance == null) {
            synchronized (MusicControlManager.class) {
                if (instance == null)
                    instance = new MusicCallbackManager();
            }
        }
        return instance;
    }

    public void provideMusicPlayer(MusicControl musicControl) {
        this.musicControl = musicControl;
    }

    public void addMediaListener(MusicListener listener) {
        if (!musicListeners.contains(listener)) {
            musicListeners.add(listener);
            if (musicControl != null) {
                exeUpdateMedia(musicControl.getCurrentMedia());
                exeUpdateMediaPlayState(musicControl.getPlayState());
            }
        }
    }

    public void removeMediaListener(MusicListener listener) {
        if (musicListeners.contains(listener)) {
            musicListeners.remove(listener);
        }
    }

    public void exeUpdateMedia(Media media) {
        for (MusicListener listener : musicListeners) {
            listener.updateMedia(media);
        }
    }

    public void exeUpdateMediaPlayPosition(int progress) {
        for (MusicListener listener : musicListeners) {
            listener.updateMediaPlayerPosition(progress);
        }
    }

    public void exeUpdateMediaPlayState(int state) {
        for (MusicListener listener : musicListeners) {
            listener.updateMediaPlayState(state);
        }
    }
}
