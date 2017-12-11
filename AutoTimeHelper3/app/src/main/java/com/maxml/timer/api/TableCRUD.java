package com.maxml.timer.api;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxml.timer.entity.Table;
import com.maxml.timer.entity.User;
import com.maxml.timer.entity.actions.CallAction;
import com.maxml.timer.entity.actions.RestAction;
import com.maxml.timer.entity.actions.WalkAction;
import com.maxml.timer.entity.actions.WorkAction;
import com.maxml.timer.entity.eventBus.dbMessage.DbMessage;
import com.maxml.timer.entity.eventBus.dbMessage.DbResultMessage;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableCRUD {

    private DatabaseReference userRef;

    private Table tempTable = new Table();
    private Map<String, Object> tempMap = new HashMap<>();

    public TableCRUD() {
        User user = UserAPI.getCurrentUser();
        if (userRef == null && user != null) {
            userRef = FirebaseDatabase.getInstance().getReference()
                    .child(Constants.USER_DATABASE_PATH)
                    .child(user.getId());
        }
    }

//    public void create(WalkAction walkAction) {
//        Log.i("Slice", " Slice starting create");
//
//        // get Firebase id
//        String dbId = userRef.push().getKey();
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
//            userRef.updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
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

        userRef.child(dayCount + "")
                .orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
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
                    String key = snapshot.getKey();
                    switch (key) {
                        case Constants.REST_DATABASE_PATH:
                            for (DataSnapshot rest : snapshot.getChildren()) {
                                table.getRestList().add(rest.getValue(RestAction.class));
                            }
                            break;
                        case Constants.WALK_DATABASE_PATH:
                            for (DataSnapshot walk : snapshot.getChildren()) {
                                table.getWalkList().add(walk.getValue(WalkAction.class));
                            }
                            break;
                        case Constants.WORK_DATABASE_PATH:
                            for (DataSnapshot work : snapshot.getChildren()) {
                                table.getWorkList().add(work.getValue(WorkAction.class));
                            }
                            break;
                        case Constants.CALL_DATABASE_PATH:
                            for (DataSnapshot call : snapshot.getChildren()) {
                                table.getCallList().add(call.getValue(CallAction.class));
                            }
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

//    private void readByList(Map<String, String> typeAndId, final DbMessage messageForResult) {
//        if (typeAndId == null) {
//            EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//            return;
//        }
//        // clear temp date
//        tempMap.clear();
//
//        for (Map.Entry<String, String> entry : typeAndId.entrySet()) {
//            String type = entry.getKey();
//            String id = entry.getValue();
//
//            userRef.child(type)
//                    .orderByChild(Constants.COLUMN_ID)
//                    .equalTo(id)
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                ArrayList<WalkAction> list = new ArrayList<>();
//                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                    WalkAction walk = snapshot.getValue(WalkAction.class);
//                                    if (!walk.isDeleted()) {
//                                        list.add(walk);
//                                    }
//                                }
//                                // send result
//                                if (list.size() > 0) {
//                                    messageForResult.setData(list);
//                                    EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_LIST, messageForResult));
//                                } else {
//                                    EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//                                }
//
//                            } else {
//                                EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//                        }
//                    });
//        }
//
//    }


//    public void update(String id, Map<String, Object> changes) {
//        if (id == null || id.equals("") || changes == null) {
//            EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//            return;
//        }
//
//        userRef.child(Constants.WALK_DATABASE_PATH)
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
//    public void delete(final WalkAction walk) {
//        if (walk == null) {
//            EventBus.getDefault().post(new DbResultMessage(Constants.EVENT_DB_RESULT_ERROR));
//            return;
//        }
//        final String id = walk.getId();
//        userRef.child(Constants.WALK_DATABASE_PATH)
//                .child(id)
//                .removeValue()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            userRef.child(Constants.SORT_BY_DATE_PATH)
//                                    .child(Utils.getDayCount(walk.getStartDate()) + "")
//                                    .child(Constants.WALK_DATABASE_PATH)
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
//        Map<String,Object> updateList = new HashMap<>();
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
//        sliceRef.updateChildren(updateList).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    handler.sendEmptyMessage(Constants.RESULT_OK);
//                } else {
//                    handler.sendEmptyMessage(Constants.RESULT_FALSE);
//                }
//            }
//        });
//
//    }

}
