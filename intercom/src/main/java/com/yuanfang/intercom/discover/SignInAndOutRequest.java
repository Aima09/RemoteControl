package com.yuanfang.intercom.discover;

import android.os.Handler;
import android.os.Message;

import com.yuanfang.intercom.job.JobHandler;
import com.yuanfang.intercom.network.Multicast;
import com.yuanfang.intercom.service.IntercomService;
import com.yuanfang.intercom.util.Command;
import com.yuanfang.intercom.util.Constants;

import java.io.IOException;
import java.net.DatagramPacket;

public class SignInAndOutRequest extends JobHandler {

    private String command;

    public SignInAndOutRequest(Handler handler) {
        super(handler);
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public void run() {
        if (command != null) {
            byte[] data = command.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(
                    data, data.length, Multicast.getMulticast().getInetAddress(), Constants.MULTI_BROADCAST_PORT);
            try {
                Multicast.getMulticast().getMulticastSocket().send(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (command.equals(Command.DISC_REQUEST)) {
                sendMsg2MainThread();
            } else if (command.equals(Command.DISC_LEAVE)) {
                setCommand(Command.DISC_REQUEST);
            }
        }
    }

    /**
     * 发送消息到主线程
     */
    private void sendMsg2MainThread() {
        Message message = new Message();
        message.what = IntercomService.DISCOVERING_SEND;
        handler.sendMessage(message);
    }
}
