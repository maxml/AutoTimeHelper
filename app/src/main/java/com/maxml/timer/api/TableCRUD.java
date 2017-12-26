package com.maxml.timer.api;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxml.timer.controllers.Controller;
import com.maxml.timer.entity.Table;
import com.maxml.timer.entity.actions.Action;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.Utils;


import java.util.Date;

public class TableCRUD {

    private DatabaseReference actionRef;
    private Controller controller;

    public TableCRUD(Controller controller) {
        this.controller = controller;
        actionRef = FirebaseDatabase.getInstance().getReference()
                .child(Constants.USER_DATABASE_PATH)
                .child(UserAPI.getCurrentUserId());
    }

    public void read(final Date date) {
        read(Utils.getDayCount(date));
    }

    public void read(final long dayCount) {
        // check valid date
        if (dayCount == 0) {
            controller.sendDbResultError();
            return;
        }

        actionRef.orderByChild(Constants.COLUMN_DAY_COUNT)
                .equalTo(dayCount)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get result data
                        Table table = new Table();
                        table.setDay(dayCount);

                        if (!dataSnapshot.exists()) {
                            controller.sendTableFromDb(table);
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
                        controller.sendTableFromDb(table);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        controller.sendDbResultError();
                    }
                });
    }
}
