package com.maxml.timer.ui.fragments;

import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.maxml.timer.R;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.ActionWeek;
import com.maxml.timer.entity.Events;
import com.maxml.timer.entity.ShowProgressListener;
import com.maxml.timer.entity.StatisticControl;
import com.maxml.timer.entity.Table;
import com.maxml.timer.ui.dialog.ChangeActionDialog;
import com.maxml.timer.ui.dialog.CreateActionDialog;
import com.maxml.timer.ui.dialog.OptionDialog;
import com.maxml.timer.util.ActionUtils;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.NetworkUtil;
import com.u1aryz.android.colorpicker.ColorPreferenceFragmentCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeekCalendarFragment extends Fragment implements WeekView.EventClickListener,
        MonthLoader.MonthChangeListener, WeekView.EventLongPressListener,
        OptionDialog.OnDialogItemClickListener, CreateActionDialog.OnActionCreatedListener,
        ChangeActionDialog.OnActionChangedListener {

    private WeekView weekView;
    private CalendarView cvCalendar;
    private Toolbar toolbar;
    private Menu menu;

    private List<WeekViewEvent> list = new ArrayList<>();
    private List<Action> actions = new ArrayList<>();
    private int[] calendarViews = {1, 3, 5};
    private int currentCalendarView = 1;
    private boolean isJoined;

    private EventBus eventBus;
    private DbController controller;
    private ShowProgressListener progressListener;
    private StatisticControl statisticControl;

    private WeekViewEvent lastEvent;

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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_week_calendar, container, false);
        initView(rootView);
        setListeners();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
        controller.registerEventBus(eventBus);

        loadActions();
        if (statisticControl != null) {
            statisticControl.showStatisticLayout();
        }
    }

    @Override
    public void onStop() {
        controller.unregisterEventBus(eventBus);
        eventBus.unregister(this);

        cvCalendar.setVisibility(View.GONE);
        progressListener.hideProgressBar();

        if (statisticControl != null) {
            statisticControl.hideStatisticLayout();
        }
        super.onStop();
    }

    @Override
    public void onActionCreated(Action action) {
        controller.createAction(action);
        List<Action> list = new ArrayList<>();
        list.add(action);
        this.list.addAll(ActionUtils.convertActionsToWeekViewEvents(list, getContext()));
        weekView.notifyDataSetChanged();

        showProgress();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        toolbar.setTitle(R.string.app_name);
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        if (isJoined) {
            if (event.getColor() == R.color.color1) {
                setStandartColorForActions();
                joinTwoAction(event);
            }
        } else {
            lastEvent = event;
            DialogFragment dialog = OptionDialog.getInstance(this, R.array.options_action);
            dialog.show(getFragmentManager(), "dialog option");
        }
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
    public void onDialogItemClick(int position) {
        switch (position) {
            case Constants.ID_BUTTON_ACTION_EDIT:
                ChangeActionDialog changeActionDialog = ChangeActionDialog.getInstance(this);
                Bundle args = new Bundle();
                args.putString(Constants.EXTRA_ID_ACTION, ((ActionWeek) lastEvent).getActionId());
                changeActionDialog.setArguments(args);
                changeActionDialog.show(getFragmentManager(), "DialogEditAction");
                break;
            case Constants.ID_BUTTON_ACTION_DELETE:
                Action action = ActionUtils.findActionById(((ActionWeek) lastEvent).getActionId(), actions);
                controller.removeActionInDb(action.getId(), action.getDescription());
                list.remove(lastEvent);
                weekView.notifyDataSetChanged();

                showProgress();
                break;
            case Constants.ID_BUTTON_ACTION_JOIN:
                isJoined = true;

                list = ActionUtils.findNeedActionsForJoin(list, lastEvent);
                lastEvent.setColor(R.color.select_action);
                weekView.notifyDataSetChanged();

                changeMenuVisible();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.calendar_week_menu, menu);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.i_v_change_view:
                weekView.setNumberOfVisibleDays(getNextCurrentCalendarView());
                break;
            case R.id.i_v_current_day:
                weekView.goToToday();
                break;
            case R.id.i_v_month_calendar:
                if (cvCalendar.getVisibility() == View.GONE) {
                    cvCalendar.setVisibility(View.VISIBLE);
                    cvCalendar.setDate(weekView.getFirstVisibleDay().getTimeInMillis());
                } else {
                    cvCalendar.setVisibility(View.GONE);
                }
                break;
            case R.id.i_v_refresh:
                loadActions();
                break;
            case R.id.i_v_cancel:
                isJoined = false;
                changeMenuVisible();
                setStandartColorForActions();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void actionChanged(Action action) {
        loadActions();
    }

    @Subscribe
    public void receiveTableFromDb(List<Table> listTable) {
        actions = new ArrayList<>();
        List<Action> workActions = new ArrayList<>();

        for (Table table : listTable) {
            actions.addAll(table.getWorkList());
            actions.addAll(table.getCallList());
            actions.addAll(table.getRestList());
            actions.addAll(table.getWalkList());
            workActions.addAll(table.getWorkList());
        }
        list = ActionUtils.convertActionsToWeekViewEvents(actions, getContext());

        updateUI();
        String workTime = getStatisticTime(workActions);
        statisticControl.setEventTime(workTime);

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
                break;
        }
    }

    private String getStatisticTime(List<Action> list) {
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


    private void joinTwoAction(WeekViewEvent event) {
        Action firesAction = ActionUtils.findActionById(((ActionWeek) lastEvent).getActionId(), actions);
        Action secondAction = ActionUtils.findActionById(((ActionWeek) event).getActionId(), actions);

        Action action = ActionUtils.joinActions(firesAction, secondAction);

        controller.updateActionInDb(action, firesAction.getDescription());
        controller.removeActionInDb(secondAction.getId(), secondAction.getDescription());
        showProgress();

        isJoined = false;

        changeMenuVisible();
    }

    private void loadActions() {
        showProgress();
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
        weekView = rootView.findViewById(R.id.weekView);
        cvCalendar = getActivity().findViewById(R.id.cv_calendar);
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.name_calendar_fragment);
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateActionDialog dialog = CreateActionDialog.getInstance(WeekCalendarFragment.this);
                dialog.show(getFragmentManager(), "Create Action Dialog");
            }
        });

        weekView.setMonthChangeListener(this);
        weekView.setOnEventClickListener(this);
        weekView.setEventLongPressListener(this);
        weekView.setShowNowLine(true);
        weekView.setNumberOfVisibleDays(3);
    }

    private void setListeners() {
        cvCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                weekView.goToDate(calendar);
            }
        });
    }

    private void updateUI() {
        weekView.notifyDataSetChanged();

    }

    private int getNextCurrentCalendarView() {
        currentCalendarView++;
        if (currentCalendarView >= calendarViews.length) {
            currentCalendarView = 0;
        }
        return calendarViews[currentCalendarView];
    }

    private void showProgress() {
        if (NetworkUtil.isNetworkAvailable(getContext())) {
            progressListener.showProgressBar();
        }
    }

    private void changeMenuVisible() {
        menu.setGroupVisible(R.id.g_main_option, !isJoined);
        menu.setGroupVisible(R.id.g_cancel_option, isJoined);
    }

    public void setStandartColorForActions() {
        list = ActionUtils.convertActionsToWeekViewEvents(actions, getContext());
        weekView.notifyDataSetChanged();
    }
}
