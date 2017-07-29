package com.yf.remotecontrolclient.adapt;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.domain.WebsiteList;
import com.yf.remotecontrolclient.util.DateUtil;


public class WebsiteAdapter extends BaseAdapter {
    private Context mContext;
    private TextView name_tv;
    private TextView name_tv_time;
    private View deleteView;
    WebsiteList websiteList;
    private boolean isShowDelete;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示

    public WebsiteAdapter(Context mContext,
                          WebsiteList websiteList) {
        this.mContext = mContext;
        this.websiteList = websiteList;
    }

    public void setIsShowDelete(boolean isShowDelete) {
        this.isShowDelete = isShowDelete;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return websiteList.getWebsites().size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return websiteList.getWebsites().get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.website_gridview_item,
                null);
        name_tv = (TextView) convertView.findViewById(R.id.name_tv);
        name_tv_time = (TextView) convertView.findViewById(R.id.name_tv_time);
        deleteView = convertView.findViewById(R.id.delete_markView);
        deleteView.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);// 设置删除按钮是否显示
        name_tv.setText(websiteList.getWebsites().get(position).getUrl());
        name_tv_time.setText(DateUtil.format(websiteList.getWebsites().get(position).getDate(), DateUtil.SHI_FEN));
        return convertView;
    }
}
