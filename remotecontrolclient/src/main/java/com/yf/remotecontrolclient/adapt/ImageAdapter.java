package com.yf.remotecontrolclient.adapt;


import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.domain.Image;
import com.yf.remotecontrolclient.util.MyThumbnailUtils;

public class ImageAdapter extends BaseAdapter {
    private List<Image> images;

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    /**
     * 添加并刷新数据
     * @param list
     */
    public void addAndrefresh(List<Image> list) {
        this.images.addAll(list);
        this.notifyDataSetChanged();
    }

    /**
     * 清除数据
     */
    public void clear() {
        this.images.clear();
        this.notifyDataSetChanged();
    }

    /**
     * 数据数量
     * @return
     */
    public int getDataCount(){
        return this.images.size();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // ViewHolder静态类
    static class ViewHolder {
        public ImageView imageView;
        public TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // 如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            holder = new ViewHolder();
            // 根据自定义的Item布局加载布局
            convertView = View.inflate(App.getAppContext(), R.layout.image_list_view_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.image_name);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_image_thumbnail);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(images.get(position).getName());
        byte[] imageb= Base64.decode(images.get(position).getB(), Base64.DEFAULT);
        holder.imageView.setImageBitmap(MyThumbnailUtils.Bytes2Bimap(imageb));
        return convertView;
    }
}
