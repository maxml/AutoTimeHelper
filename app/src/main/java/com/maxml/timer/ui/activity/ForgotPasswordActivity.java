package com.maxml.timer.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maxml.timer.R;
import com.maxml.timer.api.UserAPI;
import com.maxml.timer.controllers.Controller;
import com.maxml.timer.entity.eventBus.Events;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ForgotPasswordActivity extends Activity {

    protected static final int CONNECTION_OK = 1;
    private TextView tvEmail;
    private ProgressBar progressBar;
    private Controller controller;
    private EventBus eventBus;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        tvEmail = (TextView) findViewById(R.id.textFPEmail);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
        controller.registerEventBus(eventBus);
    }

    @Override
    protected void onStop() {
        controller.unregisterEventBus(eventBus);
        eventBus.unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onReceiveUserApiEvent(Events.DbResult event) {
        switch (event.getResultStatus()) {
            case Constants.EVENT_DB_RESULT_OK:
                // sign in successful
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),
                        "An email was successfully sent with reset instructions.", Toast.LENGTH_SHORT).show();
                break;
            case Constants.EVENT_DB_RESULT_ERROR:
                // sign in error
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Wrong email.", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public void onClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
     controller.forgotPassword(tvEmail.getText().toString());
    }
}
