package com.maxml.timer.ui.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxml.timer.R;
import com.maxml.timer.ui.dialog.OptionDialog;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {
    private FragmentManager fragmentManager;
    private List<String> list = new ArrayList<>();
    private Context context;

    private OnTagsUpdateListener tagsUpdateListener;

    public interface OnTagsUpdateListener {
        void tagsUpdate();
    }

    public TagsAdapter(Context context, FragmentManager fragmentManager, OnTagsUpdateListener listener) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        list = new ArrayList<>(SharedPreferencesUtils.getTags(context));
        Collections.sort(list);

        tagsUpdateListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void insertTag(String description) {
        if (!list.contains(description)) {
            list.add(description);
            SharedPreferencesUtils.saveTags(context, new HashSet<>(list));
            notifyDataSetChanged();
        }
    }

    public void loadNewData() {
        list = new ArrayList<>(SharedPreferencesUtils.getTags(context));
        notifyDataSetChanged();
    }

    private void saveNewData() {
        SharedPreferencesUtils.saveTags(context, new HashSet<>(list));
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }

        public void bind(String description, final int position) {
            tvDescription.setText(description);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    OptionDialog dialog = OptionDialog.getInstance(new OptionDialog.OnDialogItemClickListener() {
                        @Override
                        public void onDialogItemClick(int positionButton) {
                            if (positionButton == Constants.ID_BUTTON_TAG_delete) {
                                list.remove(position);
                                saveNewData();
                                tagsUpdateListener.tagsUpdate();
                            }
                        }
                    }, R.array.options_tags);
                    dialog.show(fragmentManager, "Dialog Remove");
                    return false;
                }
            });
        }
    }
}
