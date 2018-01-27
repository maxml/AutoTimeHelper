package com.maxml.timer.database;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.User;
import com.maxml.timer.util.Constants;

public class UserDAO {
    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DbController dbController;

    public UserDAO(DbController dbController) {
        this.dbController = dbController;
        initAuth();
    }

    private void initAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(Constants.TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(Constants.TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    // call in onStart
    public void attachListener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    // call in onStop
    public void removeListener() {
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }

    public void create(String email, String password) {
        Log.d("User", "start method create in UserCRUD");
        if (email == null || password == null) {
            dbController.sendDbResultError();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(Constants.TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            dbController.sendDbResultError();
                        } else {
                            dbController.sendDbResultOk();
                        }
                    }
                });
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(Constants.TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            dbController.sendDbResultError();
                        } else {
                            dbController.sendDbResultOk();
                        }
                    }
                });
    }

    public void loginAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dbController.sendDbResultOk();
                        } else {
                            dbController.sendDbResultError();
                        }
                    }
                });
    }

    public void logout() {
        mAuth.signOut();
    }

    public static User getCurrentUser() {
        User user = new User();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return new User();
        }
        String objectId = firebaseUser.getUid();
        String username = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();
        Uri photo = firebaseUser.getPhotoUrl();
        boolean isAnonymously = (email == null || email.isEmpty());
        user.setId(objectId);
        user.setUsername(username);
        user.setEmail(email);
        user.setAnonymously(isAnonymously);
        if (photo != null) {
            user.setPhoto(photo.toString());
        } else {
            user.setPhoto("");
        }
        return user;
    }

    public static String getCurrentUserId() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            return "";
        }
    }

    public void sentPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constants.TAG, "Email sent.");
                            dbController.sendDbResultOk();
                        } else {
                            dbController.sendDbResultError();
                        }
                    }
                });
    }

    private boolean isAnonymously() {
        return getCurrentUser().getEmail() == null;
    }

    public void updateName(final String name) {
        FirebaseUser user = mAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                .Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
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

    public void updateEmail(final String email) {
        //TODO don`t work changing email
        FirebaseUser user = mAuth.getCurrentUser();

        user.updateEmail(email)
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

    public void updatePhoto(Uri uri) {
        FirebaseUser user = mAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                .Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(profileUpdates)
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


//    public void sync(User user) {
//        Log.d("User", "start method sync");
//        if (user.getId() == null) {
//            Log.d("User", "create user");
//            create(user);
//        } else {
//            updateUserEmail(user.getEmail(), user.getId());
//            updateUserName(user.getUsername(), user.getId());
//        }
//    }
}
