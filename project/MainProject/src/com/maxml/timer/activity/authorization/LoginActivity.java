package com.maxml.timer.activity.authorization;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.maxml.timer.MainActivity;
import com.maxml.timer.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Lantar on 22.04.2015.
 */
public class LoginActivity extends Activity {
    TextView entLogin;
    TextView entPassword;
    int duration = Toast.LENGTH_SHORT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        entLogin = (TextView) findViewById(R.id.textLogin);
        entPassword = (TextView) findViewById(R.id.textPassword);


    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnLogin:
                autorization();
                break;
            case R.id.btnSignIn:
                Intent intent = new Intent(this, CreateUserActivity.class);
                startActivityForResult(intent, 1);

                break;
            case R.id.btnForgotPass:
                Intent intentF = new Intent(this, ForgotPassword.class);
                startActivityForResult(intentF, 1);

                break;
        }
    }

    public void autorization(){



        ParseUser.logInInBackground(entLogin.getText().toString(), entPassword.getText().toString(), new LogInCallback(){
        public void done(ParseUser user, ParseException e) {
            if (user != null) {
               loginOk();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Incorect login or password", duration);
                toast.show();
                // Signup failed. Look at the ParseException to see what happened.
            }
        }
    });
    }
    
    public void loginOk(){
    	try{
    	 Toast toast = Toast.makeText(getApplicationContext(), "Logined", duration);
         toast.show();
         Intent intent = new Intent(this, MainActivity.class); 
         startActivityForResult(intent, 1);
         Log.i("myLog","logined");
    	}catch(Exception e){
    		Log.i("myLog",""+e);
    	}
    }
}
