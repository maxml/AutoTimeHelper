package com.maxml.timer.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maxml.timer.R;
import com.maxml.timer.controllers.UserController;
import com.maxml.timer.entity.User;

public class CreateUserActivity extends Activity {
	
	private TextView entLogin;
	private TextView entPassword;
	private TextView entRPassword;
	private TextView entEmail;
	
	private Button btnCreate;
	private UserController c;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_user);
		Log.d("User", "start onCreate create user activity ");
		
		entLogin = (TextView) findViewById(R.id.textCreateLogin);
		entPassword = (TextView) findViewById(R.id.textCreatePassword);
		entRPassword = (TextView) findViewById(R.id.textCreateRepeatPassword);
		entEmail = (TextView) findViewById(R.id.textCreateEmail);
		
		btnCreate = (Button) findViewById(R.id.btnCreate);
		btnCreate.setOnClickListener(new OnClickListener() {
			
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
	}
	
	public void createUser() {
		Log.d("User", "create user");
		User user = new User();
		user.setUsername(entLogin.getText().toString());
		user.setPassword(entPassword.getText().toString());
		user.setEmail(entEmail.getText().toString());
		
		c = new UserController();
		c.addUser(user);
		
		Toast.makeText(CreateUserActivity.this, "successfully", Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(CreateUserActivity.this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}