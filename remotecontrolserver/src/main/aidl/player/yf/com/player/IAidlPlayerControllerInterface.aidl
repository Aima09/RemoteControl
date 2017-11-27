// IAidlPlayerControllerInterface.aidl
package player.yf.com.player;

// Declare any non-default types here with import statements
import player.yf.com.player.MusicInfo;
interface IAidlPlayerControllerInterface {
       void play();//播放
       void pause();//暂停
       void playPause();//播放暂停
       void stop();//停止
       void volumeDown();//音量-
       void volumeUp();//音量+
       void previous();//上一曲
       void next();//下一曲
       //获取状态
       //1 正在播放
       //2 暂停
       //3 停止
       int getPlayStatus();

       //alink操作接口
       void setMusicInfo(in MusicInfo musicInfo);//这个接口要做切换为阿里音源
       void setProgress(int progress);//设置进度
       void setMax(int maxProgress);//设置最大进度
}
