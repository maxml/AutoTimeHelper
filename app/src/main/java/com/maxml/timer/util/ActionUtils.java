package com.maxml.timer.util;

import android.content.Context;

import com.alamkanak.weekview.WeekViewEvent;
import com.maxml.timer.R;
import com.maxml.timer.entity.Action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActionUtils {

    public static List<WeekViewEvent> convertActionsToWeekViewEvents(List<Action> list, Context context) {
        List<WeekViewEvent> result = new ArrayList<>();

        for (Action action :
                list) {
            Calendar startData = Calendar.getInstance();
            Calendar endData = Calendar.getInstance();

            startData.setTimeInMillis(action.getStartDate().getTime());
            endData.setTimeInMillis(action.getEndDate().getTime());

            if (endData.getTimeInMillis() - startData.getTimeInMillis() < 1000 * 60 * 30) {
                endData.setTimeInMillis(startData.getTimeInMillis() + (1000 * 60 * 30));
            }

            WeekViewEvent actionWeek = new WeekViewEvent(action.getId(), action.getDescription(),
                    action.getType(), startData, endData);

            actionWeek.setColor(SharedPreferencesUtils.getColor(context, actionWeek.getType()));

            result.add(actionWeek);
        }
        return result;
    }

    public static List<WeekViewEvent> findNeedActionsForJoin(Context context, List<WeekViewEvent> list, WeekViewEvent changeAction) {
        //if the actions intersect with the selected action, then change their color.
        long startTimeSelectAction = changeAction.getStartTime().getTimeInMillis();
        long endTimeSelectAction = changeAction.getEndTime().getTimeInMillis();
        for (int i = 0; i < list.size(); i++) {
            long startTimeAction = list.get(i).getStartTime().getTimeInMillis();
            long endTimeAction = list.get(i).getEndTime().getTimeInMillis();
            if ((startTimeSelectAction != startTimeAction) &&
                    ((startTimeSelectAction <= startTimeAction && endTimeSelectAction >= startTimeAction) ||
                            (startTimeSelectAction <= endTimeAction && endTimeSelectAction >= endTimeAction)) ||
                    ((startTimeAction <= startTimeSelectAction && endTimeAction >= startTimeSelectAction) ||
                            (startTimeAction <= endTimeSelectAction && endTimeAction >= endTimeSelectAction))) {
                list.get(i).setColor(SharedPreferencesUtils.getColor(context, Constants.ACTION_JOINED));
            }
        }
        return list;
    }

    public static WeekViewEvent setStandartColor(Context context, WeekViewEvent action) {
        action.setColor(SharedPreferencesUtils.getColor(context, action.getType()));
        return action;
    }

    public static List<Action> unescaping(List<Action> list) {
        for (Action action :
                list) {
            action = unescaping(action);
        }
        return list;
    }

    public static Action escaping(Action action) {
        if (action.getDescription().contains("#"))
            action.setDescription(action.getDescription().replace("#", "__"));
        return action;
    }

    public static Action unescaping(Action action) {
        if (action.getDescription().contains("__"))
            action.setDescription(action.getDescription().replace("__", "#"));
        return action;
    }

    public static String escapingTag(String s) {
        if (s.contains("#"))
            s = s.replace("#", "__");
        return s;
    }

    public static String unescapingTag(String s) {
        if (s.contains("__"))
            s = s.replace("__", "#");
        return s;
    }

    public static List<String> escapingTag(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, escapingTag(list.get(i)));
        }
        return list;
    }

    public static List<String> unescapingTag(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, unescapingTag(list.get(i)));
        }
        return list;
    }

    public static Action findActionById(String id, List<Action> actions) {
        for (Action action :
                actions) {
            if (action.getId().equalsIgnoreCase(id)) {
                return action;
            }
        }
        return null;
    }

    public static Action joinActions(Action firstAction, Action secondAction) {
        if (firstAction.getStartDate().getTime() < secondAction.getStartDate().getTime()) {
            Date endDate = new Date(firstAction.getEndDate().getTime() +
                    (secondAction.getEndDate().getTime() - secondAction.getStartDate().getTime()));

            firstAction.setEndDate(endDate);
            return firstAction;
        } else {
            Date startData = new Date(firstAction.getStartDate().getTime() -
                    (secondAction.getEndDate().getTime() - secondAction.getStartDate().getTime()));
            firstAction.setStartDate(startData);

            return firstAction;
        }
    }
}
