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
import com.maxml.timer.controllers.Controller;
import com.maxml.timer.entity.Table;
import com.maxml.timer.entity.eventBus.EventMessage;
import com.maxml.timer.entity.eventBus.DbMessage;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.EventBusType;
import com.maxml.timer.util.FragmentUtils;
import com.maxml.timer.util.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.util.Date;

public class CalendarFragment extends Fragment implements View.OnClickListener {

    private Controller controller;
    private CalendarView calendarView;
    private TextView callsCount;
    private TextView restCount;
    private TextView workCount;
    private TextView walkCount;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        controller = new Controller(getContext());
        initUI(view);
        initListener();
        // get current date
        Date selectedDate = new Date(calendarView.getDate());
        DbMessage getTableMsg = new DbMessage(Constants.EVENT_TABLE_DATE_REQUEST, EventBusType.CALENDAR_FRAGMENT, selectedDate);
        controller.getEventBus(EventBusType.DB).post(getTableMsg);
        return view;
    }


    private void initUI(View view) {
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        callsCount = (TextView) view.findViewById(R.id.tvCallsCount);
        restCount = (TextView) view.findViewById(R.id.tvvtRestCount);
        workCount = (TextView) view.findViewById(R.id.tvWorkCount);
        walkCount = (TextView) view.findViewById(R.id.tvWalkCount);
    }

    private void initListener() {
        callsCount.setOnClickListener(this);
        walkCount.setOnClickListener(this);
        workCount.setOnClickListener(this);
        restCount.setOnClickListener(this);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                DbMessage getTableMsg = new DbMessage(Constants.EVENT_TABLE_DATE_REQUEST, EventBusType.CALENDAR_FRAGMENT,
                        Utils.getDate(dayOfMonth, month, year));
                controller.getEventBus(EventBusType.DB).post(getTableMsg);
            }
        });
    }

    @Subscribe()
    public void getDbMessage(EventMessage message) {
        switch (message.getMessage()) {
            case Constants.EVENT_TABLE_DATE_RESULT:
                Table table = (Table) message.getData();
                updateUI(table);
                break;

            case Constants.EVENT_DB_RESULT_ERROR:
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
        callsCount.setText(calls + "");
        workCount.setText(work + "");
        walkCount.setText(walks + "");
        restCount.setText(rests + "");
    }

    @Override
    public void onClick(View view) {
        ActionListViewFragment sliceFragment = new ActionListViewFragment();
        FragmentUtils.setFragment(getActivity(), sliceFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        controller.getEventBus(EventBusType.CALENDAR_FRAGMENT).register(this);
    }

    @Override
    public void onStop() {
        controller.getEventBus(EventBusType.CALENDAR_FRAGMENT).unregister(this);
        super.onStop();
    }
}
