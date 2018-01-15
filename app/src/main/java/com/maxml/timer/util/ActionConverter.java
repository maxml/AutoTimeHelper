package com.maxml.timer.util;

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

    public static List<WeekViewEvent> actionsToWeekViewEvents(List<Action> list, Resources resources) {
        List<WeekViewEvent> result = new ArrayList<>();

        for (Action action :
                list) {
            Calendar startData = Calendar.getInstance();
            startData.setTimeInMillis(action.getStartDate().getTime());
            Calendar endData = Calendar.getInstance();
            endData.setTimeInMillis(action.getEndDate().getTime());
            if (endData.getTimeInMillis() - startData.getTimeInMillis() < 1000 * 60 * 50) {
                endData.setTimeInMillis(startData.getTimeInMillis() + (1000 * 60 * 50));
            }

            String description = (action.getDescription() == null) ? "" : action.getDescription();

            ActionWeek actionWeek = new ActionWeek(action.getId(), action.getType() + "\n" + description,
                    action.getType(), startData, endData);

            makeColor(resources, actionWeek);

            result.add(actionWeek);
        }
        return result;
    }

    private static void makeColor(Resources resources, ActionWeek actionWeek) {
        if (actionWeek.getType().equalsIgnoreCase(Constants.EVENT_WALK_ACTION)) {
            actionWeek.setColor(resources.getColor(R.color.event_color_blue));
        } else if (actionWeek.getType().equalsIgnoreCase(Constants.EVENT_CALL_ACTION)) {
            actionWeek.setColor(resources.getColor(R.color.event_color_yellow));
        } else if (actionWeek.getType().equalsIgnoreCase(Constants.EVENT_WORK_ACTION)) {
            actionWeek.setColor(resources.getColor(R.color.event_color_red));
        } else if (actionWeek.getType().equalsIgnoreCase(Constants.EVENT_REST_ACTION)) {
            actionWeek.setColor(resources.getColor(R.color.event_color_green));
        }
    }
}
