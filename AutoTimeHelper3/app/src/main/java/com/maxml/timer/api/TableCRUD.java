package com.maxml.timer.api;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxml.timer.entity.Table;
import com.maxml.timer.entity.actions.Action;
import com.maxml.timer.entity.eventBus.dbMessage.DbMessage;
import com.maxml.timer.entity.eventBus.dbMessage.DbResultMessage;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

public class TableCRUD {

    private DatabaseReference actionRef;

    public TableCRUD() {
        actionRef = FirebaseDatabase.getInstance().getReference()
                .child(Constants.USER_DATABASE_PATH)
                .child(UserAPI.getCurrentUserId());
    }

//    public void create(WalkAction walkAction) {
//        Log.i("Slice", " Slice starting create");
//
//        // get Firebase id
//        String dbId = actionRef.push().getKey();
//        walkAction.setId(dbId);
//
//        // create update map
//        Map<String, Object> data = new HashMap<>();
//
//        // put data for sort by day
//        long dayCount = Utils.getDayCount(walkAction.getStartDate());
//        if (dayCount == -1) {
//            EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//            return;
//        }
//        data.put("/" + Constants.SORT_BY_DATE_PATH
//                + "/" + dayCount
//                + "/" + Constants.WALK_DATABASE_PATH, dbId);
//
//        // put data for call db
//        data.put("/" + Constants.WALK_DATABASE_PATH, walkAction);
//
//        try {
//            actionRef.updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_OK));
//                    } else {
//                        EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//                    }
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void read(final Date date, DbMessage messageForResult) {
        read(Utils.getDayCount(date), messageForResult);
    }

    public void read(final long dayCount, final DbMessage messageForResult) {
        if (dayCount == 0) {
            EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR, messageForResult));
            return;
        }

        actionRef.orderByChild(Constants.COLUMN_DAY_COUNT)
                .equalTo(dayCount)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Table table = new Table();
                        table.setDay(dayCount);
                        messageForResult.setData(table);
                        if (!dataSnapshot.exists()) {
                            EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_OK, messageForResult));
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
                        EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_OK, messageForResult));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR, messageForResult));
                    }
                });
    }

}
