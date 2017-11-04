/**
 * ZigbeeService.java[V 1.0.0]
 * @author xujie@yf-space.com create on 2015-6-17
 */

package com.yf.remotecontrolserver.serial.zigbee;

import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.yf.remotecontrolserver.serial.Analyzer;
import com.yf.remotecontrolserver.serial.ISSend;
import com.yf.yfpublic.utils.CommonLog;
import com.yf.yfpublic.utils.LogFactory;

import java.util.Arrays;


public class ZigbeeService extends AZigbeeEngine {
	private static final CommonLog log = LogFactory.createLog(ZigbeeService.class.getSimpleName());
	private byte[] mStoreBuffer; // �洢BUFFER����
	private int mPosition; // BUFFERĩβλ��
	private String mCommand;
	private String mParameter; // ��������
	private int mRoomNumber; // �����
	private Analyzer mAnalyzer;
//	private Executor mExecutor;
//	private FeedBacker mFeedBacker;

	@Override
	public void onCreate() {
		super.onCreate();
		log.d("onCreate");
//		mFeedBacker = new FeedBacker();
//		mFeedBacker.setIISend(ISend);

//		mExecutor = new Executor(this);
//		mExecutor.setISerial(mFeedBacker);

		mAnalyzer = new Analyzer();
		mAnalyzer.setISerial(IAnalyzer);

		mStoreBuffer = new byte[256];
		clearMask();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		log.d("onDestroy");
//		mExecutor.exit();
		super.onDestroy();
	}

	private Analyzer.ISerial IAnalyzer = new Analyzer.ISerial() {
		@Override
		public void callBack(int roomNumber, String function, String partemterString) {
			mRoomNumber = roomNumber;
			mCommand = function;
			mParameter = partemterString;
		}

		@Override
		public void initBuradrate(int buradRrate) {
			int burad = getBAUDRATE();
			if (burad != buradRrate) {
				setBAUDRATE(buradRrate);
				startService(new Intent(ZigbeeService.this, ZigbeeService.class));
			}
		}

		@Override
		public int getBuradrate() {
			return getBAUDRATE();
		}

		@Override
		public void callAccess(boolean suc) {
			if (suc) {
				new Handler(getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ZigbeeService.this, //
								"getResources().getString(R.string.zigbee_net_suc)", Toast.LENGTH_LONG).show();
					}
				});
			}
		}
	};

	/**
	 * �洢���ݺ��� --- ���ռ���0xFAΪ��ͷ��0xFEΪ��β������
	 * @return true:�ɹ�; false��ʧ��
	 */

	private boolean storeData(byte[] buffer, int size) {
		// �Ƚ���ʼ��
		if (0xFA == (buffer[0] & 0xFF)) {
			Arrays.fill(mStoreBuffer, (byte) 0xff);
			for (int i = 0; i < size; i++) {
				mStoreBuffer[i] = buffer[i];
			}
			mPosition = size;
			// log.i("[zigbee start] " + mPosition);
		} else {
			if (mPosition == 0) {
				log.d("[mPosition = 0] " + size);// ERROR_FB_UNUSUAL
				return false;
			}

			if (mPosition + size > 16 /*1023*/) {
				log.e("[exp data!!!] " + mPosition);
				return true;
			}

			for (int i = 0; i < size; i++) {
				mStoreBuffer[mPosition + i] = buffer[i];
			}
			mPosition += size;
		}

		// ��֤������
		if (0xFE == (buffer[size - 1] & 0xFF)) {
			// log.i("[End] " + mPosition);
			return true;// ��ȷ������ǣ�����һ�γɹ�����
		}
		// log.i("[Continue] " + mPosition);
		return false;
	}

	private void clearMask() {
		mPosition = 0;
		Arrays.fill(mStoreBuffer, (byte) 0xff);
	}

	private long preIniTime = System.currentTimeMillis();
	private long curIniTime;

	private void filterInitialize() {
		curIniTime = System.currentTimeMillis();
		if (curIniTime - preIniTime > 150 * 1000) {
			log.i("" + (curIniTime - preIniTime));
			clearMask();
		}
		preIniTime = curIniTime;
	}

	/** ���ζ���ָ����Ϊ */
	private long preInstructionTime = System.currentTimeMillis();
	private long curInstructionTime;

	private boolean filterFrequentlyInstruction() {
		curInstructionTime = System.currentTimeMillis();
		if (curInstructionTime - preInstructionTime < 49) {
			log.d("[frequently instruction " + (curInstructionTime - preInstructionTime) + "ms]");
			clearMask();
			return true;
		}
		preInstructionTime = curInstructionTime;
		return false;
	}

	@Override
	protected synchronized void analyzeData(final byte[] buffer, final int size) {
		// true - �ռ��ɹ���false - �ռ���ȫ
		// filterInitialize();

		if (storeData(buffer, size)) {
			mCommand = null;

			if (filterFrequentlyInstruction()) {
				clearMask();
				return;
			}

			mAnalyzer.handle(mStoreBuffer, mPosition);

			clearMask();

//			mExecutor.handle(mRoomNumber, mCommand, mParameter);
		}
	}

	private ISSend ISend = new ISSend() {
		@Override
		public void transferData(byte[] buffer, int size) {
			ZigbeeService.this.transferData(buffer, size);
		}
	};

	private final IBinder binder = new MyBinder();

	public class MyBinder extends Binder {
		public ZigbeeService getService() {
			return ZigbeeService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

}
