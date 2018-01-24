package com.maxml.timer.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.maxml.timer.MainActivity;
import com.maxml.timer.R;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.controllers.ReceiverService;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.NetworkUtil;
import com.maxml.timer.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {
    private EventBus eventBus;
    private DbController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initService();
        initController();

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isLogged = FirebaseAuth.getInstance().getCurrentUser() != null;
                if (!isLogged) {
                    dbController.loginAnonymously();
                }

                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }).start();
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
}
