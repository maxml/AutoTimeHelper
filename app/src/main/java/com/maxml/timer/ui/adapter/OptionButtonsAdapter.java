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
    private int textSize;

    private List<OptionButtons> optionButtonsList;
    private OnClickButton listener;

    public interface OnClickButton {
        void onClick(OptionButtons buttonType);
    }

    public OptionButtonsAdapter(List<OptionButtons> buttonsList, OnClickButton listener, int textSize) {
        optionButtonsList = buttonsList;
        this.listener = listener;
        this.textSize = textSize;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Button button = new Button(parent.getContext());
        button.setTextSize(textSize);
        button.setBackground(null);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.height = textSize * 10;
        button.setLayoutParams(lp);

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
                case SPLIT:
                    bOption.setId(Constants.ID_BUTTON_SPLIT);
                    bOption.setText(R.string.opyion_button_split);
                    break;
            }
            bOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(optionButtons);
                }
            });
            setAnimation(bOption, position);
            lastPosition = position;
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.setStartOffset(position * 100);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
