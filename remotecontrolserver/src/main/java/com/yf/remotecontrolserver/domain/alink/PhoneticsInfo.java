package com.yf.remotecontrolserver.domain.alink;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sujuntao on 2017/10/27 .
 */

public class PhoneticsInfo implements Serializable{
    private String artist;

    private int cacheStatus;

    private int collectionId;

    private String collectionName;

    private String duration;

    private long expiresIn;

    private int id;

    private int itemType;

    private String logo;

    private boolean loved;

    private String name;

    private int outId;

    private int playMode;

    private int playTime;

    private String playUrl;

    private String provider;

    private int providerId;

    private int seqNum;

    private int theChannelId;

    private String ttsUrl;

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setCacheStatus(int cacheStatus) {
        this.cacheStatus = cacheStatus;
    }

    public int getCacheStatus() {
        return this.cacheStatus;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public int getCollectionId() {
        return this.collectionId;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionName() {
        return this.collectionName;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public long getExpiresIn() {
        return this.expiresIn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getItemType() {
        return this.itemType;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLoved(boolean loved) {
        this.loved = loved;
    }

    public boolean getLoved() {
        return this.loved;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setOutId(int outId) {
        this.outId = outId;
    }

    public int getOutId() {
        return this.outId;
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }

    public int getPlayMode() {
        return this.playMode;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public int getPlayTime() {
        return this.playTime;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getPlayUrl() {
        return this.playUrl;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProvider() {
        return this.provider;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public int getProviderId() {
        return this.providerId;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public int getSeqNum() {
        return this.seqNum;
    }

    public void setTheChannelId(int theChannelId) {
        this.theChannelId = theChannelId;
    }

    public int getTheChannelId() {
        return this.theChannelId;
    }

    public void setTtsUrl(String ttsUrl) {
        this.ttsUrl = ttsUrl;
    }

    public String getTtsUrl() {
        return this.ttsUrl;
    }

    public class SwitchAudio {
        private int codeId;

        private PhoneticsInfo data;

        public void setCodeId(int codeId) {
            this.codeId = codeId;
        }

        public int getCodeId() {
            return this.codeId;
        }

        public void setData(PhoneticsInfo data) {
            this.data = data;
        }

        public PhoneticsInfo getData() {
            return this.data;
        }
    }

    public class ServiceDataMap {
        private SwitchAudio SwitchAudio;

        public void setSwitchAudio(SwitchAudio SwitchAudio) {
            this.SwitchAudio = SwitchAudio;
        }

        public SwitchAudio getSwitchAudio() {
            return this.SwitchAudio;
        }
    }

    public class Service_data {
        private List<String> commandList;

        private boolean executeResult;

        private ServiceDataMap serviceDataMap;

        private List<String> ttsUrl;

        public void setCommandList(List<String> commandList) {
            this.commandList = commandList;
        }

        public List<String> getCommandList() {
            return this.commandList;
        }

        public void setExecuteResult(boolean executeResult) {
            this.executeResult = executeResult;
        }

        public boolean getExecuteResult() {
            return this.executeResult;
        }

        public void setServiceDataMap(ServiceDataMap serviceDataMap) {
            this.serviceDataMap = serviceDataMap;
        }

        public ServiceDataMap getServiceDataMap() {
            return this.serviceDataMap;
        }

        public void setTtsUrl(List<String> ttsUrl) {
            this.ttsUrl = ttsUrl;
        }

        public List<String> getTtsUrl() {
            return this.ttsUrl;
        }
    }

    public class Params {
        private String artist;

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getArtist() {
            return this.artist;
        }
    }

    public class Nlp_ret {
        private String tts_text;

        private String task_status;

        private String domain;

        private int tts_id;

        private Params params;

        private String intent;

        public void setTts_text(String tts_text) {
            this.tts_text = tts_text;
        }

        public String getTts_text() {
            return this.tts_text;
        }

        public void setTask_status(String task_status) {
            this.task_status = task_status;
        }

        public String getTask_status() {
            return this.task_status;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getDomain() {
            return this.domain;
        }

        public void setTts_id(int tts_id) {
            this.tts_id = tts_id;
        }

        public int getTts_id() {
            return this.tts_id;
        }

        public void setParams(Params params) {
            this.params = params;
        }

        public Params getParams() {
            return this.params;
        }

        public void setIntent(String intent) {
            this.intent = intent;
        }

        public String getIntent() {
            return this.intent;
        }
    }

    public class Root {
        private Service_data service_data;

        private String status_code;

        private String biz_code;

        private String finish;

        private String asr_ret;

        private String context;

        private String is_resume;

        private String uuid;

        private List<Nlp_ret> nlp_ret;

        private String ctrl_dev;

        public void setService_data(Service_data service_data) {
            this.service_data = service_data;
        }

        public Service_data getService_data() {
            return this.service_data;
        }

        public void setStatus_code(String status_code) {
            this.status_code = status_code;
        }

        public String getStatus_code() {
            return this.status_code;
        }

        public void setBiz_code(String biz_code) {
            this.biz_code = biz_code;
        }

        public String getBiz_code() {
            return this.biz_code;
        }

        public void setFinish(String finish) {
            this.finish = finish;
        }

        public String getFinish() {
            return this.finish;
        }

        public void setAsr_ret(String asr_ret) {
            this.asr_ret = asr_ret;
        }

        public String getAsr_ret() {
            return this.asr_ret;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getContext() {
            return this.context;
        }

        public void setIs_resume(String is_resume) {
            this.is_resume = is_resume;
        }

        public String getIs_resume() {
            return this.is_resume;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getUuid() {
            return this.uuid;
        }

        public void setNlp_ret(List<Nlp_ret> nlp_ret) {
            this.nlp_ret = nlp_ret;
        }

        public List<Nlp_ret> getNlp_ret() {
            return this.nlp_ret;
        }

        public void setCtrl_dev(String ctrl_dev) {
            this.ctrl_dev = ctrl_dev;
        }

        public String getCtrl_dev() {
            return this.ctrl_dev;
        }
    }
}
