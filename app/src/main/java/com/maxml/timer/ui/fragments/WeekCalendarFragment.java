package com.maxml.timer.ui.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.maxml.timer.R;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.dialog.OptionActionDialog;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.ActionWeek;
import com.maxml.timer.entity.Events;
import com.maxml.timer.entity.ShowFragmentListener;
import com.maxml.timer.entity.ShowProgressListener;
import com.maxml.timer.entity.StatisticControl;
import com.maxml.timer.entity.Table;
import com.maxml.timer.util.ActionConverter;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeekCalendarFragment extends Fragment implements WeekView.EventClickListener,
        MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, OptionActionDialog.OnDialogItemClickListener {

    private List<WeekViewEvent> list = new ArrayList<>();
    private WeekView weekView;

    private EventBus eventBus;
    private DbController controller;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_week_calendar, container, false);

        initView(rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
        controller.registerEventBus(eventBus);

        loadActions();
    }

    @Override
    public void onStop() {
        controller.unregisterEventBus(eventBus);
        eventBus.unregister(this);

        if (statisticControl != null) {
            statisticControl.hideStatisticLayout();
        }

        progressListener.hideProgressBar();
        super.onStop();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        DialogFragment dialog = OptionActionDialog.getInstance(this, ((ActionWeek) event).getActionId());
        dialog.show(getFragmentManager(), "dialog option");
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int month = calendar.get(Calendar.MONTH) + 1;
        if (newMonth == month) {
            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public void onDialogItemClick(int position, String idEvent) {
        if (position == Constants.ID_BUTTON_EDIT) {
            Bundle args = new Bundle();
            args.putString(Constants.EXTRA_ID_ACTION, idEvent);
            DetailsActionFragment fragment = new DetailsActionFragment();
            fragment.setArguments(args);

            fragmentListener.showFragment(fragment);
        } else if (position == Constants.ID_BUTTON_DELETE) {
            controller.removeActionInDb(idEvent);

            progressListener.showProgressBar();
        }
    }

    @Subscribe
    public void receiveTableFromDb(List<Table> listTable) {
        List<Action> actions = new ArrayList<>();

        for (Table table :
                listTable) {
            actions.addAll(table.getWorkList());
            actions.addAll(table.getCallList());
            actions.addAll(table.getRestList());
            actions.addAll(table.getWalkList());
        }
        list = ActionConverter.actionsToWeekViewEvents(actions, getResources());

        updateUI();
        initStatistic(actions);

        progressListener.hideProgressBar();
    }

    @Subscribe
    public void onDatabaseEvent(Events.DbResult event) {
        switch (event.getResultStatus()) {
            case Constants.EVENT_DB_RESULT_OK:
                getActionsFromDb();
                break;
            case Constants.EVENT_DB_RESULT_ERROR:
                progressListener.hideProgressBar();

                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void loadActions() {
        progressListener.showProgressBar();
        getActionsFromDb();
    }

    private void getActionsFromDb() {
        Date startDate = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * Constants.WEEK_COUNT_DAY);
        Date endDate = new Date(System.currentTimeMillis());
        controller.getTableFromDb(startDate, endDate);
    }

    private void registerEventBus() {
        eventBus = new EventBus();
        controller = new DbController(getContext(), eventBus);
    }

    private void initView(View rootView) {
        weekView = (WeekView) rootView.findViewById(R.id.weekView);
        weekView.setMonthChangeListener(this);
        weekView.setOnEventClickListener(this);
        weekView.setEventLongPressListener(this);
        weekView.setNumberOfVisibleDays(1);
    }

    private void updateUI() {
        weekView.notifyDatasetChanged();
    }

    private void initStatistic(List<Action> list) {
        if (statisticControl != null) {
            String time = getStatisticTime(list);
            statisticControl.setEventTime(time);
            statisticControl.showStatisticLayout();
        }
    }

    private String getStatisticTime(List<Action> list) {
        long timeInMillis = 0;
        for (Action action : list) {
            if (action.getType().equalsIgnoreCase(Constants.EVENT_WORK_ACTION)) {
                Date startDate = action.getStartDate();
                Date endDate = action.getEndDate();
                long different = endDate.getTime() - startDate.getTime();
                timeInMillis += different;
            }
        }
        NumberFormat f = new DecimalFormat("00");
        long hours = timeInMillis / 1000 / 60 / 60;
        long min = timeInMillis / 1000 / 60 % 60;
        return f.format(hours) + ":" + f.format(min);
    }
}
