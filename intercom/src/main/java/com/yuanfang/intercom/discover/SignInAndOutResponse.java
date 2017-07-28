package com.yuanfang.intercom.discover;

import android.os.Handler;
import android.os.Message;

import com.yuanfang.intercom.data.AudioData;
import com.yuanfang.intercom.data.MessageQueue;
import com.yuanfang.intercom.job.JobHandler;
import com.yuanfang.intercom.network.Multicast;
import com.yuanfang.intercom.service.IntercomService;
import com.yuanfang.intercom.util.Command;
import com.yuanfang.intercom.util.Constants;
import com.yuanfang.intercom.util.IPUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Arrays;

/**
 * Created by yanghao1 on 2017/4/12.
 * 组播仅用于发现设备吧，经测试组播性能好差 - xujie@yf-space.com
 */

public class SignInAndOutResponse extends JobHandler {
    private boolean interrupted = false;

    public SignInAndOutResponse(Handler handler) {
        super(handler);
    }

    @Override
    public void run() {
        while (!interrupted) {
            // 设置接收缓冲段
            byte[] receivedData = new byte[512];
            DatagramPacket datagramPacket = new DatagramPacket(receivedData, receivedData.length);
            try {
                // 接收数据报文
                Multicast.getMulticast().getMulticastSocket().receive(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 判断数据报文类型，并做相应处理
            if (datagramPacket.getLength() == Command.DISC_REQUEST.getBytes().length ||
                    datagramPacket.getLength() == Command.DISC_LEAVE.getBytes().length ||
                    datagramPacket.getLength() == Command.DISC_RESPONSE.getBytes().length) {
                handleCommandData(datagramPacket);
            } else {
                handleAudioData(datagramPacket);
            }
        }
    }

    /**
     * 处理命令数据
     *
     * @param packet 命令数据包
     */
    private void handleCommandData(DatagramPacket packet) {
        String content = new String(packet.getData()).trim();
        if (content.equals(Command.DISC_REQUEST) &&
                !packet.getAddress().toString().equals("/" + IPUtil.getLocalIPAddress())) {
            byte[] feedback = Command.DISC_RESPONSE.getBytes();
            // 发送数据
            DatagramPacket sendPacket = new DatagramPacket(feedback, feedback.length,
                    packet.getAddress(), Constants.MULTI_BROADCAST_PORT);
            try {
                Multicast.getMulticast().getMulticastSocket().send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 发送Handler消息
            sendMsg2MainThread(packet.getAddress().toString(), IntercomService.DISCOVERING_RECEIVE);
        } else if (content.equals(Command.DISC_RESPONSE) &&
                !packet.getAddress().toString().equals("/" + IPUtil.getLocalIPAddress())) {
            // 发送Handler消息
            sendMsg2MainThread(packet.getAddress().toString(), IntercomService.DISCOVERING_RECEIVE);
        } else if (content.equals(Command.DISC_LEAVE) &&
                !packet.getAddress().toString().equals("/" + IPUtil.getLocalIPAddress())) {
            sendMsg2MainThread(packet.getAddress().toString(), IntercomService.DISCOVERING_LEAVE);
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

    @Override
    public void free() {
        interrupted = true;
    }
}
