package com.yf.remotecontrolclient.adapt;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.domain.File;

public class FileShowListAdapter extends BaseAdapter {
    private List<File> files;
    private List<Integer> images = new ArrayList<Integer>();
    private Integer imagePosition = 0;

    public FileShowListAdapter() {
        super();
        images.add(R.drawable.music1);
        images.add(R.drawable.vidio1);
        images.add(R.drawable.image);
        images.add(R.drawable.text1);
        images.add(R.drawable.archive_yellow);
        images.add(R.drawable.ic_launcher);
        images.add(R.drawable.sd);
    }

    public Integer getImagePosition() {
        return imagePosition;
    }

    public void setImagePosition(Integer imagePosition) {
        this.imagePosition = imagePosition;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // ViewHolder静态类
    static class ViewHolder {
        public TextView name;
        public ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // 如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            holder = new ViewHolder();
            // 根据自定义的Item布局加载布局
            convertView = View.inflate(App.getAppContext(), R.layout.file_show_list_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_file_name);
            holder.imageView = (ImageView) convertView.findViewById(R.id.id_icon);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(files.get(position).getFileName());
        holder.imageView.setImageResource(images.get(imagePosition));
        return convertView;
    }
}
