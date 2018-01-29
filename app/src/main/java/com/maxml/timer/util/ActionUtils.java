package com.maxml.timer.util;

import android.content.Context;

import com.alamkanak.weekview.WeekViewEvent;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.ActionWeek;

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

            String description = (action.getDescription() == null) ? "" : action.getDescription();

            ActionWeek actionWeek = new ActionWeek(action.getId(), action.getType() + "\n" + description,
                    action.getType(), startData, endData);

            actionWeek.setColor(SharedPreferencesUtils.getColor(context, actionWeek.getType()));

            result.add(actionWeek);
        }
        return result;
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
