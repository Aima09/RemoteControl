package com.yf.remotecontrolclient.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.activity.fragment.MediaMusicLocalListFragment;
import com.yf.remotecontrolclient.activity.fragment.MediaMusicRemotListFragment;
import com.yf.remotecontrolclient.activity.fragment.MediaVideoLocalListFragment;
import com.yf.remotecontrolclient.activity.fragment.MediaVideoRemotListFragment;
import com.yf.remotecontrolclient.activity.view.Indicator;
import com.yf.remotecontrolclient.activity.view.ScrollRelativeLayout;
import com.yf.remotecontrolclient.adapt.VideoAdapter;
import com.yf.remotecontrolclient.domain.Setplayvideoid;
import com.yf.remotecontrolclient.domain.Setvideoplaystatus;
import com.yf.remotecontrolclient.domain.Setvideovolumeadd;
import com.yf.remotecontrolclient.domain.Video;
import com.yf.remotecontrolclient.domain.VideoList;
import com.yf.remotecontrolclient.media.MediaSource;
import com.yf.remotecontrolclient.media.model.Media;
import com.yf.remotecontrolclient.service.MusicBusinessService;
import com.yf.remotecontrolclient.service.VideoBusinessService;
import com.yf.remotecontrolclient.service.imp.MusicBusinessServiceImpl;
import com.yf.remotecontrolclient.service.imp.VideoBusinessServiceImpl;
import com.yf.remotecontrolclient.util.MyThumbnailUtils;


public class MediaVideoActivity extends BaseActivity  implements ViewPager.OnPageChangeListener, View.OnClickListener  {
    public final String TAG = "MediaVideoActivity";
    private ScrollRelativeLayout mMainContainer;
    private Indicator mIndicator;
    public final static String MBROADCASTRECEIVER = "com.yf.client.activity.MediaVideoActivity.mBroadcastReceiver.video";
    private TextView mLocalTextView;
    private TextView mSearchTextView;
    private ViewPager mVpMusicList;

    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        initspinner();
        initView();
        initData();
    }

    private void initData() {
        MediaVideoRemotListFragment mMediaVideoRemotListFragment = new MediaVideoRemotListFragment();
        MediaVideoLocalListFragment mMediaVideoLocalListFragment = new MediaVideoLocalListFragment();

        mFragments.add(mMediaVideoRemotListFragment);
        mFragments.add(mMediaVideoLocalListFragment);
        mVpMusicList.setAdapter(mPagerAdapter);
        //初始默认选中第0
        selectTab(0);
    }

    private void initView() {
        mMainContainer = (ScrollRelativeLayout) findViewById(R.id.rl_main_container);
        mIndicator = (Indicator) findViewById(R.id.main_indicator);
        mLocalTextView = (TextView) findViewById(R.id.tv_main_local);
        mSearchTextView = (TextView) findViewById(R.id.tv_main_remote);
        mVpMusicList = (ViewPager) findViewById(R.id.vp_music_list);

        mVpMusicList.addOnPageChangeListener(this);
        mLocalTextView.setOnClickListener(this);
        mSearchTextView.setOnClickListener(this);
    }

    private FragmentPagerAdapter mPagerAdapter =
            new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public int getCount() {
                    return mFragments.size();
                }
                @Override
                public Fragment getItem(int position) {
                    return mFragments.get(position);
                }
            };

    /**
     * 切换导航indicator
     * 设置文字颜色
     *
     * @param index
     */
    private void selectTab(int index) {
        switch (index) {
            case 0:
                mLocalTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.mediaMusicList));
                mSearchTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.mediaMusicList_dark));
                break;
            case 1:
                mLocalTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.mediaMusicList_dark));
                mSearchTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.mediaMusicList));
                break;
        }
    }

    /**
     * OnPageChangeListener
     */
    @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mIndicator.scroll(position, positionOffset);
    }

    @Override public void onPageSelected(int position) {
        selectTab(position);
    }

    @Override public void onPageScrollStateChanged(int state) {

    }

    /**
     * OnClickListener
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main_local:
                mVpMusicList.setCurrentItem(0);
                break;
            case R.id.tv_main_remote:
                mVpMusicList.setCurrentItem(1);
                break;
        }
    }
}