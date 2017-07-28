package com.yuanfang.intercom.users;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuanfang.intercom.R;

import java.util.List;

/**
 * Created by yanghao1 on 2017/4/13.
 */

public class IntercomAdapter extends RecyclerView.Adapter<IntercomAdapter.ViewHolder> {

    private List<IntercomUserBean> userBeanList;
    private int currentIndex = -1;

    public IntercomAdapter(List<IntercomUserBean> userBeanList) {
        this.userBeanList = userBeanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_intercom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IntercomUserBean user = userBeanList.get(position);
        holder.userName.setText(user.getAliasName() == null ?
                user.getIpAddress() : user.getAliasName());
        if (position == currentIndex) {
            holder.cardView.setCardBackgroundColor(Color.GREEN);
        } else {
            holder.cardView.setCardBackgroundColor(null);
        }
    }

    @Override
    public int getItemCount() {
        return userBeanList.size();
    }

    public void setCurrentIndex(int currentIndex) {
        if (currentIndex == 0)
            return;
        if (this.currentIndex != currentIndex) {
            this.currentIndex = currentIndex;
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final CardView cardView;
        final TextView userName;

        ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.intercom_user_name_tv);
            cardView = (CardView) itemView;
        }
    }
}
