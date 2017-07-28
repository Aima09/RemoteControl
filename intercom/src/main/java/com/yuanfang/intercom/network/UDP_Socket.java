package com.yuanfang.intercom.network;

import com.yuanfang.intercom.util.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by xuie on 17-7-13.
 */

public class UDP_Socket {
    private static UDP_Socket instance;
    private DatagramSocket socket = null;

    private UDP_Socket() {
        try {
            socket = new DatagramSocket(Constants.UNICAST_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static UDP_Socket get() {
        if (instance == null)
            synchronized (UDP_Socket.class) {
                instance = new UDP_Socket();
            }
        return instance;
    }

    public void send(DatagramPacket packet) throws IOException {
        socket.send(packet);
    }

    public void receive(DatagramPacket packet) throws IOException {
        socket.receive(packet);
    }

}
