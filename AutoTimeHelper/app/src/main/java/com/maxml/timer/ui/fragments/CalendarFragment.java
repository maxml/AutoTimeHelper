package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.maxml.timer.R;
import com.maxml.timer.controllers.TableControllerService;
import com.maxml.timer.entity.Table;
import com.maxml.timer.entity.eventBus.DbMessage;
import com.maxml.timer.entity.eventBus.UiMessage;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.FragmentUtils;
import com.maxml.timer.util.SliceType;
import com.maxml.timer.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;

public class CalendarFragment extends Fragment implements View.OnClickListener {

    private CalendarView calendarView;
    private TextView callsCount;
    private TextView restCount;
    private TextView workCount;
    private TextView walkCount;
    private TableControllerService controller;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initUI();
        initListener();
        controller = new TableControllerService();
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }


    private void initUI() {
        calendarView = (CalendarView) getActivity().findViewById(R.id.calendarView);
        callsCount = (TextView) getActivity().findViewById(R.id.tvCalls);
        restCount = (TextView) getActivity().findViewById(R.id.tvvtRestCount);
        workCount = (TextView) getActivity().findViewById(R.id.tvWorkCount);
        walkCount = (TextView) getActivity().findViewById(R.id.tvWalkCount);
    }

    private void initListener() {
        callsCount.setOnClickListener(this);
        walkCount.setOnClickListener(this);
        workCount.setOnClickListener(this);
        restCount.setOnClickListener(this);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                EventBus.getDefault().post(
                        new UiMessage(Constants.EVENT_UI_TABLE_DATE, Utils.getDate(dayOfMonth, month, year))
                );
            }
        });
    }

    @Subscribe(sticky = true)
    public void onUiEvent(UiMessage event) {
        switch (event.getMessage()) {
            case Constants.EVENT_UI_GET_TABLE:
                Table table = (Table) event.getData();
                updateUI(table);
                break;

        }
    }


    private void updateUI(Table table) {
        int calls = 0;
        int work = 0;
        int rests = 0;
        int walks = 0;
        if (table != null) {
            calls = table.getCallList().size();
            walks = table.getWalkList().size();
            rests = table.getRestList().size();
            work = table.getWorkList().size();
        }
        callsCount.setText(calls);
        workCount.setText(work);
        walkCount.setText(walks);
        restCount.setText(rests);
    }

    @Override
    public void onClick(View view) {
        SliceListViewFragment sliceFragment = new SliceListViewFragment();
        FragmentUtils.setFragment(getActivity(), sliceFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
