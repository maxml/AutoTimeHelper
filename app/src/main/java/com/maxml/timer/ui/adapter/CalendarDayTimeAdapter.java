package com.maxml.timer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxml.timer.R;
import com.maxml.timer.entity.Action;
import com.maxml.timer.util.Utils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CalendarDayTimeAdapter extends RecyclerView.Adapter<CalendarDayTimeAdapter.DayViewHolder> {
    private List<Date> dates;
    private Context context;

    public CalendarDayTimeAdapter(Context context, List<Date> dates) {
        this.dates = dates;
        this.context = context;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_time_fragment, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DayViewHolder holder, final int position) {
        Date date = dates.get(holder.getAdapterPosition());
        int itemPosition = holder.getAdapterPosition() + 1;
        holder.tvTime.setText(Utils.parseToTime(date.getTime()));
        if (itemPosition == dates.size()) {
            setMargin(500, holder.cardView);
        } else {
            if (itemPosition % 3 == 0) {
                setMargin(110, holder.cardView);
            } else {
                setMargin(0, holder.cardView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(dates, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(dates, i, i - 1);
            }
        }
        notifyDataSetChanged();
        return true;
    }

    public void swapData(List<Date> dates) {
        this.dates = dates;
        notifyDataSetChanged();
    }

    class DayViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime;
        private CardView cardView;

        DayViewHolder(View itemView) {
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