package com.maxml.timer.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maxml.timer.MainActivity;
import com.maxml.timer.R;
import com.maxml.timer.api.UserAPI;
import com.maxml.timer.entity.User;
import com.maxml.timer.util.Constants;

public class CreateUserActivity extends Activity {

    private TextView entLogin;
    private TextView entPassword;
    private TextView entRPassword;
    private TextView entEmail;
    private ProgressBar progressBar;

    protected int CONNECTION_OK = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);
        Log.d("User", "start onCreate create user activity ");

        entLogin = (TextView) findViewById(R.id.textCreateLogin);
        entPassword = (TextView) findViewById(R.id.textCreatePassword);
        entRPassword = (TextView) findViewById(R.id.textRepeatPassword);
        entEmail = (TextView) findViewById(R.id.textCreateEmail);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
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

    public void createUser() {
        Log.d("User", "create user");
        progressBar.setVisibility(View.VISIBLE);
        UserAPI c = new UserAPI(this, new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.RESULT_OK:
                        progressBar.setVisibility(View.INVISIBLE);
                        authorisation();
                        break;
                    case Constants.RESULT_FALSE:
                        progressBar.setVisibility(View.INVISIBLE);
                        incorrect();
                        break;
                }
            }
        });
        c.create(entEmail.getText().toString(), entPassword.getText().toString());
    }
}