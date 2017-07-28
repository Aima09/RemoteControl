package com.yuanfang.intercom.output;

import android.os.Handler;
import android.os.Message;

import com.yuanfang.intercom.data.AudioData;
import com.yuanfang.intercom.data.MessageQueue;
import com.yuanfang.intercom.job.JobHandler;
import com.yuanfang.intercom.network.UDP_Socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Arrays;

/**
 * Created by yanghao1 on 2017/4/12.
 */

public class Receiver extends JobHandler {
    private static final String TAG = "Receiver";

    public Receiver(Handler handler) {
        super(handler);
    }

    @Override
    public void run() {
        while (true) {
            // 设置接收缓冲段
            byte[] receivedData = new byte[512];
            DatagramPacket datagramPacket = new DatagramPacket(receivedData, receivedData.length);

            try {
                UDP_Socket.get().receive(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            handleAudioData(datagramPacket);
        }
    }

    /**
     * 处理音频数据
     *
     * @param packet 音频数据包
     */
    private void handleAudioData(DatagramPacket packet) {
        AudioData audioData = new AudioData(Arrays.copyOf(packet.getData(), packet.getLength()));
        MessageQueue.getInstance(MessageQueue.DECODER_DATA_QUEUE).put(audioData);

        /*
         * 这里跳过消息队列的解码过程
         */
//        audioData.setRawData(AudioDataUtil.spx2raw(audioData.getEncodedData()));
//        MessageQueue.getInstance(MessageQueue.TRACKER_DATA_QUEUE).put(audioData);
    }

    /**
     * 发送Handler消息
     *
     * @param content 内容
     */
    private void sendMsg2MainThread(String content, int msgWhat) {
        Message msg = new Message();
        msg.what = msgWhat;
        msg.obj = content;
        handler.sendMessage(msg);
    }
}
