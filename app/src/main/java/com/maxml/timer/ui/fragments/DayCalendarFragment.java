package com.maxml.timer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.maxml.timer.R;
import com.maxml.timer.entity.Events;
import com.maxml.timer.ui.adapter.CalendarDayAdapter;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.ShowFragmentListener;
import com.maxml.timer.entity.ShowProgressListener;
import com.maxml.timer.entity.StatisticControl;
import com.maxml.timer.entity.Table;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.OptionButtons;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DayCalendarFragment extends Fragment {

    private RecyclerView recyclerView;

    private List<OptionButtons> options = new ArrayList<>();
    private List<Action> list = new ArrayList<>();
    private CalendarDayAdapter adapter;
    private DbController controller;
    private EventBus eventBus;

    private StatisticControl statisticControl;
    private ShowFragmentListener fragmentListener;
    private ShowProgressListener progressListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StatisticControl) {
            statisticControl = (StatisticControl) context;
        }
        if (context instanceof ShowFragmentListener) {
            fragmentListener = (ShowFragmentListener) context;
        }
        if (context instanceof ShowProgressListener) {
            progressListener = (ShowProgressListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
        initOptionButtons();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        controller.getTableFromDb(new Date(System.currentTimeMillis()));
        progressListener.showProgressBar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_calendar, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        initRecyclerView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
        controller.registerEventBus(eventBus);
    }

    @Override
    public void onStop() {
        eventBus.unregister(this);
        controller.unregisterEventBus(eventBus);
        if (statisticControl != null) {
            statisticControl.hideStatisticLayout();
        }

        list.clear();
        super.onStop();
    }

    @Subscribe
    public void receiveTableFromDb(Table table) {
        list.addAll(table.getWorkList());
        list.addAll(table.getCallList());
        list.addAll(table.getRestList());
        list.addAll(table.getWalkList());

        updateUI();
        initStatistic();
        progressListener.hideProgressBar();
    }

    @Subscribe
    public void onDatabaseEvent(Events.DbResult event) {
        switch (event.getResultStatus()) {
            case Constants.EVENT_DB_RESULT_OK:
                list.clear();
                controller.getTableFromDb(new Date(System.currentTimeMillis()));
                break;
            case Constants.EVENT_DB_RESULT_ERROR:
                progressListener.hideProgressBar();

                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void updateUI() {
        adapter.swapData(list);

        initStatistic();
    }

    private void registerEventBus() {
        eventBus = new EventBus();
        controller = new DbController(getContext(), eventBus);
    }

    private void initStatistic() {
        if (statisticControl != null) {
            String time = getStatisticTime();
            statisticControl.setEventTime(time);
            statisticControl.showStatisticLayout();
        }
    }

    private String getStatisticTime() {
        long timeInMillis = 0;
        for (Action entity : list) {
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

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CalendarDayAdapter(getContext(), new ArrayList<Action>(), options, new CalendarDayAdapter.OnClickOption() {
            @Override
            public void onClick(OptionButtons optionType, Action item) {
                if (optionType == OptionButtons.EDIT) {
                    Bundle args = new Bundle();
                    args.putString(Constants.EXTRA_ID_ACTION, item.getId());
                    DetailsActionFragment fragment = new DetailsActionFragment();
                    fragment.setArguments(args);

                    fragmentListener.showFragment(fragment);
                } else if (optionType == OptionButtons.DELETE) {
                    controller.removeActionInDb(item.getId());

                    progressListener.showProgressBar();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initOptionButtons() {
        options.add(OptionButtons.DELETE);
        options.add(OptionButtons.SPLIT);
        options.add(OptionButtons.EDIT);
    }
}
