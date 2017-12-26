package com.maxml.timer.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.maxml.timer.MainActivity;
import com.maxml.timer.MyLog;
import com.maxml.timer.R;
import com.maxml.timer.api.UserAPI;
import com.maxml.timer.controllers.Controller;
import com.maxml.timer.controllers.GeneralService;
import com.maxml.timer.entity.User;
import com.maxml.timer.entity.eventBus.Events;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.SharedPrefUtils;
import com.maxml.timer.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginActivity extends Activity {
    protected static final int CONNECTION_OK = 1;

    private EventBus eventBus;
    private Controller controller;

    private TextView entLogin;
    private TextView entPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Log.d("User", "start login activity");

        initService();
        eventBus = new EventBus();
        controller = new Controller(this, eventBus);

        entLogin = (TextView) findViewById(R.id.et_login);
        entPassword = (TextView) findViewById(R.id.et_password);
        progressBar = (ProgressBar) findViewById(R.id.pb_load);

        boolean isLogged = FirebaseAuth.getInstance().getCurrentUser() != null;
        if (isLogged) {
            loginOk();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    protected void onStop() {
        eventBus.unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onReceiveUserApiEvent(Events.DbResult event) {
        switch (event.getResultStatus()) {
            case Constants.EVENT_DB_RESULT_OK:
                // sign in successful
                progressBar.setVisibility(View.INVISIBLE);
                loginOk();
                break;
            case Constants.EVENT_DB_RESULT_ERROR:
                // sign in error
                progressBar.setVisibility(View.INVISIBLE);
                incorrect();
                break;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_login:
                login();
                break;
            case R.id.b_login_anonymously:
                loginAsAnonymously();
                break;
            case R.id.b_sign_in:
                Intent intent = new Intent(this, CreateUserActivity.class);
                startActivity(intent);
                break;
            case R.id.b_forgot_password:
                Intent intentForgot = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intentForgot);
                break;
        }
    }

    public void loginAsAnonymously() {
        progressBar.setVisibility(View.VISIBLE);
        controller.loginAnonymously();
    }

    public void login() {
        progressBar.setVisibility(View.VISIBLE);
        controller.login(entLogin.getText().toString(), entPassword.getText().toString());
    }

    private void incorrect() {
        Toast.makeText(getApplicationContext(), "Incorrect login or password", Toast.LENGTH_SHORT)
                .show();
    }

    public void loginOk() {
        User user = UserAPI.getCurrentUser();
        SharedPrefUtils.saveCurrentUser(this, user);
        Toast.makeText(getApplicationContext(), "Logined", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void initService() {
        // if service not instant yet, start one
        if (!Utils.isServiceRunning(this, GeneralService.class)) {
            MyLog.d("start new service instance");
            Intent serviceIntent = new Intent(this, GeneralService.class);
            startService(serviceIntent);
        }
    }


}
