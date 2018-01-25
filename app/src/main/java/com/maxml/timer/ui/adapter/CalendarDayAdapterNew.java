package com.maxml.timer.ui.adapter;

import android.content.Context;
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
import com.maxml.timer.util.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class CalendarDayAdapterNew extends RecyclerView.Adapter<CalendarDayAdapterNew.DayViewHolder> {
    private int mExpandedPosition = -1;

    private Context context;
    private List<Action> list;
    private List<OptionButtons> optionList;

    private OnClickListener listener;

    public interface OnClickListener {
        void onClickOption(OptionButtons optionType, Action item);

        void onClick(Action action);
    }

    public CalendarDayAdapterNew(Context context, List<Action> entityList,
                                 List<OptionButtons> optionList, OnClickListener listener) {
        this.listener = listener;
        this.list = entityList;
        this.context = context;
        this.optionList = optionList;
//        Collections.sort(list);
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_fragment, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DayViewHolder holder, final int position) {
        final int itemPosition = holder.getAdapterPosition();
        Action action = list.get(itemPosition);
        switch (itemPosition) {
            case 0:
                setMargin(0, holder.cardView);
                break;
            case 1:
                setMargin(30, holder.cardView);
                break;
            case 2:
                setMargin(60, holder.cardView);
                break;
            default:
                setMargin(0, holder.cardView);
                break;
        }

        // tvType
        holder.tvType.setText(action.getType());
        // tvTime
//        holder.tvTime.setText(Utils.parseToTime(action.getStartDate().getTime()-action.getEndDate().getTime()));
        holder.tvTime.setText(Utils.parseToTime(action.getStartDate().getTime()));
//        // tvDescription
//        if (action.getDescription() != null && !action.getDescription().equals("")) {
//            holder.tvTime.setText(action.getDescription());
//        } else {
//            holder.tvTime.setText(R.string.action_default_description);
//        }

//                // options
//                final boolean isExpanded = position == mExpandedPosition;
//                if (isExpanded) {
//                    rvOptions.setVisibility(View.VISIBLE);
//                    showOptions(rvOptions, action);
//                } else {
//                    rvOptions.setVisibility(View.GONE);
//                    rvOptions.setAdapter(null);
//                }
//                cardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (isExpanded) {
//                            mExpandedPosition = -1;
//                        } else {
//                            mExpandedPosition = position;
//                        }
//                        notifyItemRemoved(position);
//                        TransitionManager.beginDelayedTransition(rvOptions);
//                        notifyDataSetChanged();
//                        listener.onClick(action);
//                    }
//                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void swapData(List<Action> actions) {
        list = actions;
//        Collections.sort(list);
        notifyDataSetChanged();
    }

    public void resetList() {
        mExpandedPosition = -1;
        notifyDataSetChanged();
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public class DayViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tvTime;
        private TextView tvType;
//        private RecyclerView rvOptions;

        public DayViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardVied);
            tvTime = itemView.findViewById(R.id.duration);
            tvType = itemView.findViewById(R.id.action);
//            rvOptions = itemView.findViewById(R.id.recyclerViewOptions);
        }
    }

    private void setMargin(int margin, View view) {
        CardView.LayoutParams params = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(convertToDp(2), convertToDp(margin), convertToDp(2), convertToDp(60));
        view.setLayoutParams(params);
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

    private int convertToDp(int input) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (input * scale + 0.5f);
    }
}