package com.yf.remotecontrolclient.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.activity.fragment.MediaVideoLocalListFragment;
import com.yf.remotecontrolclient.activity.fragment.MediaVideoRemotListFragment;
import com.yf.remotecontrolclient.activity.view.Indicator;
import com.yf.remotecontrolclient.activity.view.ScrollRelativeLayout;


public class MediaVideoActivity extends BaseActivity  implements ViewPager.OnPageChangeListener, View.OnClickListener  {
    public final String TAG = "MediaVideoActivity";
    private ScrollRelativeLayout mMainContainer;
    private Indicator mIndicator;
    public final static String MBROADCASTRECEIVER = "com.yf.client.activity.MediaVideoActivity.mBroadcastReceiver.video";
    private TextView mLocalTextView;
    private TextView mSearchTextView;
    private ViewPager mVpVideoList;

    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_viewpager);
        initspinner();
        initView();
        initData();
    }

    private void initData() {
        MediaVideoRemotListFragment mMediaVideoRemotListFragment = new MediaVideoRemotListFragment();
        MediaVideoLocalListFragment mMediaVideoLocalListFragment = new MediaVideoLocalListFragment();

        mFragments.add(mMediaVideoRemotListFragment);
        mFragments.add(mMediaVideoLocalListFragment);
        mVpVideoList.setAdapter(mPagerAdapter);
        //初始默认选中第0
        selectTab(0);
    }

    private void initView() {
        mMainContainer = (ScrollRelativeLayout) findViewById(R.id.rl_main_container);
        mIndicator = (Indicator) findViewById(R.id.main_indicator);
        mLocalTextView = (TextView) findViewById(R.id.tv_main_local);
        mSearchTextView = (TextView) findViewById(R.id.tv_main_remote);
        mVpVideoList = (ViewPager) findViewById(R.id.vp_music_list);

        mVpVideoList.addOnPageChangeListener(this);
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
                mVpVideoList.setCurrentItem(0);
                break;
            case R.id.tv_main_remote:
                mVpVideoList.setCurrentItem(1);
                break;
        }
    }
}