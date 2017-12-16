package com.maxml.timer.api;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxml.timer.controllers.Controller;
import com.maxml.timer.entity.DbReturnData;
import com.maxml.timer.entity.Table;
import com.maxml.timer.entity.actions.Action;
import com.maxml.timer.entity.eventBus.DbMessage;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.EventBusType;
import com.maxml.timer.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

public class TableCRUD {

    private DatabaseReference actionRef;
    private EventBus dbEventBus;

    public TableCRUD(Context context) {
        Controller controller = new Controller(context);
        dbEventBus = controller.getEventBus(EventBusType.DB);
        actionRef = FirebaseDatabase.getInstance().getReference()
                .child(Constants.USER_DATABASE_PATH)
                .child(UserAPI.getCurrentUserId());
    }

    public void read(final Date date, DbReturnData returnData) {
        read(Utils.getDayCount(date), returnData);
    }

    public void read(final long dayCount, final DbReturnData returnData) {
        // check valid date
        if (dayCount == 0) {
            dbEventBus.post(new DbMessage(Constants.EVENT_DB_RESULT_ERROR, returnData));
            return;
        }

        actionRef.orderByChild(Constants.COLUMN_DAY_COUNT)
                .equalTo(dayCount)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // create result message
                        DbMessage resultMessage = new DbMessage(Constants.EVENT_DB_RESULT_OK, returnData);
                        // get result data
                        Table table = new Table();
                        table.setDay(dayCount);
                        resultMessage.setData(table);

                        if (!dataSnapshot.exists()) {
                            // add data for feedback
                            dbEventBus.post(resultMessage);
                            return;
                        }

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Action action = snapshot.getValue(Action.class);
                            switch (action.getType()) {
                                case Constants.EVENT_REST_ACTION:
                                    table.addRest(action);
                                    break;
                                case Constants.EVENT_WALK_ACTION:
                                    table.addWalk(action);
                                    break;
                                case Constants.EVENT_WORK_ACTION:
                                    table.addWork(action);
                                    break;
                                case Constants.EVENT_CALL_ACTION:
                                    table.addCall(action);
                                    break;
                            }
                        }
                        dbEventBus.post(resultMessage);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dbEventBus.post(new DbMessage(Constants.EVENT_DB_RESULT_ERROR, returnData));
                    }
                });
    }
}
