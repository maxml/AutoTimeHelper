package com.maxml.timer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxml.timer.R;
import com.maxml.timer.entity.actions.Action;
import com.maxml.timer.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActionnsAdapter extends RecyclerView.Adapter<ActionnsAdapter.ViewHolder> {
    private List<Action> actionList = new ArrayList<>();

    public ActionnsAdapter(List<Action> actionList) {
        this.actionList = actionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_action, parent);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(actionList.get(position));
    }

    @Override
    public int getItemCount() {
        return actionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvType;
        private TextView tvDescription;
        private TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);

            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }

        public void bind(Action action) {
            bindTvType(action.getType());
            tvDescription.setText(action.getDescription());
        }

        public void bindTvType(String type) {
            tvType.setText(type);
            if (type.equalsIgnoreCase(Constants.EVENT_CALL_ACTION)) {
                tvType.setBackgroundResource(R.drawable.button_call);
            } else if (type.equalsIgnoreCase(Constants.EVENT_REST_ACTION)) {
                tvType.setBackgroundResource(R.drawable.button_rest);
            } else if (type.equalsIgnoreCase(Constants.EVENT_WALK_ACTION)) {
                tvType.setBackgroundResource(R.drawable.button_walk);
            } else if (type.equalsIgnoreCase(Constants.EVENT_WORK_ACTION)) {
                tvType.setBackgroundResource(R.drawable.button_work);
            }
        }
    }
}
