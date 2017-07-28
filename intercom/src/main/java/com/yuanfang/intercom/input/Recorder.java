package com.yuanfang.intercom.input;

import android.media.AudioRecord;
import android.os.Handler;
import android.util.Log;

import com.yuanfang.intercom.data.AudioData;
import com.yuanfang.intercom.job.JobHandler;
import com.yuanfang.intercom.network.UDP_Socket;
import com.yuanfang.intercom.util.AudioDataUtil;
import com.yuanfang.intercom.util.Constants;
import com.yuanfang.intercom.util.IPUtil;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * 音频录制数据格式ENCODING_PCM_16BIT，返回数据类型为short[]
 *
 * @author yanghao1
 */
public class Recorder extends JobHandler {
    private static final String TAG = "Recorder";
    private AudioRecord audioRecord;
    // 音频大小
    private int inAudioBufferSize;
    // 录音标志
    private boolean isRecording = false;

    public Recorder(Handler handler) {
        super(handler);
        init();
    }

    private synchronized void init() {
        // 获取音频数据缓冲段大小
        inAudioBufferSize = AudioRecord.getMinBufferSize(
                Constants.sampleRateInHz, Constants.inputChannelConfig, Constants.audioFormat);
        // 初始化音频录制
        audioRecord = new AudioRecord(Constants.audioSource,
                Constants.sampleRateInHz, Constants.inputChannelConfig, Constants.audioFormat, inAudioBufferSize);
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    @Override
    public void run() {
        while (isRecording) {
            if (audioRecord == null)
                init();
            if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED) {
                audioRecord.startRecording();
            }
            // 实例化音频数据缓冲
            short[] rawData = new short[inAudioBufferSize];
            audioRecord.read(rawData, 0, inAudioBufferSize);
            AudioData audioData = new AudioData(rawData);

            /*
             * 这里先跳过消息队列，直接在这里处理编码与发送
             */
//            MessageQueue.getInstance(MessageQueue.ENCODER_DATA_QUEUE).put(audioData);

            audioData.setEncodedData(AudioDataUtil.raw2spx(audioData.getRawData()));
//            MessageQueue.getInstance(MessageQueue.SENDER_DATA_QUEUE).put(audioData);
            DatagramPacket datagramPacket = new DatagramPacket(
                    audioData.getEncodedData(), audioData.getEncodedData().length,
                    IPUtil.getIntercomAddress(), Constants.UNICAST_PORT);
            try {
                UDP_Socket.get().send(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void free() {
        // 释放音频录制资源
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        AudioDataUtil.free();
    }
}
