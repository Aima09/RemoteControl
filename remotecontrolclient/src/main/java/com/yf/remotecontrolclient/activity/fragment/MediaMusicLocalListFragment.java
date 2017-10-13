package com.yf.remotecontrolclient.activity.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yf.minalibrary.common.FileMessageConstant;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.media.MediaSource;
import com.yf.remotecontrolclient.media.model.Media;
import com.yf.remotecontrolclient.service.MusicBusinessService;
import com.yf.remotecontrolclient.service.imp.MusicBusinessServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sujuntao on 2017/8/17 .
 */

public class MediaMusicLocalListFragment extends Fragment implements View.OnClickListener{
    private static String TAG="MediaMusicLocalListFragment";
    private View mView;
    private Activity mActivity;
    private ListView mListView;
    private LocalMusicAdapter mLocalMusicAdapter;
    private MusicBusinessService mMusicBusinessService;
    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_music_local_list, null);
        mActivity=getActivity();
        initView();
        initData();
        return mView;
    }

    private void initView() {
        mListView=(ListView) mView.findViewById(R.id.local_music_list);
    }
    private void initData() {
        mLocalMusicAdapter=new LocalMusicAdapter(mActivity,MediaSource.getInstance().getMusicList());
        mListView.setAdapter(mLocalMusicAdapter);
        mMusicBusinessService=new MusicBusinessServiceImpl();
    }

    class LocalMusicAdapter extends BaseAdapter {
        protected LayoutInflater mInflater;
        private List<Media> mMediaList;
        @Override
        public int getCount() {
            return mMediaList.size();
        }

        @Override
        public Object getItem(int position) {
            return mMediaList.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        public LocalMusicAdapter(Context context, List<Media> list) {
            this.mMediaList = new ArrayList<Media>(list);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            Media media = mMediaList.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.fragment_music_local_list_item, null);
                viewHolder.musicName = (TextView) convertView.findViewById(R.id.music_name);
                viewHolder.musicZz = (TextView) convertView.findViewById(R.id.music_zz);
                viewHolder.btnTs = (Button) convertView.findViewById(R.id.btn_ts);
                viewHolder.btnSc = (Button) convertView.findViewById(R.id.btn_sc);
            }
            if(media==null){
                return null;
            }
            viewHolder.musicName.setText(media.getTitle());
            viewHolder.musicZz.setText(media.getArtist());
            viewHolder.btnTs.setTag(position);
            viewHolder.btnTs.setOnClickListener(MediaMusicLocalListFragment.this);
            viewHolder.btnSc.setTag(mMediaList.get(position).getPath());
            viewHolder.btnSc.setOnClickListener(MediaMusicLocalListFragment.this);
            return convertView;
        }

        public class ViewHolder {
            TextView musicName ;
            TextView musicZz ;
            Button btnTs ;
            Button btnSc ;
        }
    }

    @Override public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ts:
                Media m=MediaSource.getInstance().getMusicList().get((Integer) v.getTag());
                mMusicBusinessService.sendBsTsMusicFile(m);
                break;
            case R.id.btn_sc:
                //Log.i(TAG,"上传:"+v.getTag());
                mMusicBusinessService.sendBsMusicFile((String)v.getTag(), FileMessageConstant.UPLOAD_MUSIC);
                break;
        }
    }
}
