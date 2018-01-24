package com.maxml.timer.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxml.timer.R;
import com.maxml.timer.entity.Action;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ViewHolder> {
    private List<Action> actionList = new ArrayList<>();

    public ActionsAdapter(List<Action> actionList) {
        this.actionList = actionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_action, parent);
        return new ViewHolder(rootView);
    }

    public void swapData(List<Action> actionList) {
        this.actionList = actionList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Action action = actionList.get(position);
        holder.tvType.setText(action.getType());
        switch (action.getType()){
            case Constants.EVENT_CALL_ACTION:
                holder.cardView.setBackgroundResource(R.drawable.button_call);
                break;
            case Constants.EVENT_REST_ACTION:
                holder.cardView.setBackgroundResource(R.drawable.button_rest);
                break;
            case Constants.EVENT_WALK_ACTION:
                holder.cardView.setBackgroundResource(R.drawable.button_walk);
                break;
            case Constants.EVENT_WORK_ACTION:
                holder.cardView.setBackgroundResource(R.drawable.button_work);
                break;
        }
        holder.tvDuration.setText(Utils.getTimeSubscribe(action.getStartDate(), action.getEndDate()));
    }

    @Override
    public int getItemCount() {
        return actionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvType;
        private TextView tvDuration;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.action);
            tvDuration = itemView.findViewById(R.id.duration);
            cardView = itemView.findViewById(R.id.cardVied);
        }
    }
}
