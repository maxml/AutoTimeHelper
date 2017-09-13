package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.maxml.timer.R;
import com.maxml.timer.controllers.TableController;
import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Table;
import com.maxml.timer.util.FragmentUtils;
import com.maxml.timer.util.SliceType;
import com.maxml.timer.util.Utils;

import java.util.ArrayList;
import java.util.Date;

public class CalendarFragment extends Fragment implements View.OnClickListener{

    private CalendarView calendarView;
    private TextView callsCount;
    private TextView restCount;
    private TextView workCount;
    private TextView walkCount;
    private TableController controller;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initUI();
        initListener();
        controller = new TableController();
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
                updateUI(Utils.getDate(dayOfMonth, month, year));
            }
        });
    }

    private void updateUI(Date date) {
        Table table = controller.getTable(date);
        int calls = 0;
        int work = 0;
        int rests = 0;
        int walks = 0;
        if (table != null && table.getList().size() > 0) {
            for (Slice slice : table.getList()) {
                SliceType type = slice.getType();
                if (type == SliceType.CALL){
                    calls++;
                }
                if (type == SliceType.WORK){
                    work++;
                }
                if (type == SliceType.REST){
                    rests++;
                }
                if (type == SliceType.WALK){
                    walks++;
                }
            }
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
}
