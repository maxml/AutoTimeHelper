package com.maxml.timer.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maxml.timer.MainActivity;
import com.maxml.timer.R;
import com.maxml.timer.controllers.ActionController;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Events;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CreateUserActivity extends Activity {

    private TextView entLogin;
    private TextView entPassword;
    private TextView entRPassword;
    private TextView entEmail;
    private ProgressBar progressBar;

    private DbController dbController;
    private EventBus eventBus;

    protected int CONNECTION_OK = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);
        Log.d("User", "start onCreate create user activity ");

        eventBus = new EventBus();
        dbController = new DbController(this, eventBus);

        entLogin = (TextView) findViewById(R.id.textCreateLogin);
        entPassword = (TextView) findViewById(R.id.textCreatePassword);
        entRPassword = (TextView) findViewById(R.id.textRepeatPassword);
        entEmail = (TextView) findViewById(R.id.textCreateEmail);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
        dbController.registerEventBus(eventBus);
    }

    @Override
    protected void onStop() {
        eventBus.unregister(this);
        dbController.unregisterEventBus(eventBus);
        super.onStop();
    }

    public void onClick(View v) {
        if (!entLogin.getText().toString().equals("")
                && !entPassword.getText().toString().equals("")
                && !entEmail.getText().toString().equals("")
                && !entRPassword.getText().toString().equals("")) {
            if (entPassword.getText().toString().equals(entRPassword.getText().toString())) {
                Log.d("User", "start create ");
                createUser();
            } else {
                Toast.makeText(getApplicationContext(), "Use different passwords", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Something isn't correct", Toast.LENGTH_SHORT).show();
        }
    }


    private void authorisation() {
        Toast.makeText(CreateUserActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreateUserActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void incorrect() {
        Toast.makeText(CreateUserActivity.this, "Please check and try again", Toast.LENGTH_SHORT)
                .show();

    }

    @Subscribe
    public void onReceiveUserAPIEvent(Events.DbResult event){
        switch (event.getResultStatus()) {
            case Constants.EVENT_DB_RESULT_OK:
                progressBar.setVisibility(View.INVISIBLE);
                authorisation();
                break;
            case Constants.EVENT_DB_RESULT_ERROR:
                progressBar.setVisibility(View.INVISIBLE);
                incorrect();
                break;
        }

    }

    public void createUser() {
        Log.d("User", "create user");
        progressBar.setVisibility(View.VISIBLE);
        dbController.createUser(entEmail.getText().toString(), entPassword.getText().toString());
    }
}