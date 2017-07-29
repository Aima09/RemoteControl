package com.yf.remotecontrolclient.dao.udp;


import com.yf.remotecontrolclient.activity.service.MouseService;
import com.yf.remotecontrolclient.dao.AnalyzerInterface;
import com.yf.remotecontrolclient.dao.UdpAnalyzerImpl;
import com.yf.remotecontrolclient.domain.Equipment;
import com.yf.remotecontrolclient.domain.Gateway;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
    //	private static String TAG = "UDPServer";
    private static UDPServer udpServer;

    public static UDPServer getInstans(Gateway gateway, Equipment equipment, AnalyzerInterface analyzerInterface) {
        if (udpServer == null) {
            udpServer = new UDPServer(gateway, equipment, analyzerInterface);
        }
        return udpServer;
    }

    private AnalyzerInterface analyzerInterface;
    private Gateway gateway;
    private Equipment equipment;

    private DatagramSocket datagramSocket;
    private Thread startThread;
    private Thread readThread;
    private Thread writThread;
    private byte[] wb;

    private boolean isStopRead = false;

    public UDPServer(Gateway gateway, Equipment equipment,
                     AnalyzerInterface analyzerInterface) {
        this.gateway = gateway;
        this.equipment = equipment;
        this.analyzerInterface = analyzerInterface;
    }

    public void send(byte[] data) {
        wb = data;
        if (writThread == null) {
            writThread = new Thread(new WriteRunnable());
            writThread.start();
        } else {
            if (writThread.isAlive()) {
                return;
            }
        }
    }

    public void stop() {
        new Thread() {
            public void run() {

                isStopRead = true;
                readThread = null;
                writThread = null;
                if (datagramSocket != null) {
                    datagramSocket.close();
                    datagramSocket = null;
                }
            }

            ;
        }.start();
    }

    public void start() {
        isStopRead = false;
        startThread = new Thread(new StartRunnable());
        startThread.start();
    }

    class WriteRunnable implements Runnable {
        @Override
        public void run() {
            if (datagramSocket != null) {
                try {
                    equipment = MouseService.equipment;
                    DatagramPacket writedatagramPacket = new DatagramPacket(wb,
                            wb.length,
                            InetAddress.getByName(equipment.getIp()),
                            gateway.getGwUdpPort());
                    datagramSocket.setBroadcast(true);
                    datagramSocket.send(writedatagramPacket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                writThread = null;
            } else {
            }
        }
    }

    class ReadRunnable implements Runnable {
        @Override
        public void run() {
            while (!isStopRead) {
                try {
                    if (datagramSocket != null) {
                        byte[] rb = new byte[1024];
                        DatagramPacket readdatagramPacket = new DatagramPacket(
                                rb, rb.length);
                        datagramSocket.receive(readdatagramPacket);
                        if (rb.length > 0) {
                            ((UdpAnalyzerImpl) analyzerInterface).analay(rb, readdatagramPacket.getAddress().getHostAddress());
                        }
                    }
                } catch (Exception e) {
                    // Log.i(TAG, "exception");
                    e.printStackTrace();
                }
            }
        }
    }

    class StartRunnable implements Runnable {
        @Override
        public void run() {
            try {
                datagramSocket = new DatagramSocket(7320);
                readThread = new Thread(new ReadRunnable());
                readThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
