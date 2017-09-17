package com.maxml.timer.api;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maxml.timer.MyLog;
import com.maxml.timer.entity.User;
import com.maxml.timer.util.Constants;

public class UserAPI {
    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Activity activity;
    private Handler handler = new Handler();
    private boolean isLogined = false;

    public UserAPI(Activity activity, Handler handler) {
        this.handler = handler;
        this.activity = activity;
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
                    MyLog.d("onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    MyLog.d("onAuthStateChanged:signed_out");

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
            handler.sendEmptyMessage(Constants.RESULT_FALSE);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        MyLog.d("createUserWithEmail:onComplete:" + task.isSuccessful());

                       if (!task.isSuccessful()) {
                            handler.sendEmptyMessage(Constants.RESULT_FALSE);
                        }else {
                            handler.sendEmptyMessage(Constants.RESULT_OK);
                        }
                    }
                });
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        MyLog.d("signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            handler.sendEmptyMessage(Constants.RESULT_FALSE);
                        }else {
                            handler.sendEmptyMessage(Constants.RESULT_OK);
                        }
                    }
                });
    }

    public static User getCurrentUser() {
        User user = new User();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) return null;
        String objectId = firebaseUser.getProviderId();
        String username = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();
        Uri photo = firebaseUser.getPhotoUrl();
        user.setId(objectId);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoto(photo);
        return user;
    }

    public void sentPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            MyLog.d("Email sent.");
                            handler.sendEmptyMessage(Constants.RESULT_OK);
                        }else {
                            handler.sendEmptyMessage(Constants.RESULT_FALSE);
                        }
                    }
                });    }

    public void updateName(final String name, final String id) {
        Log.d("UserAPI", "start updateName");
//		ParseQuery<ParseUser> query = ParseUser.getQuery();
//		query.whereEqualTo(USERNAME, ParseUser.getCurrentUser().getUsername());
//		query.findInBackground(new FindCallback<ParseUser>() {
//			public void done(List<ParseUser> objects, ParseException e) {
//				if (e == null) {
//					for (ParseUser parseUser : objects) {
//						Log.d("UserAPI", "find object");
//						if (parseUser.getId().equals(id)) {
//							Log.d("UserAPI", "found, rename");
//							ParseUser.getCurrentUser().put(USERNAME, name);
//						}
//					}
//				} else {
//					Log.d("UserAPI", "problem in updateName");
//					e.printStackTrace();
//				}
//			}
//		});
    }

    public void updateEmail(final String email, final String id) {
        Log.d("UserAPI", "start updateEmail");
//		ParseQuery<ParseUser> queryOne = ParseUser.getQuery();
//		queryOne.whereEqualTo(EMAIL, email);
//		queryOne.findInBackground(new FindCallback<ParseUser>() {
//			public void done(List<ParseUser> objects, ParseException e) {
//				if (e == null) {
//					for (ParseUser parseUser : objects) {
//						if (parseUser.getId().equals(id)) {
//							ParseUser.getCurrentUser().put(EMAIL, email);
//						}
//					}
//				} else {
//					Log.d("UserAPI", "problem in updateEmail");
//					e.printStackTrace();
//				}
//			}
//		});
    }


//    public void sync(User user) {
//        Log.d("User", "start method sync");
//        if (user.getId() == null) {
//            Log.d("User", "create user");
//            create(user);
//        } else {
//            updateEmail(user.getEmail(), user.getId());
//            updateName(user.getUsername(), user.getId());
//        }
//    }
}
