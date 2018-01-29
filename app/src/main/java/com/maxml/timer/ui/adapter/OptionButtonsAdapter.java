package com.maxml.timer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.maxml.timer.R;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.OptionButtons;

import java.util.List;

public class OptionButtonsAdapter extends RecyclerView.Adapter<OptionButtonsAdapter.DayViewHolder> {

    private int lastPosition = -1;

    private List<OptionButtons> optionButtonsList;
    private OnClickButton listener;

    public interface OnClickButton {
        void onClick(OptionButtons buttonType);
    }

    public OptionButtonsAdapter(List<OptionButtons> buttonsList, OnClickButton listener) {
        optionButtonsList = buttonsList;
        this.listener = listener;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Button button = new Button(parent.getContext());
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, convertToDp(parent.getContext(),30));
        lp.setMargins(0,0,0,0);
        button.setLayoutParams(lp);
        button.setBackground(null);
        button.setTextSize(10);
        button.setPadding(0,0,0,0);
        return new DayViewHolder(button);
    }

    @Override
    public void onBindViewHolder(final DayViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return optionButtonsList.size();
    }

    class DayViewHolder extends RecyclerView.ViewHolder {
        private Button bOption;

        DayViewHolder(Button bOption) {
            super(bOption);
            this.bOption = bOption;
        }

        void bind(int position) {
            final OptionButtons optionButtons = optionButtonsList.get(position);

            switch (optionButtons) {
                case EDIT:
                    bOption.setId(Constants.ID_BUTTON_ACTION_EDIT);
                    bOption.setText(R.string.opyion_button_edit);
                    break;
                case DELETE:
                    bOption.setId(Constants.ID_BUTTON_ACTION_DELETE);
                    bOption.setText(R.string.opyion_button_delete);
                    break;
                case JOIN:
                    bOption.setId(Constants.ID_BUTTON_ACTION_JOIN);
                    bOption.setText(R.string.opyion_button_join);
            }
            bOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(optionButtons);
                }
            });
            lastPosition = position;
        }
    }

    private int convertToDp(Context context, int input) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (input * scale + 0.5f);
    }

}
