package com.yf.remotecontrolclient.adapt;


import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.domain.Video;
import com.yf.remotecontrolclient.util.MyThumbnailUtils;

import java.util.List;

public class VideoAdapter extends BaseAdapter {
    private List<Video> videos;

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // ViewHolder静态类
    static class ViewHolder {
        public TextView title;
        public ImageView thumbnail;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // 如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            holder = new ViewHolder();
            // 根据自定义的Item布局加载布局
            convertView = View.inflate(App.getAppContext(), R.layout.activity_video_list_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.video_name);
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.iv_video_thumbnail);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(videos.get(position).getVideoname());
        byte[] imageb= Base64.decode(videos.get(position).getB(), Base64.DEFAULT);
        holder.thumbnail.setImageBitmap(MyThumbnailUtils.Bytes2Bimap(imageb));
        return convertView;
    }
}
