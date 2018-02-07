package com.maxml.timer.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxml.timer.R;
import com.maxml.timer.entity.WifiState;
import com.maxml.timer.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {
    private List<WifiState> list = new ArrayList<>();

    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(WifiState wifiState);
    }

    public WifiAdapter(List<WifiState> list, OnItemClickListener clickListener) {
        this.list = list;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void swapData(List<WifiState> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivType;
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);

            ivType = itemView.findViewById(R.id.iv_type);
            tvName = itemView.findViewById(R.id.tv_name);
        }

        public void bind(final WifiState wifiState) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(wifiState);
                }
            });

            if (wifiState.getType() == Constants.WIFI_TYPE_HOME) {
                ivType.setBackgroundResource(R.drawable.ic_home);
            } else if (wifiState.getType() == Constants.WIFI_TYPE_WORK) {
                ivType.setBackgroundResource(R.drawable.ic_work);
            } else {
                ivType.setBackground(null);
            }
            tvName.setText(wifiState.getName().substring(1, wifiState.getName().length() - 1));
        }
    }
}
