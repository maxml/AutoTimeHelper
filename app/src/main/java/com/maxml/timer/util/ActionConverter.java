package com.maxml.timer.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.Toast;

import com.alamkanak.weekview.WeekViewEvent;
import com.maxml.timer.R;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.ActionWeek;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static com.maxml.timer.util.Constants.EVENT_WALK_ACTION;

public class ActionConverter {

    public static List<WeekViewEvent> actionsToWeekViewEvents(List<Action> list, Context context) {
        List<WeekViewEvent> result = new ArrayList<>();

        for (Action action :
                list) {
            Calendar startData = Calendar.getInstance();
            startData.setTimeInMillis(action.getStartDate().getTime());
            Calendar endData = Calendar.getInstance();
            endData.setTimeInMillis(action.getEndDate().getTime());
            if (endData.getTimeInMillis() - startData.getTimeInMillis() < 1000 * 60 * 40) {
                endData.setTimeInMillis(startData.getTimeInMillis() + (1000 * 60 * 40));
            }

            String description = (action.getDescription() == null) ? "" : action.getDescription();

            ActionWeek actionWeek = new ActionWeek(action.getId(), action.getType() + "\n" + description,
                    action.getType(), startData, endData);

            actionWeek.setColor(SharedPrefUtils.getColor(context, actionWeek.getType()));

            result.add(actionWeek);
        }
        return result;
    }
}
