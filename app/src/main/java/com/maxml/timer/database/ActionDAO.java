package com.maxml.timer.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.User;
import com.maxml.timer.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionDAO {
    private DbController dbController;
    private DatabaseReference actionRef;
    private TagDao tagDao;

    public ActionDAO(DbController dbController) {
        this.dbController = dbController;
        User user = UserDAO.getCurrentUser();
        tagDao = new TagDao();
        if (actionRef == null && user != null) {
            actionRef = FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Constants.USER_DATABASE_PATH)
                    .child(user.getId());
        }
    }

    public void create(final Action action) {
        Log.i("Slice", " Slice starting create");

        String dbId = actionRef.push().getKey();
        action.setId(dbId);

        actionRef.child(dbId).setValue(action)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dbController.sendDbResultOk();
                        } else {
                            dbController.sendDbResultError();
                        }
                    }
                });
        tagDao.createDescription(action.getDescription());
    }

    public void createWalkAction(Action walk) {
        Log.i("Slice", " Slice starting create");

        final String dbId = actionRef.push().getKey();
        walk.setId(dbId);

        actionRef.child(dbId).setValue(walk).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dbController.endSavingWalkAction(dbId);
                } else {
                    dbController.sendDbResultError();
                }
            }
        });
    }

    public void getActionFromDb(String id) {
        actionRef.child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Action action = dataSnapshot.getValue(Action.class);

                        dbController.sendActionFromDb(action);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dbController.sendDbResultError();
                    }
                });
    }

    public void updateActionInDb(final Action action, final String oldDescription) {
        Map<String, Object> map = new HashMap<>();
        map.put("description", action.getDescription());
        map.put("type", action.getType());
        map.put("startDate", action.getStartDate());
        map.put("endDate", action.getEndDate());
        actionRef.child(action.getId())
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dbController.sendDbResultOk();
                        } else {
                            dbController.sendDbResultError();
                        }
                    }
                });
        tagDao.updateDescription(oldDescription, action.getDescription());
    }

    public void removeAction(String id, final String description) {
        actionRef.child(id).removeValue(
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        databaseReference.removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            dbController.sendDbResultOk();
                                        } else {
                                            dbController.sendDbResultError();
                                        }
                                    }
                                });
                    }
                });
        tagDao.removeDescription(description);
    }

    public void getAllTags() {
        actionRef
                .child(Constants.DESCRIPTION_DATABASE_PATH)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> list = new ArrayList<>();
                        for (DataSnapshot snapshot :
                                dataSnapshot.getChildren()) {
                            list.add(snapshot.getKey());
                        }
                        dbController.sendAllTags(list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
