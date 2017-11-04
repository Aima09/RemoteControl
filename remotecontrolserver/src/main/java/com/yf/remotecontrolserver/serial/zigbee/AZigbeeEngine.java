/**
 * AZigbeeEngine.java[V 1.0.0]
 * @author xujie@yf-space.com create on 2015-6-16
 */

package com.yf.remotecontrolserver.serial.zigbee;

import android.app.Service;
import android.content.Intent;


import com.yf.remotecontrolserver.serial.SerialPort;
import com.yf.yfpublic.utils.CommonLog;
import com.yf.yfpublic.utils.LogFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;


public abstract class AZigbeeEngine extends Service {
	private static final CommonLog log = LogFactory.createLog(AZigbeeEngine.class.getSimpleName());
	private SerialPort mSerialPort;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	private SendThread mSendThread;

	private byte[] mSendBuffer;

	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (true) {
				int size;
				try {
					synchronized (this) {

						byte[] buffer = new byte[64];
						if (mInputStream == null)
							return;
						size = mInputStream.read(buffer);
						// ////////
						System.out.print("--> ");
						for (int i = 0; i < size; i++) {
							System.out.print((buffer[i] & 0xFF) + " ");
						}
						System.out.println();
						// /////
						if (size > 0) {
							analyzeData(buffer, size);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	private class SendThread extends Thread {
		@Override
		public void run() {
			// while (!isInterrupted()) ;
			try {
				if (mOutputStream != null) {

					if (mSendBuffer != null) {
						byte[] buffer = mSendBuffer;
						mSendBuffer = null;
						mOutputStream.write(buffer);
						buffer = null;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				return;

			}
		}
	}

	protected abstract void analyzeData(final byte[] buffer, final int size);

	public void transferData(final byte[] buffer, final int size) {
		for (int i = 0; i < buffer.length; i++) {
			System.out.print((buffer[i] & 0xFF) + " ");
		}
		System.out.println();
		// System.out.println("zigbee send data");
		mSendBuffer = buffer;
		if (mSendThread == null) {
			mSendThread = new SendThread();
			mSendThread.start();
		} else {
			if (mSendThread.isAlive()) {
				log.e("mSendThread.isAlive() !!!");
				return;
			}
			mSendThread.run();
		}
		// new SendThread().start();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	private void init() {
		BAUDRATE=9600;
		/*if (config_server.isSuokete()) {
			BAUDRATE = PreferenceUtils.getPrefInt(this, config_server.RS232_BAUDRATE, 4800);
		} else {
			BAUDRATE = PreferenceUtils.getPrefInt(this, config_server.RS232_BAUDRATE, 9600);
		}*/
		log.d("current baudrate is " + BAUDRATE);
		mSendBuffer = new byte[1024];

//		if (config_server.isHuaLi()) {
//			return;
//		}

		try {
			mSerialPort = new SerialPort(new File("/dev/ttyS3"), BAUDRATE, 0);
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
			mReadThread = new ReadThread();
			mReadThread.start();

			log.i("Create Zigbee Port -> ttyS3, bps " + getBAUDRATE());
		} catch (SecurityException e) {
			log.e("getResources().getString(R.string.error_security)");
		} catch (IOException e) {
			log.e("getResources().getString(R.string.error_unknown)");
		} catch (InvalidParameterException e) {
			log.e("getResources().getString(R.string.error_configuration)");
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		log.d("onStartCommand");
		if (intent != null && intent.getBooleanExtra("buradRrate", false)) {
			log.d("onStartCommand init");
			init();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		log.d("onDestroy!!");
		// if (mReadThread != null) {
		// mReadThread.stop();
		// }
		//
		// if (mSendThread != null) {
		// mSendThread.stop();
		// }
		// stopSelf();
		try {
			mInputStream.close();
			mInputStream = null;

			mOutputStream.close();
			mOutputStream = null;
		} catch (Exception e) {
		}
	}

	private int BAUDRATE;

	public int getBAUDRATE() {
		return BAUDRATE;
	}

	public void setBAUDRATE(int bAUDRATE) {
		BAUDRATE = bAUDRATE;
		//PreferenceUtils.setPref(this, config_server.RS232_BAUDRATE, bAUDRATE);
	}
}
