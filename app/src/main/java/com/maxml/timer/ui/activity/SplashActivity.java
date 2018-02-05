package com.maxml.timer.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.maxml.timer.MainActivity;
import com.maxml.timer.R;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.controllers.ReceiverService;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.NetworkUtil;
import com.maxml.timer.util.Utils;

import org.greenrobot.eventbus.EventBus;

public class SplashActivity extends AppCompatActivity {
    private EventBus eventBus;
    private DbController dbController;
    private TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initService();
        initController();

        AsyncSplash asyncSplash = new AsyncSplash();
        asyncSplash.execute();

        tvMessage = findViewById(R.id.tv_message);
    }

    private void initService() {
        // if service not instant yet, start one
        if (!Utils.isServiceRunning(this, ReceiverService.class)) {
            Log.d(Constants.TAG, "start new service instance");
            Intent serviceIntent = new Intent(this, ReceiverService.class);
            startService(serviceIntent);
        }
    }

    private void initController() {
        eventBus = new EventBus();
        dbController = new DbController(this, eventBus);
    }

    private class AsyncSplash extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            boolean isLogged = FirebaseAuth.getInstance().getCurrentUser() != null;
            if (!isLogged) {
                if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    dbController.loginAnonymously();
                } else {
                    publishProgress();

                    SystemClock.sleep(2000);

                    finish();
                    return null;
                }
            }

            SystemClock.sleep(2000);

            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            tvMessage.setText(R.string.no_network);
            super.onProgressUpdate(values);
        }
    }
}
