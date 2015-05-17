package com.maxml.timer.ui.activity;

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
	private TextView entLogin;
	private TextView entPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		entLogin = (TextView) findViewById(R.id.textLogin);
		entPassword = (TextView) findViewById(R.id.textPassword);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			autorization();
			break;
		case R.id.btnSignIn:

			startActivityForResult(new Intent(this, CreateUserActivity.class), 1);
			// TODO 1 is hardcode. Fix

			break;
		case R.id.btnForgotPass:

			startActivityForResult(new Intent(this, ForgotPasswordActivity.class), 1);

			break;
		}
	}

	public void autorization() {

		ParseUser.logInInBackground(entLogin.getText().toString(), entPassword.getText().toString(),
				new LogInCallback() {
					public void done(ParseUser user, ParseException e) {
						if (user != null) {
							loginOk();
						} else {
							Toast.makeText(LoginActivity.this, "Incorect login or password", Toast.LENGTH_SHORT).show();
							// Signup failed. Look at the ParseException to see
							// what
							// happened.
						}
					}
				});
	}

	public void loginOk() {
		try {
			Toast.makeText(getApplicationContext(), "Logined", Toast.LENGTH_SHORT).show();

			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

			finish();

		} catch (Exception e) {
			// TODO: delete catch, fix error!
			Log.i("myLog", "" + e);
		}
	}
}
