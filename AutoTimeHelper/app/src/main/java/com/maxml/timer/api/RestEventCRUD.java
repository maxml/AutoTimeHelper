package com.maxml.timer.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxml.timer.entity.User;
import com.maxml.timer.entity.actions.RestAction;
import com.maxml.timer.entity.eventBus.DbMessage;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestEventCRUD {

    private DatabaseReference userRef;

    public RestEventCRUD() {
        User user = UserAPI.getCurrentUser();
        if (userRef == null && user != null) {
            userRef = FirebaseDatabase.getInstance().getReference()
                    .child(Constants.USER_DATABASE_PATH)
                    .child(user.getId());
        }
    }

    public void create(RestAction restAction) {
        Log.i("Slice", " Slice starting create");

        // get Firebase id
        String dbId = userRef.push().getKey();
        restAction.setId(dbId);

        // create update map
        Map<String, Object> data = new HashMap<>();

        // put data for sort by day
        long dayCount = Utils.getDayCount(restAction.getStartDate());
        if (dayCount == -1) {
            EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_ERROR));
            return;
        }
        data.put("/" + Constants.SORT_BY_DATE_PATH
                + "/" + dayCount
                + "/" + Constants.REST_DATABASE_PATH, dbId);

        // put data for call db
        data.put("/" + Constants.REST_DATABASE_PATH, restAction);

        try {
            userRef.updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_OK));
                    } else {
                        EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_ERROR));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void read(String id) {
        if (id == null) {
            EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_ERROR));
            return;
        }
        userRef.child(Constants.REST_DATABASE_PATH)
                .orderByChild(Constants.COLUMN_ID)
                .equalTo(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<RestAction> list = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                RestAction rest = snapshot.getValue(RestAction.class);
                                if (!rest.isDeleted()) {
                                    list.add(rest);
                                }
                            }
                            // send result
                            if (list.size() > 0) {
                                EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_LIST, list.get(0)));
                            } else {
                                EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_ERROR));
                            }

                        } else {
                            EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_ERROR));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_ERROR));
                    }
                });
    }

    public void read(Date date) {
        if (date == null) {
            EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_ERROR));
            return;
        }
        userRef.child(Constants.SORT_BY_DATE_PATH)
                .child(Utils.getDayCount(date) + "")
                .child(Constants.REST_DATABASE_PATH)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<RestAction> list = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                RestAction rest = snapshot.getValue(RestAction.class);
                                if (!rest.isDeleted()) {
                                    list.add(rest);
                                }
                            }
                            // send result
                            EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_LIST, list));

                        } else {
                            EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_ERROR));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_ERROR));
                    }
                });
    }


    public void update(String id, Map<String, Object> changes) {
        if (id == null || id.equals("") || changes == null) {
            EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_ERROR));
            return;
        }

        userRef.child(Constants.REST_DATABASE_PATH)
                .child(id)
                .updateChildren(changes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_OK));
                } else {
                    EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_ERROR));
                }
            }
        });
    }

    public void delete(final RestAction restAction) {
        if (restAction == null) {
            EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_ERROR));
            return;
        }
        final String id = restAction.getId();
        userRef.child(Constants.REST_DATABASE_PATH)
                .child(id)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            userRef.child(Constants.SORT_BY_DATE_PATH)
                                    .child(Utils.getDayCount(restAction.getStartDate()) + "")
                                    .child(Constants.REST_DATABASE_PATH)
                                    .child(id)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_OK));
                                            } else {
                                                EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_ERROR));
                                            }
                                        }
                                    });

                        } else {
                            EventBus.getDefault().post(new DbMessage(Constants.EVENT_RESULT_ERROR));
                        }
                    }
                });
    }

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
//    }
}
