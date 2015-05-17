package com.maxml.timer.activity.authorization;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.maxml.timer.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

/**
 * Created by Lantar on 22.04.2015.
 */
public class ForgotPassword extends Activity {

    TextView tfEmail;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        tfEmail = (TextView) findViewById(R.id.textFPEmail);
    }
    public void onClick(View v){
        ParseUser.requestPasswordResetInBackground(tfEmail.getText().toString(),
                new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // An email was successfully sent with reset instructions.
                            Toast toast = Toast.makeText(getApplicationContext(), "An email was successfully sent with reset instructions.", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            // Something went wrong. Look at the ParseException to see what's up.
                            Toast toast = Toast.makeText(getApplicationContext(), "Wrong email.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }
}
