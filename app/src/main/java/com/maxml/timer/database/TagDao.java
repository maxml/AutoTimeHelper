package com.maxml.timer.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxml.timer.entity.User;
import com.maxml.timer.util.Constants;

public class TagDao {
    private DatabaseReference actionRef;

    public TagDao() {
        User user = UserDAO.getCurrentUser();
        if (actionRef == null && user != null) {
            actionRef = FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Constants.USER_DATABASE_PATH)
                    .child(user.getId());
        }
    }

    public void createDescription(final String description) {
        actionRef
                .child(Constants.DESCRIPTION_DATABASE_PATH)
                .child(description)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            int count = dataSnapshot.getValue(Integer.class);
                            actionRef
                                    .child(Constants.DESCRIPTION_DATABASE_PATH)
                                    .child(description)
                                    .setValue(++count);
                        } else {
                            actionRef
                                    .child(Constants.DESCRIPTION_DATABASE_PATH)
                                    .child(description)
                                    .setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void updateDescription(final String oldDescription, final String description) {
        actionRef
                .child(Constants.DESCRIPTION_DATABASE_PATH)
                .child(oldDescription)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            int count = dataSnapshot.getValue(Integer.class);
                            if (count > 1) {
                                actionRef
                                        .child(Constants.DESCRIPTION_DATABASE_PATH)
                                        .child(oldDescription)
                                        .setValue(--count);
                            } else {
                                actionRef
                                        .child(Constants.DESCRIPTION_DATABASE_PATH)
                                        .child(oldDescription)
                                        .removeValue();
                            }
                        }
                        createDescription(description);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void removeDescription(final String description) {
        actionRef
                .child(Constants.DESCRIPTION_DATABASE_PATH)
                .child(description)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            int count = dataSnapshot.getValue(Integer.class);
                            if (count > 1) {
                                actionRef
                                        .child(Constants.DESCRIPTION_DATABASE_PATH)
                                        .child(description)
                                        .setValue(--count);
                            } else {
                                actionRef
                                        .child(Constants.DESCRIPTION_DATABASE_PATH)
                                        .child(description)
                                        .removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
