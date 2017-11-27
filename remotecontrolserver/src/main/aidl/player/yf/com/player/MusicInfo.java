package player.yf.com.player;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by sujuntao on 2017/10/31 .
 */

public class MusicInfo implements Parcelable{
        private int playTime;
        private int seqNum;
        private boolean loved;
        private int cacheStatus;
        private int outId;
        private String artist;
        private String duration;
        private int theChannelId;
        private String playUrl;
        private String collectionName;
        private int collectionId;
        private int providerId;
        private String ttsUrl;
        private int id;
        private int itemType;
        private String name;
        private long expiresIn;
        private String logo;
        private String provider;
        private int playMode;

    protected MusicInfo(Parcel in) {
        playTime = in.readInt();
        seqNum = in.readInt();
        loved = in.readByte() != 0;
        cacheStatus = in.readInt();
        outId = in.readInt();
        artist = in.readString();
        duration = in.readString();
        theChannelId = in.readInt();
        playUrl = in.readString();
        collectionName = in.readString();
        collectionId = in.readInt();
        providerId = in.readInt();
        ttsUrl = in.readString();
        id = in.readInt();
        itemType = in.readInt();
        name = in.readString();
        expiresIn = in.readLong();
        logo = in.readString();
        provider = in.readString();
        playMode = in.readInt();
    }

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel in) {
            return new MusicInfo(in);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }
        public int getPlayTime() {
            return playTime;
        }

        public void setSeqNum(int seqNum) {
            this.seqNum = seqNum;
        }
        public int getSeqNum() {
            return seqNum;
        }

        public void setLoved(boolean loved) {
            this.loved = loved;
        }
        public boolean getLoved() {
            return loved;
        }

        public void setCacheStatus(int cacheStatus) {
            this.cacheStatus = cacheStatus;
        }
        public int getCacheStatus() {
            return cacheStatus;
        }

        public void setOutId(int outId) {
            this.outId = outId;
        }
        public int getOutId() {
            return outId;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }
        public String getArtist() {
            return artist;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }
        public String getDuration() {
            return duration;
        }

        public void setTheChannelId(int theChannelId) {
            this.theChannelId = theChannelId;
        }
        public int getTheChannelId() {
            return theChannelId;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }
        public String getPlayUrl() {
            return playUrl;
        }

        public void setCollectionName(String collectionName) {
            this.collectionName = collectionName;
        }
        public String getCollectionName() {
            return collectionName;
        }

        public void setCollectionId(int collectionId) {
            this.collectionId = collectionId;
        }
        public int getCollectionId() {
            return collectionId;
        }

        public void setProviderId(int providerId) {
            this.providerId = providerId;
        }
        public int getProviderId() {
            return providerId;
        }

        public void setTtsUrl(String ttsUrl) {
            this.ttsUrl = ttsUrl;
        }
        public String getTtsUrl() {
            return ttsUrl;
        }

        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }
        public int getItemType() {
            return itemType;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setExpiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
        }
        public long getExpiresIn() {
            return expiresIn;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
        public String getLogo() {
            return logo;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }
        public String getProvider() {
            return provider;
        }

        public void setPlayMode(int playMode) {
            this.playMode = playMode;
        }
        public int getPlayMode() {
            return playMode;
        }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(playTime);
        dest.writeInt(seqNum);
        dest.writeByte((byte) (loved ? 1 : 0));
        dest.writeInt(cacheStatus);
        dest.writeInt(outId);
        dest.writeString(artist);
        dest.writeString(duration);
        dest.writeInt(theChannelId);
        dest.writeString(playUrl);
        dest.writeString(collectionName);
        dest.writeInt(collectionId);
        dest.writeInt(providerId);
        dest.writeString(ttsUrl);
        dest.writeInt(id);
        dest.writeInt(itemType);
        dest.writeString(name);
        dest.writeLong(expiresIn);
        dest.writeString(logo);
        dest.writeString(provider);
        dest.writeInt(playMode);
    }
}
