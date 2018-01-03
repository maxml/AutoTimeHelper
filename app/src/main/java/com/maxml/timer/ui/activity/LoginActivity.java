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
import com.maxml.timer.R;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.database.UserDAO;
import com.maxml.timer.controllers.ReceiverService;
import com.maxml.timer.entity.User;
import com.maxml.timer.entity.Events;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.SharedPrefUtils;
import com.maxml.timer.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginActivity extends Activity {
    protected static final int CONNECTION_OK = 1;

    private TextView entLogin;
    private TextView entPassword;
    private ProgressBar pbLoad;

    private EventBus eventBus;
    private DbController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Log.d("User", "start login activity");

        eventBus = new EventBus();
        dbController = new DbController(this, eventBus);

        entLogin = (TextView) findViewById(R.id.et_login);
        entPassword = (TextView) findViewById(R.id.et_password);
        pbLoad = (ProgressBar) findViewById(R.id.pb_load);

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
                pbLoad.setVisibility(View.INVISIBLE);
                loginOk();
                break;
            case Constants.EVENT_DB_RESULT_ERROR:
                // sign in error
                pbLoad.setVisibility(View.INVISIBLE);
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
        pbLoad.setVisibility(View.VISIBLE);
        dbController.loginAnonymously();
    }

    public void login() {
        pbLoad.setVisibility(View.VISIBLE);
        dbController.login(entLogin.getText().toString(), entPassword.getText().toString());
    }

    private void incorrect() {
        Toast.makeText(getApplicationContext(), R.string.toast_incorrect_login_or_password, Toast.LENGTH_SHORT)
                .show();
    }

    public void loginOk() {
        initService();
        User user = UserDAO.getCurrentUser();
        SharedPrefUtils.saveCurrentUser(this, user);
        Toast.makeText(getApplicationContext(), R.string.toast_logged, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void initService() {
        // if service not instant yet, start one
        if (!Utils.isServiceRunning(this, ReceiverService.class)) {
            Log.d(Constants.TAG, "start new service instance");
            Intent serviceIntent = new Intent(this, ReceiverService.class);
            startService(serviceIntent);
        }
    }
}
