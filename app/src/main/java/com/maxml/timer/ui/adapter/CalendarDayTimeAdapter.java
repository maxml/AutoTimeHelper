package com.maxml.timer.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxml.timer.R;
import com.maxml.timer.entity.Action;
import com.maxml.timer.util.OptionButtons;
import com.maxml.timer.util.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class CalendarDayTimeAdapter extends RecyclerView.Adapter<CalendarDayTimeAdapter.DayViewHolder> {
    private List<Action> actions;
    private Context context;

    public CalendarDayTimeAdapter(Context context, List<Action> actions) {
        this.actions = actions;
        this.context = context;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_time_fragment, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DayViewHolder holder, final int position) {
        Action action = actions.get(holder.getAdapterPosition());
        int itemPosition = holder.getAdapterPosition() + 1;
        holder.tvTime.setText(Utils.parseToTime(action.getStartDate().getTime()));
        if (itemPosition == actions.size()) {
            setMargin(90 + 60, holder.cardView);
        } else {
            if (itemPosition % 3 == 0) {
                setMargin(90, holder.cardView);
            } else {
                setMargin(0, holder.cardView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

    public class DayViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime;
        private CardView cardView;

        public DayViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.duration);
            cardView = itemView.findViewById(R.id.cardVied);
        }
    }

    private void setMargin(int margin, View view) {
        CardView.LayoutParams params = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(convertToDp(2), 0, convertToDp(2), convertToDp(margin));
        view.setLayoutParams(params);
    }

    private int convertToDp(int input) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (input * scale + 0.5f);
    }
}