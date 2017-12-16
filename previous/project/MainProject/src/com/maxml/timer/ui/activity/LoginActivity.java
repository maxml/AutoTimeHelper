package com.maxml.timer.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.maxml.timer.MainActivity;
import com.maxml.timer.R;
import com.maxml.timer.api.UserAPI;
import com.parse.ParseUser;

/**
 * Created by Lantar on 22.04.2015.
 */
public class LoginActivity extends Activity {
	protected static final int CONNECTION_OK = 1;
	private TextView entLogin;
	private TextView entPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		Log.d("User", "start login activity");
		// comment this if u want to see login and registration pages!
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			Log.d("User", "user = " + currentUser.getUsername());
			loginOk();
		}
		entLogin = (TextView) findViewById(R.id.textLogin);
		entPassword = (TextView) findViewById(R.id.textPassword);
		
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnLogin:
				login();
			break;
			case R.id.btnSignIn:
				Intent intent = new Intent(this, CreateUserActivity.class);
				startActivity(intent);
			
			break;
			case R.id.btnForgotPass:
				Intent intentForgot = new Intent(this, ForgotPasswordActivity.class);
				startActivity(intentForgot);
			break;
		}
	}
	
	public void login() {
		UserAPI c = new UserAPI();
		c.login(entLogin.getText().toString(), entPassword.getText().toString());
		c.handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == CONNECTION_OK) {
					loginOk();
				} else {
					incorrect();
				}
			};
		};
	}
	
	private void incorrect() {
		Toast.makeText(getApplicationContext(), "Incorrect login or password", Toast.LENGTH_SHORT)
				.show();
	}
	
	public void loginOk() {
		Toast.makeText(getApplicationContext(), "Logined", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
}
