package com.yuanfang.intercom.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.yuanfang.intercom.R;
import com.yuanfang.intercom.service.IIntercomCallback;
import com.yuanfang.intercom.users.IntercomAdapter;
import com.yuanfang.intercom.users.IntercomUserBean;
import com.yuanfang.intercom.users.VerticalSpaceItemDecoration;
import com.yuanfang.intercom.util.IPUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView localNetworkUser;
    private TextView currentIp;

    private List<IntercomUserBean> userBeanList = new ArrayList<>();
    private IntercomAdapter intercomAdapter;

    /**
     * 被调用的方法运行在Binder线程池中，不能更新UI
     */
    private IIntercomCallback intercomCallback = new IIntercomCallback.Stub() {
        @Override
        public void findNewUser(String ipAddress) throws RemoteException {
            sendMsg2MainThread(ipAddress, FOUND_NEW_USER);
        }

        @Override
        public void removeUser(String ipAddress) throws RemoteException {
            sendMsg2MainThread(ipAddress, REMOVE_USER);
        }
    };

    private static final int FOUND_NEW_USER = 0;
    private static final int REMOVE_USER = 1;

    /**
     * 跨进程回调更新界面
     */
    private static class DisplayHandler extends Handler {
        // 弱引用
        private WeakReference<MainActivity> activityWeakReference;

        DisplayHandler(MainActivity audioActivity) {
            activityWeakReference = new WeakReference<>(audioActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity = activityWeakReference.get();
            if (activity != null) {
                if (msg.what == FOUND_NEW_USER) {
                    activity.foundNewUser((String) msg.obj);
                } else if (msg.what == REMOVE_USER) {
                    activity.removeExistUser((String) msg.obj);
                }
            }
        }
    }

    private Handler handler = new DisplayHandler(this);

    /**
     * 发送Handler消息
     *
     * @param content 内容
     * @param msgWhat 消息类型
     */
    private void sendMsg2MainThread(String content, int msgWhat) {
        Message msg = new Message();
        msg.what = msgWhat;
        msg.obj = content;
        handler.sendMessage(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        // 设置用户列表
        localNetworkUser = (RecyclerView) findViewById(R.id.activity_audio_local_network_user_rv);
        localNetworkUser.setLayoutManager(new LinearLayoutManager(this));
        localNetworkUser.addItemDecoration(new VerticalSpaceItemDecoration(10));
        localNetworkUser.setItemAnimator(new DefaultItemAnimator());
        intercomAdapter = new IntercomAdapter(userBeanList);
        localNetworkUser.setAdapter(intercomAdapter);
        localNetworkUser.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View item = rv.findChildViewUnder(e.getX(),e.getY());
                int position = rv.getChildLayoutPosition(item);
                if (position == -1)
                    return false;
                if (intercomService == null) {
                    Log.e(TAG, "onInterceptTouchEvent: intercomService is null, wait a minute.");
                    return false;
                }

                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "------>>>> start record ..." + position);
                        try {
                            intercomService.startRecord(userBeanList.get(position).getIpAddress());
                        } catch (RemoteException re) {
                            re.printStackTrace();
                        }
                        intercomAdapter.setCurrentIndex(position);
                        break;
                    case MotionEvent.ACTION_UP:
                        try {
                            intercomService.stopRecord();
                        } catch (RemoteException re) {
                            re.printStackTrace();
                        }
                        Log.d(TAG, "------>>>> stop record ... -1");
                        intercomAdapter.setCurrentIndex(-1);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
        // 添加自己
        addNewUser(new IntercomUserBean(IPUtil.getLocalIPAddress(), "本机"));
        // 设置当前IP地址
        currentIp = (TextView) findViewById(R.id.activity_audio_current_ip);
        currentIp.setText(IPUtil.getLocalIPAddress());
    }

    private void initData() {
    }

    @Override
    public void serviceConnected() {
        try {
            intercomService.registerCallback(intercomCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serviceDisconnected() {
        try {
            intercomService.unRegisterCallback(intercomCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新自身IP
     */
    public void updateMyself() {
        currentIp.setText(IPUtil.getLocalIPAddress());
    }

    private void foundNewUser(String ipAddress) {
        IntercomUserBean userBean = new IntercomUserBean(ipAddress);
        if (!userBeanList.contains(userBean)) {
            addNewUser(userBean);
        }
    }

    private void removeExistUser(final String ipAddress) {
        IntercomUserBean userBean = new IntercomUserBean(ipAddress);
        if (userBeanList.contains(userBean)) {
            int position = userBeanList.indexOf(userBean);
            userBeanList.remove(position);
            intercomAdapter.notifyItemRemoved(position);
            intercomAdapter.notifyItemRangeChanged(0, userBeanList.size());
        }
    }

    private void addNewUser(IntercomUserBean userBean) {
        if (!userBeanList.contains(userBean)) {
            userBeanList.add(userBean);
            intercomAdapter.notifyItemInserted(userBeanList.size() - 1);
        }
    }
}
