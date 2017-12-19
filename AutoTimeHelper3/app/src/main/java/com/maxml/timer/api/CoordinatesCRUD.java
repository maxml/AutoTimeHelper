package com.maxml.timer.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maxml.timer.controllers.Controller;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.User;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CoordinatesCRUD {

    private Controller controller;
    private DatabaseReference pathRef;

    public CoordinatesCRUD(Controller controller) {
        this.controller = controller;
        User user = UserAPI.getCurrentUser();
        if (pathRef == null && user != null) {
            pathRef = FirebaseDatabase.getInstance().getReference()
                    .child(Constants.WALK_DATABASE_PATH);
        }
    }

    public void create(String walkActionId, List<Coordinates> coordinates) {
        Log.i("Slice", " Slice starting create");
        pathRef.child(walkActionId).setValue(coordinates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    controller.sendDbResultOk();
                } else {
                    controller.sendDbResultError();
                }
            }
        });
    }


//    public void read(String id, final DbMessage messageForResult) {
//        if (id == null) {
//            EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//            return;
//        }
//        pathRef.child(Constants.CALL_DATABASE_PATH)
//                .orderByKey()
//                .equalTo(id)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            String dayCount = dataSnapshot.getValue(String.class);
//                            read(Utils.getDate(dayCount), messageForResult);
//                            return;
//                        }
//                        EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_OK, messageForResult));
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//                    }
//                });
//    }
//
//
//    public void read(Date date, final DbMessage messageForResult) {
//        if (date == null) {
//            EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//            return;
//        }
//        pathRef.child(Utils.getDayCount(date) + "")
//                .child(Constants.CALL_DATABASE_PATH)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            List<CallAction> list = new ArrayList<>();
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                CallAction call = snapshot.getValue(CallAction.class);
//                                if (!call.isDeleted()) {
//                                    list.add(call);
//                                }
//                            }
//                            // send result
//                            EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_LIST, list));
//
//                        } else {
//                            EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//                    }
//                });
//    }


//    public void update(String id, Map<String, Object> changes) {
//        if (id == null || id.equals("") || changes == null) {
//            EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//            return;
//        }
//
//        pathRef.child(Constants.CALL_DATABASE_PATH)
//                .child(id)
//                .updateChildren(changes).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_OK));
//                } else {
//                    EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//                }
//            }
//        });
//    }
//
//    public void delete(final CallAction call) {
//        if (call == null) {
//            EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//            return;
//        }
//        final String id = call.getId();
//        pathRef.child(Constants.CALL_DATABASE_PATH)
//                .child(id)
//                .removeValue()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            pathRef.child(Constants.SORT_BY_DATE_PATH)
//                                    .child(Utils.getDayCount(call.getStartDate()) + "")
//                                    .child(Constants.CALL_DATABASE_PATH)
//                                    .child(id)
//                                    .removeValue()
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_OK));
//                                            } else {
//                                                EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//                                            }
//                                        }
//                                    });
//
//                        } else {
//                            EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//                        }
//                    }
//                });
//    }

//    public void sync(Table table) {
//        Log.i("Slice", "" + NetworkStatus.isConnected);
//
//        Map<String, Object> updateList = new HashMap<>();
//        Log.i("Slice", "Slice synchronized start");
//        for (Slice slice : table.getList()) {
//            // if slice already exist in DB it has no null id field
//            // if field null - save new slice to DB
//            if (slice.getId() == null) {
//                create(slice);
//            } else {
//                updateList.put(slice.getId(), slice);
//            }
//        }
//        // update items from updateList in DB
//        pathRef.updateChildren(updateList).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_OK));
//                } else {
//                    EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//                }
//            }
//        });
//
//
//    }
}
