package com.maxml.timer.util;

import android.content.Context;

import com.alamkanak.weekview.WeekViewEvent;
import com.maxml.timer.database.DatabaseHelper;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.ActionWeek;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActionUtils {

    public static List<WeekViewEvent> actionsToWeekViewEvents(List<Action> list, Context context) {
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
}
