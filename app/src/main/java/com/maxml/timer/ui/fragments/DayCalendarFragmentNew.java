package com.maxml.timer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maxml.timer.R;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.Events;
import com.maxml.timer.entity.ShowFragmentListener;
import com.maxml.timer.entity.ShowProgressListener;
import com.maxml.timer.entity.StatisticControl;
import com.maxml.timer.entity.Table;
import com.maxml.timer.listeners.OnSwipeTouchListener;
import com.maxml.timer.ui.adapter.CalendarDayAdapterNew;
import com.maxml.timer.ui.adapter.CalendarDayTimeAdapter;
import com.maxml.timer.util.ActionUtils;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.OptionButtons;
import com.maxml.timer.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DayCalendarFragmentNew extends Fragment implements CalendarDayAdapterNew.OnClickListener {

    private RecyclerView recyclerViewTime;
    private RecyclerView recyclerViewActions;
    private TextView tvDay;

    private boolean isJoined;

    private Date currentDate;
    private List<OptionButtons> options = new ArrayList<>();
    private List<Action> list = new ArrayList<>();
    private List<Date> dates = new ArrayList<>();
    private CalendarDayAdapterNew actionAdapter;
    private CalendarDayTimeAdapter timeAdapter;
    private DbController controller;
    private EventBus eventBus;
    private Action lastAction;
    private Menu menu;

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
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_calendar_new, container, false);
        recyclerViewActions = view.findViewById(R.id.recyclerView);
        recyclerViewTime = view.findViewById(R.id.recyclerViewTime);
        tvDay = view.findViewById(R.id.day);
        initRecyclerView();
        recyclerViewActions.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                gotoPreviousDate();
            }

            public void onSwipeLeft() {
                gotoNextDate();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
        controller.registerEventBus(eventBus);
        if (getArguments() != null) {
            currentDate = new Date(getArguments().getLong(Constants.EXTRA_TIME_ACTION));
        } else {
            currentDate = new Date(System.currentTimeMillis());
        }
        loadActions(currentDate.getTime());
        updateDay();
    }

    private void updateDay() {
        String date = Utils.parseToDate(currentDate.getTime());
        tvDay.setText(date);
    }

    @Override
    public void onStop() {
        eventBus.unregister(this);
        controller.unregisterEventBus(eventBus);
        if (statisticControl != null) {
            statisticControl.hideStatisticLayout();
        }
        progressListener.hideProgressBar();
        list.clear();
        dates.clear();
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.calendar_day_menu, menu);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.i_v_cancel:
                isJoined = false;
                changeMenuVisible();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickOption(OptionButtons optionType, Action item) {
        switch (optionType) {
            case DELETE:
                controller.removeActionInDb(String.valueOf(item.getId()), item.getDescription());
                progressListener.showProgressBar();
                break;
            case EDIT:
                Bundle args = new Bundle();
                args.putString(Constants.EXTRA_ID_ACTION, item.getId());
                DetailsActionFragment fragment = new DetailsActionFragment();
                fragment.setArguments(args);
                fragmentListener.showFragment(fragment);
                break;
            case JOIN:
                isJoined = true;
                lastAction = item;
                changeMenuVisible();
                break;
        }
        actionAdapter.resetList();
    }

    @Override
    public void onClick(Action action) {
        if (isJoined) {
            actionAdapter.resetList();
            if (action != lastAction) {
                Action newAction = ActionUtils.joinActions(lastAction, action);
                controller.updateActionInDb(newAction,lastAction.getDescription());
                controller.removeActionInDb(action.getId(), action.getDescription());
                progressListener.showProgressBar();
            } else {
                Toast.makeText(getContext(), R.string.message_two_identical_action, Toast.LENGTH_SHORT).show();
            }
            isJoined = false;
            changeMenuVisible();
        }
    }

    @Subscribe
    public void receiveTableFromDb(Table table) {
        list.clear();
        list.addAll(table.getWorkList());
        list.addAll(table.getCallList());
        list.addAll(table.getRestList());
        list.addAll(table.getWalkList());
        Collections.sort(list);

        dates.clear();
        for (Action action : list) {
            dates.add(action.getStartDate());
        }

        updateUI();
        initStatistic();
        progressListener.hideProgressBar();
    }

    @Subscribe
    public void onDatabaseEvent(Events.DbResult event) {
        switch (event.getResultStatus()) {
            case Constants.EVENT_DB_RESULT_OK:
                controller.getTableFromDb(new Date(System.currentTimeMillis()));
                break;
            case Constants.EVENT_DB_RESULT_ERROR:
                progressListener.hideProgressBar();
                break;
        }
    }

    private void loadActions(long time) {
        if (time == 0) {
            controller.getTableFromDb(new Date(System.currentTimeMillis()));
        } else {
            controller.getTableFromDb(new Date(time));
        }
        progressListener.showProgressBar();
    }

    private void updateUI() {
        actionAdapter.swapData(list);
        timeAdapter.swapData(dates);
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
            if (entity.getType().equalsIgnoreCase(Constants.EVENT_WORK_ACTION)) {
                Date startDate = entity.getStartDate();
                Date endDate = entity.getEndDate();
                long different = endDate.getTime() - startDate.getTime();
                timeInMillis += different;
            }
        }
        NumberFormat f = new DecimalFormat("00");
        long hours = timeInMillis / 1000 / 60 / 60;
        long min = timeInMillis / 1000 / 60 % 60;
        return f.format(hours) + ":" + f.format(min);
    }

    private void initRecyclerView() {
        recyclerViewTime.setLayoutManager(new LinearLayoutManager(getContext()));
        timeAdapter = new CalendarDayTimeAdapter(getContext(), dates);
        recyclerViewTime.setAdapter(timeAdapter);

        recyclerViewActions.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        actionAdapter = new CalendarDayAdapterNew(getContext(), list, options, this);
        recyclerViewActions.setAdapter(actionAdapter);

        initRecyclerViewListeners();
    }

    private void initRecyclerViewListeners() {
        // synchronize recycler view scrolling
        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView == recyclerViewActions) {
                    recyclerViewTime.removeOnScrollListener(this);
                    recyclerViewTime.scrollBy(0, dy);
                    recyclerViewTime.addOnScrollListener(this);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        };

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    actionAdapter.closeExpandableOption();
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPosition = viewHolder.getAdapterPosition();
                final int toPosition = target.getAdapterPosition();
                // and notify the actionAdapter that its dataset has changed
                actionAdapter.onItemMove(fromPosition, toPosition);
                timeAdapter.onItemMove(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }

            //defines the enabled move directions in each state (idle, swiping, dragging).
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(itemTouch);
        touchHelper.attachToRecyclerView(recyclerViewActions);
        recyclerViewTime.setNestedScrollingEnabled(false);
        recyclerViewActions.addOnScrollListener(scrollListener);
    }

    private void gotoPreviousDate() {
        long time = currentDate.getTime() - 86400000;
        currentDate = new Date(time);
        loadActions(time);
        updateDay();
    }

    private void gotoNextDate() {
        long time = currentDate.getTime() + 86400000;
        currentDate = new Date(time);
        loadActions(time);
        updateDay();
    }

    private void initOptionButtons() {
        options.add(OptionButtons.DELETE);
        options.add(OptionButtons.EDIT);
        options.add(OptionButtons.JOIN);
    }

    private void changeMenuVisible() {
        menu.setGroupVisible(R.id.g_cancel_option, isJoined);
    }
}
