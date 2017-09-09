package com.maxml.timer.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.maxml.timer.MainActivity;
import com.maxml.timer.R;
import com.maxml.timer.api.UserAPI;
import com.maxml.timer.entity.User;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.SharedPrefUtils;

/**
 * Created by Lantar on 22.04.2015.
 */
public class LoginActivity extends Activity {
    protected static final int CONNECTION_OK = 1;

    private Handler handler;
    private UserAPI userAPI;
    private TextView entLogin;
    private TextView entPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Log.d("User", "start login activity");

        initHandler();
        userAPI = new UserAPI(this, handler);

        entLogin = (TextView) findViewById(R.id.textLogin);
        entPassword = (TextView) findViewById(R.id.textPassword);

    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.RESULT_OK:
                        // user already sign in
                        loginOk();
                        break;
                    case Constants.RESULT_FALSE:
                        // user not sign yet, start auth
                        break;
                }
            }
        };
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                login();
                break;
            case R.id.btnSignIn:
                Intent intent = new Intent(this, CreateUserActivity.class);
                startActivity(intent);

                break;
            case R.id.btnForgotPass:
                Intent intentForgot = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intentForgot);
                break;
        }
    }

    public void login() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.RESULT_OK:
                        // sign in successful
                        loginOk();
                        break;
                    case Constants.RESULT_FALSE:
                        // sign in error
                        incorrect();
                        break;
                }
            }
        };
        userAPI.login(entLogin.getText().toString(), entPassword.getText().toString());
    }

    private void incorrect() {
        Toast.makeText(getApplicationContext(), "Incorrect login or password", Toast.LENGTH_SHORT)
                .show();
    }

    public void loginOk() {
        User user = userAPI.getCurrentUser();
        SharedPrefUtils.saveCurrentUser(this, user);
        Toast.makeText(getApplicationContext(), "Logined", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        userAPI.attachListener();
    }

    @Override
    protected void onStop() {
        userAPI.removeListener();
        super.onStop();
    }
}
