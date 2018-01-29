package com.maxml.timer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.maxml.timer.MainActivity;
import com.maxml.timer.R;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Events;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, View.OnClickListener {

    private TextView entLogin;
    private TextView entPassword;
    private ProgressBar pbLoad;
//    private BootstrapButton bLoginAnonymously;
    private BootstrapButton bLogin;
    private BootstrapButton bSignIn;
    private BootstrapButton bForgotPassword;

    private EventBus eventBus;
    private DbController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Log.d("User", "start login activity");

        initController();

        initUI();
        setListeners();
    }

    private void initController() {
        eventBus = new EventBus();
        dbController = new DbController(this, eventBus);
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

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (requestCode){
            case Constants.REQUEST_LOCATION_PERMISSIONS:
                loginOk();
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        switch (requestCode){
            case Constants.REQUEST_LOCATION_PERMISSIONS:
                loginOk();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Subscribe
    public void onReceiveUserApiEvent(Events.DbResult event) {
        switch (event.getResultStatus()) {
            case Constants.EVENT_DB_RESULT_OK:
                // sign in successful
                pbLoad.setVisibility(View.INVISIBLE);
                checkPermissions();
                break;
            case Constants.EVENT_DB_RESULT_ERROR:
                // sign in error
                pbLoad.setVisibility(View.INVISIBLE);
                incorrect();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_login:
                login();
                break;
//            case R.id.b_login_anonymously:
//                loginAsAnonymously();
//                break;
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

    private void loginAsAnonymously() {
        pbLoad.setVisibility(View.VISIBLE);
        dbController.loginAnonymously();
    }

    private void login() {

        String login = entLogin.getText().toString();
        String password = entPassword.getText().toString();
        if (!TextUtils.isEmpty(login) && !TextUtils.isEmpty(password)) {
            dbController.login(login, password);
            pbLoad.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, R.string.error_input_data, Toast.LENGTH_SHORT).show();
        }
    }

    @AfterPermissionGranted(Constants.REQUEST_LOCATION_PERMISSIONS)
    void checkPermissions() {
        String[] perms = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            loginOk();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.text_dialog_location_permission),
                    Constants.REQUEST_LOCATION_PERMISSIONS, perms);
        }
    }

    private void initUI() {
        entLogin = findViewById(R.id.et_login);
        entPassword = findViewById(R.id.et_password);
        pbLoad = findViewById(R.id.pb_load);
//        bLoginAnonymously = findViewById(R.id.b_login_anonymously);
        bLogin = findViewById(R.id.b_login);
        bSignIn = findViewById(R.id.b_sign_in);
        bForgotPassword = findViewById(R.id.b_forgot_password);
    }

    private void setListeners() {
//        bLoginAnonymously.setOnClickListener(this);
        bLogin.setOnClickListener(this);
        bSignIn.setOnClickListener(this);
        bForgotPassword.setOnClickListener(this);
    }

    private void incorrect() {
        Toast.makeText(getApplicationContext(), R.string.toast_incorrect_login_or_password, Toast.LENGTH_SHORT)
                .show();
    }

    private void loginOk() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}