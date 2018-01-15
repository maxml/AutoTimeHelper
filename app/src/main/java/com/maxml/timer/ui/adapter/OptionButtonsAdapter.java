package com.maxml.timer.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
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
        button.setBackground(null);
        button.setTextSize(8);

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

    public class DayViewHolder extends RecyclerView.ViewHolder {
        private Button bOption;

        public DayViewHolder(Button bOption) {
            super(bOption);
            this.bOption = bOption;
        }

        public void bind(int position) {
            final OptionButtons optionButtons = optionButtonsList.get(position);

            switch (optionButtons) {
                case EDIT:
                    bOption.setId(Constants.ID_BUTTON_EDIT);
                    bOption.setText(R.string.opyion_button_edit);
                    break;
                case DELETE:
                    bOption.setId(Constants.ID_BUTTON_DELETE);
                    bOption.setText(R.string.opyion_button_delete);
                    break;
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
}
