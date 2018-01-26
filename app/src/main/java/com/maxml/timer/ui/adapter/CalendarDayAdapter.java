package com.maxml.timer.ui.adapter;

import android.content.Context;
import android.support.transition.TransitionManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxml.timer.R;
import com.maxml.timer.entity.Action;
import com.maxml.timer.util.OptionButtons;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class CalendarDayAdapter extends RecyclerView.Adapter<CalendarDayAdapter.DayViewHolder> {
    private int mExpandedPosition = -1;

    private Context context;
    private List<Action> list;
    private List<OptionButtons> optionList;

    private OnClickListener listener;

    public interface OnClickListener {
        void onClickOption(OptionButtons optionType, Action item);

        void onClick(Action action);
    }

    public CalendarDayAdapter(Context context, List<Action> entityList,
                              List<OptionButtons> optionList, OnClickListener listener) {
        this.listener = listener;
        this.list = entityList;
        this.context = context;
        this.optionList = optionList;

        Collections.sort(list);
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DayViewHolder holder, final int position) {
        holder.bind(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void swapData(List<Action> actions) {
        list = actions;
        Collections.sort(list);

        notifyDataSetChanged();
    }

    public void resetList() {
        mExpandedPosition = -1;
        notifyDataSetChanged();
    }

    public class DayViewHolder extends RecyclerView.ViewHolder {
        private CardView cvView;
        private TextView tvTime;
        private TextView tvType;
        private TextView tvDescription;
        private RecyclerView rvOptions;

        public DayViewHolder(View itemView) {
            super(itemView);
            cvView = itemView.findViewById(R.id.rootCardView);
            tvTime = itemView.findViewById(R.id.time);
            tvType = itemView.findViewById(R.id.tvActionType);
            tvDescription = itemView.findViewById(R.id.tvActionDescription);
            rvOptions = itemView.findViewById(R.id.recyclerViewOptions);
        }

        public void bind(final Action action, final int position) {
            // tvTime
            if (action != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(action.getStartDate());
                NumberFormat f = new DecimalFormat("00");
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                String time = f.format(hour) + ":" + f.format(min);
                tvTime.setText(time);
                // tvDescription
                if (action.getDescription() != null && !action.getDescription().equals("")) {
                    tvDescription.setText(action.getDescription());
                } else {
                    tvDescription.setText(itemView.getContext().getString(R.string.action_default_description));
                }
                // tvType
                tvType.setText(action.getType());

                // options
                final boolean isExpanded = position == mExpandedPosition;
                if (isExpanded) {
                    rvOptions.setVisibility(View.VISIBLE);
                    showOptions(rvOptions, action);
                } else {
                    rvOptions.setVisibility(View.GONE);
                    rvOptions.setAdapter(null);
                }
                cvView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isExpanded) {
                            mExpandedPosition = -1;
                        } else {
                            mExpandedPosition = position;
                        }
                        notifyItemRemoved(position);
                        TransitionManager.beginDelayedTransition(rvOptions);
                        notifyDataSetChanged();
                        listener.onClick(action);
                    }
                });
            }
        }
    }

    private void showOptions(RecyclerView recyclerView, final Action entity) {
        // init RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        OptionButtonsAdapter adapter = new OptionButtonsAdapter(optionList, new OptionButtonsAdapter.OnClickButton() {
            @Override
            public void onClick(OptionButtons buttonType) {
                listener.onClickOption(buttonType, entity);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}