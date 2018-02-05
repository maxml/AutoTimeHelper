package com.maxml.timer.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.maxml.timer.R;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.ShowProgressListener;
import com.maxml.timer.entity.StatisticControl;
import com.maxml.timer.entity.Table;
import com.maxml.timer.ui.adapter.TagsAdapter;
import com.maxml.timer.util.NetworkUtil;
import com.maxml.timer.util.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class SelectTagsFragment extends Fragment implements View.OnClickListener, TagsAdapter.OnTagsUpdateListener {
    private AutoCompleteTextView acTags;
    private BootstrapButton bAdd;

    private DbController controller;
    private EventBus eventBus;
    private TagsAdapter adapter;
    private List<String> allTags;
    private List<Action> listAction = new ArrayList<>();

    private ShowProgressListener progressListener;
    private StatisticControl statisticControl;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowProgressListener) {
            progressListener = (ShowProgressListener) context;
        }
        if (context instanceof StatisticControl) {
            statisticControl = (StatisticControl) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_tags, container, false);

        initView(rootView);
        setListeners();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
        controller.registerEventBus(eventBus);

        loadData();
    }

    @Override
    public void onStop() {
        eventBus.unregister(this);
        controller.unregisterEventBus(eventBus);

        if (statisticControl != null) {
            statisticControl.hideStatisticLayout();
        }

        listAction.clear();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_add:
                String description = acTags.getText().toString();
                if (!TextUtils.isEmpty(description)) {
                    if (addDescription(description)) {
                        loadData();
                        acTags.setText("");
                    } else {
                        Toast.makeText(getContext(), R.string.toast_wrong_description, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void tagsUpdate() {
        loadData();
    }

    @Subscribe
    public void onReceiveTags(List<String> tags) {
        this.allTags = tags;
        changeOldData();
        updateUi(allTags);
        adapter.loadNewData();
        progressListener.hideProgressBar();

        initStatistic();
    }

    @Subscribe
    public void onReceiveTable(Table table) {
        listAction.addAll(table.getWorkList());
        listAction.addAll(table.getCallList());
        listAction.addAll(table.getRestList());
        listAction.addAll(table.getWalkList());

        initStatistic();
        progressListener.hideProgressBar();
    }

    private void initStatistic() {
        if (statisticControl != null) {
            String time = getStatisticTime();
            statisticControl.setEventTime("Time actions: " + time);
            statisticControl.showStatisticLayout();
        }
    }

    private void changeOldData() {
        Set<String> set = SharedPreferencesUtils.getTags(getContext());
        for (String tag :
                new ArrayList<>(set)) {
            if (!allTags.contains(tag)) {
                set.remove(tag);
            }
        }
        SharedPreferencesUtils.saveTags(getContext(), set);
    }

    private String getStatisticTime() {
        long timeInMillis = 0;
        for (Action entity : listAction) {
            Date startDate = entity.getStartDate();
            Date endDate = entity.getEndDate();
            long different = endDate.getTime() - startDate.getTime();
            timeInMillis += different;
        }
        NumberFormat f = new DecimalFormat("00");
        long hours = timeInMillis / 1000 / 60 / 60;
        long min = timeInMillis / 1000 / 60 % 60;
        return f.format(hours) + ":" + f.format(min);
    }

    private void initView(View rootView) {
        acTags = rootView.findViewById(R.id.ac_tags);
        bAdd = rootView.findViewById(R.id.b_add);
        RecyclerView rvListTags = rootView.findViewById(R.id.rv_list_tags);
        rvListTags.setHasFixedSize(true);
        rvListTags.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TagsAdapter(getContext(), getFragmentManager(), this);
        rvListTags.setAdapter(adapter);
    }

    private void updateUi(List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, list);
        acTags.setAdapter(adapter);
    }

    private void setListeners() {
        bAdd.setOnClickListener(this);
    }

    private void registerEventBus() {
        eventBus = new EventBus();
        controller = new DbController(getContext(), eventBus);
    }

    private boolean addDescription(String description) {
        if (allTags.contains(description)) {
            adapter.insertTag(description);
            return true;
        }
        return false;
    }

    private void loadData() {
        listAction.clear();
        List<String> list = new ArrayList<>(SharedPreferencesUtils.getTags(getContext()));
        for (String s :
                list) {
            controller.getTableByTag(s);
        }

        controller.getAllTags();
        showProgress();
    }

    private void showProgress() {
        if (NetworkUtil.isNetworkAvailable(getContext())) {
            progressListener.showProgressBar();
        }
    }
}