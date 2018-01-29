package com.maxml.timer.database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.Table;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TableDAO {

    private DatabaseReference actionRef;
    private DbController dbController;

    public TableDAO(DbController dbController) {
        this.dbController = dbController;
        actionRef = FirebaseDatabase.getInstance().getReference()
                .child(Constants.USER_DATABASE_PATH)
                .child(UserDAO.getCurrentUserId());
    }

    public void getTableByData(final Date date) {
        getTableByData(Utils.getDayCount(date));
    }

    public void getTableByData(final Date startDate, final Date entDate) {
        getTableByData(Utils.getDayCount(startDate), Utils.getDayCount(entDate));
    }

    public void getTableByData(final long dayCount) {
        // check valid date
        if (dayCount == 0) {
            dbController.sendDbResultError();
            return;
        }

        actionRef
                .orderByChild(Constants.COLUMN_DAY_COUNT)
                .equalTo(dayCount)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get result data
                        Table table = new Table();
                        table.setDay(dayCount);

                        if (!dataSnapshot.exists()) {
                            dbController.sendTableFromDb(table);
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
                        dbController.sendTableFromDb(table);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dbController.sendDbResultError();
                    }
                });
    }

    public void getTableByData(final long startDayCount, final long endDayCount) {
        // check valid date
        if (startDayCount == 0 || endDayCount == 0) {
            dbController.sendDbResultError();
            return;
        }

        final List<Table> list = new ArrayList<>();

        for (long i = endDayCount; i > startDayCount; i--) {
            final long finalI = i;
            actionRef.orderByChild(Constants.COLUMN_DAY_COUNT)
                    .equalTo(i)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // get result data
                            Table table = new Table();
                            table.setDay(finalI);

                            if (!dataSnapshot.exists()) {
                                list.add(table);
                                sendListTableIfFull(list);
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
                            list.add(table);
                            sendListTableIfFull(list);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            dbController.sendDbResultError();
                        }
                    });
        }
        Log.i("LOG", "getTableByData: " + list.size());
    }

    public void getTableByDescription(String description) {
        actionRef
                .orderByChild(Constants.COLUMN_DESCRIPTION)
                .equalTo(description)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get result data
                        Table table = new Table();

                        if (!dataSnapshot.exists()) {
                            dbController.sendTableFromDb(table);
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
                        dbController.sendTableFromDb(table);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dbController.sendDbResultError();
                    }
                });
    }

    private void sendListTableIfFull(List<Table> list) {
        if (list.size() == Constants.WEEK_COUNT_DAY) {
            dbController.sendTableFromDb(list);
        }
    }
}
