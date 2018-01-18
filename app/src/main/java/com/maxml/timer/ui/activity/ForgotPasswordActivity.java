package com.maxml.timer.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maxml.timer.R;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Events;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ForgotPasswordActivity extends Activity {

    protected static final int CONNECTION_OK = 1;

    private TextView tvEmail;
    private ProgressBar pbLoad;

    private DbController dbController;
    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        eventBus = new EventBus();
        dbController = new DbController(this, eventBus);
        tvEmail = findViewById(R.id.textFPEmail);
        pbLoad = findViewById(R.id.pb_load);
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
        dbController.registerEventBus(eventBus);
    }

    @Override
    protected void onStop() {
        dbController.unregisterEventBus(eventBus);
        eventBus.unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onReceiveUserApiEvent(Events.DbResult event) {
        switch (event.getResultStatus()) {
            case Constants.EVENT_DB_RESULT_OK:
                // sign in successful
                pbLoad.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),
                        "An email was successfully sent with reset instructions.", Toast.LENGTH_SHORT).show();
                break;
            case Constants.EVENT_DB_RESULT_ERROR:
                // sign in error
                pbLoad.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Wrong email.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onClick(View v) {
        pbLoad.setVisibility(View.VISIBLE);
        dbController.forgotPassword(tvEmail.getText().toString());
    }
}
