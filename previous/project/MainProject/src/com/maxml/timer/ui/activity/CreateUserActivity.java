package com.maxml.timer.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maxml.timer.MainActivity;
import com.maxml.timer.R;
import com.maxml.timer.api.UserAPI;
import com.maxml.timer.entity.User;

public class CreateUserActivity extends Activity {
	
	private TextView entLogin;
	private TextView entPassword;
	private TextView entRPassword;
	private TextView entEmail;
	
	private Button btnCreate;
	
	protected int CONNECTION_OK = 1;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_user);
		Log.d("User", "start onCreate create user activity ");
		
		entLogin = (TextView) findViewById(R.id.textCreateLogin);
		entPassword = (TextView) findViewById(R.id.textCreatePassword);
		entRPassword = (TextView) findViewById(R.id.textRepeatPassword);
		entEmail = (TextView) findViewById(R.id.textCreateEmail);
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
		
	/*	btnCreate.setOnClickListener(new OnClickListener() {
			
			@Override
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
					Toast.makeText(getApplicationContext(), "Something isn't correct", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}*/
	
	private void autorisation() {
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
		User user = new User();
		user.setUsername(entLogin.getText().toString());
		user.setPassword(entPassword.getText().toString());
		user.setEmail(entEmail.getText().toString());
		
		UserAPI c = new UserAPI();
		c.sync(user);
		c.handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == CONNECTION_OK) {
					autorisation();
				} else {
					incorrect();
				}
			};
		};
	}
}