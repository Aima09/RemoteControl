package com.yuanfang.intercom.input;

import android.os.Handler;
import android.util.Log;

import com.yuanfang.intercom.data.AudioData;
import com.yuanfang.intercom.data.MessageQueue;
import com.yuanfang.intercom.job.JobHandler;
import com.yuanfang.intercom.network.UDP_Socket;
import com.yuanfang.intercom.util.Constants;
import com.yuanfang.intercom.util.IPUtil;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Socket发送
 *
 * @author yanghao1
 */
public class Sender extends JobHandler {
    private static final String TAG = "Sender";

    public Sender(Handler handler) {
        super(handler);
    }

    @Override
    public void run() {
        AudioData audioData;
        while ((audioData = MessageQueue.getInstance(MessageQueue.SENDER_DATA_QUEUE).take()) != null) {
            Log.d(TAG, "send udp socket " + IPUtil.getIntercomAddress());
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
}
