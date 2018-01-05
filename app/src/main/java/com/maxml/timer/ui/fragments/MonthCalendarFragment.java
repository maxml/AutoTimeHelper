package com.maxml.timer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.maxml.timer.R;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.StatisticControl;
import com.maxml.timer.entity.Table;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MonthCalendarFragment extends Fragment {
    private CalendarView cvCalendar;

    private EventBus eventBus;
    private DbController controller;

    private StatisticControl statisticControl;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month_calendar, container, false);

        initUI(view);
        initListener();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
        controller.registerEventBus(eventBus);
        if (statisticControl != null) {
            String time = getStatisticTime();
            statisticControl.setEventTime(time);
            statisticControl.showStatisticLayout();
        }
    }

    @Override
    public void onStop() {
        eventBus.unregister(this);
        controller.unregisterEventBus(eventBus);
        if (statisticControl != null) {
            statisticControl.hideStatisticLayout();
        }
        super.onStop();
    }

    @Subscribe
    public void receiveTableFromDb(Table table) {

    }

    // todo
    public String getStatisticTime() {
        return "00:00";
    }

    private void registerEventBus() {
        eventBus = new EventBus();
        controller = new DbController(getContext(), eventBus);
    }

    private void initUI(View view) {
        cvCalendar = (CalendarView) view.findViewById(R.id.cv_calendar);
        cvCalendar.setDate(System.currentTimeMillis());
    }

    private void initListener() {
        cvCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {

            }
        });
    }
}
