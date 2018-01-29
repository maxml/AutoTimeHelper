package com.maxml.timer.ui.adapter;

import android.content.Context;
import android.support.transition.TransitionManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxml.timer.R;
import com.maxml.timer.entity.Action;
import com.maxml.timer.util.OptionButtons;
import com.maxml.timer.util.SharedPreferencesUtils;
import com.maxml.timer.util.Utils;

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
        final Action action = list.get(itemPosition);
        switch (itemPosition) {
            case 0:
                setTopMargin(0, holder.cardView);
                break;
            case 1:
                setTopMargin(30, holder.cardView);
                break;
            case 2:
                setTopMargin(60, holder.cardView);
                break;
            default:
                setTopMargin(0, holder.cardView);
                break;
        }

        // tvType
        holder.tvType.setText(action.getType());
        // tvTime
        long time = action.getEndDate().getTime() - action.getStartDate().getTime();
        holder.tvTime.setText(Utils.parseToTimeDuration(context, time));

//        // tvDescription
//        if (action.getDescription() != null && !action.getDescription().equals("")) {
//            holder.tvTime.setText(action.getDescription());
//        } else {
//            holder.tvTime.setText(R.string.action_default_description);
//        }

        int color = SharedPreferencesUtils.getColor(context, action.getType());
        holder.actionLayout.setBackgroundColor(color);
        holder.rvOptions.setBackgroundColor(color);

        // options
        final boolean isExpanded = position == mExpandedPosition;
        if (isExpanded) {
            holder.rvOptions.setVisibility(View.VISIBLE);
            setBottomMargin(2, holder.cardView);
            showOptions(holder.rvOptions, action);
        } else {
            holder.rvOptions.setVisibility(View.GONE);
            setBottomMargin(80, holder.cardView);
            holder.rvOptions.setAdapter(null);
        }
        holder.actionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    mExpandedPosition = -1;
                } else {
                    mExpandedPosition = position;
                }
                notifyItemRemoved(position);
                TransitionManager.beginDelayedTransition(holder.rvOptions);
                notifyDataSetChanged();
                listener.onClick(action);
            }
        });
    }

    public void closeExpandableOption() {
        if (mExpandedPosition != -1) {
            int expandedItem = mExpandedPosition;
            mExpandedPosition = -1;
            notifyItemChanged(expandedItem);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void swapData(List<Action> actions) {
        list = actions;
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
                notifyItemChanged(i);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
                notifyItemChanged(i);
            }
        }
        notifyItemChanged(toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public class DayViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tvTime;
        private TextView tvType;
        private RelativeLayout actionLayout;
        private RecyclerView rvOptions;

        public DayViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardVied);
            tvTime = itemView.findViewById(R.id.duration);
            tvType = itemView.findViewById(R.id.action);
            actionLayout = itemView.findViewById(R.id.rootRelativeLayout);
            rvOptions = itemView.findViewById(R.id.recyclerViewOptions);
        }
    }

    private void setTopMargin(int margin, CardView view) {
        StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        params.topMargin = convertToDp(margin);
    }

    private void setBottomMargin(int margin, CardView view) {
        StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        params.bottomMargin = convertToDp(margin);
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