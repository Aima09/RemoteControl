package com.yf.remotecontrolclient.adapt;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.domain.FileCategory;

import java.util.ArrayList;
import java.util.List;

public class FileCategoryAdapter extends BaseAdapter {
    private List<FileCategory> fileCategories;
    private static List<Integer> images = new ArrayList<Integer>();

    public FileCategoryAdapter() {
        super();
        images.add(R.drawable.music1);
        images.add(R.drawable.vidio1);
        images.add(R.drawable.image);
        images.add(R.drawable.text1);
        images.add(R.drawable.archive_yellow);
        images.add(R.drawable.ic_launcher);
        images.add(R.drawable.sd);
        images.add(R.drawable.sd);
    }

    public List<FileCategory> getFileCategories() {
        return fileCategories;
    }

    public void setFileCategories(List<FileCategory> fileCategories) {
        this.fileCategories = fileCategories;
    }

    @Override
    public int getCount() {
        return fileCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return fileCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // ViewHolder静态类
    static class ViewHolder {
        public ImageView icon;
        public TextView filename;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // 如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            holder = new ViewHolder();
            // 根据自定义的Item布局加载布局
            convertView = View.inflate(App.getAppContext(), R.layout.file_category_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.id_icon);
            holder.filename = (TextView) convertView.findViewById(R.id.id_filename);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.icon.setImageResource(images.get(position));
        holder.filename.setText(fileCategories.get(position).getTitle());
        return convertView;
    }
}
