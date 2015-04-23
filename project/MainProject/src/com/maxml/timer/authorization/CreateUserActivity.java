package com.maxml.timer.authorization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import com.maxml.timer.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateUserActivity extends Activity {

    TextView entLogin;
    TextView entPassword;
    TextView entRPassword;
    TextView entEmail;
//    Context context = getApplicationContext();

    int duration = Toast.LENGTH_SHORT;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);

        entLogin = (TextView) findViewById(R.id.textCreateLogin);
        entPassword = (TextView) findViewById(R.id.textCreatePassword);
        entRPassword = (TextView) findViewById(R.id.textCreateRepeatPassword);
        entEmail = (TextView) findViewById(R.id.textCreateEmail);



    }

    public void createUser() {
        try {
            ParseUser user = new ParseUser();
            user.setUsername(entLogin.getText().toString());
            user.setPassword(entPassword.getText().toString());
            user.setEmail(entEmail.getText().toString());

            // other fields can be set just like with ParseObject


            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Hooray! Let them use the app now.
                    } else {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                    }
                }
            });
        } catch (Exception e) {
            Log.d("myLog", "" + e);
        }
    }


    public void onClick(View v) {
        if (v.getId() == R.id.btnCreate) {
            if (entPassword.getText().toString().equals(entRPassword.getText().toString())) {
                createUser();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                Toast toast = Toast.makeText(getApplicationContext(), "User " + entLogin.getText().toString() + " created!", duration);
                toast.show();
                finish();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Use different passwords", duration);
                toast.show();
            }

        }
    }
}