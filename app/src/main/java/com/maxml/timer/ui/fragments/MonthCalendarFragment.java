package com.maxml.timer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.maxml.timer.R;
import com.maxml.timer.entity.ShowFragmentListener;
import com.maxml.timer.entity.StatisticControl;
import com.maxml.timer.util.Constants;

import java.util.Calendar;

public class MonthCalendarFragment extends Fragment {
    private CalendarView cvCalendar;
    private Toolbar toolbar;

    private ShowFragmentListener fragmentListener;
    private StatisticControl statisticControl;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowFragmentListener) {
            fragmentListener = (ShowFragmentListener) context;
        }
        if (context instanceof StatisticControl) {
            statisticControl = (StatisticControl) context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month_calendar, container, false);
        initUI(view);
        initListener();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (statisticControl != null) {
//            statisticControl.showStatisticLayout();
        }
    }

    @Override
    public void onStop() {
        if (statisticControl != null){
//            statisticControl.hideStatisticLayout();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toolbar.setTitle(R.string.app_name);
    }

    private void initUI(View view) {
        cvCalendar = view.findViewById(R.id.cv_calendar);
        cvCalendar.setDate(System.currentTimeMillis());

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.calendar);
    }

    private void initListener() {
        cvCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                showDayFragment(calendar);
            }
        });
    }

    private void showDayFragment(Calendar calendar) {
        DayCalendarFragment dayCalendarFragment = new DayCalendarFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.EXTRA_TIME_ACTION, calendar.getTimeInMillis());
        dayCalendarFragment.setArguments(args);
        fragmentListener.showFragment(dayCalendarFragment);
    }
}
