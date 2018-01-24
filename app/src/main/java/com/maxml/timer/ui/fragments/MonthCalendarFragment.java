package com.maxml.timer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.maxml.timer.R;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.ShowFragmentListener;
import com.maxml.timer.entity.ShowProgressListener;
import com.maxml.timer.entity.StatisticControl;
import com.maxml.timer.entity.Table;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthCalendarFragment extends Fragment {
    private CalendarView cvCalendar;

    private ShowFragmentListener fragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowFragmentListener) {
            fragmentListener = (ShowFragmentListener) context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month_calendar, container, false);

        initUI(view);
        initListener();

        return view;
    }

    private void initUI(View view) {
        cvCalendar = view.findViewById(R.id.cv_calendar);
        cvCalendar.setDate(System.currentTimeMillis());
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
        DayCalendarFragmentNew dayCalendarFragment = new DayCalendarFragmentNew();
        Bundle args = new Bundle();
        args.putLong(Constants.EXTRA_TIME_ACTION, calendar.getTimeInMillis());
        dayCalendarFragment.setArguments(args);

        fragmentListener.showFragment(dayCalendarFragment);
    }
}
