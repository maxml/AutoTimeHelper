package com.maxml.timer.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.maxml.timer.R;
import com.maxml.timer.util.DialogFactory;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateUserActivity extends Activity {

	private TextView entLogin;
	private TextView entPassword;
	private TextView entRPassword;
	private TextView entEmail;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_user);

		entLogin = (TextView) findViewById(R.id.textCreateLogin);
		entPassword = (TextView) findViewById(R.id.textCreatePassword);
		entRPassword = (TextView) findViewById(R.id.textCreateRepeatPassword);
		entEmail = (TextView) findViewById(R.id.textCreateEmail);

	}

	public void createUser() {
		// TODO: no ParseUser, only our entity user
		ParseUser user = new ParseUser();
		user.setUsername(entLogin.getText().toString());
		user.setPassword(entPassword.getText().toString());
		user.setEmail(entEmail.getText().toString());

		// other fields can be set just like with ParseObject
		// TODO: move in UserAPI
		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(CreateUserActivity.this, "User " + entLogin.getText().toString() + " created!",
							Toast.LENGTH_SHORT).show();

					Intent intent = new Intent(CreateUserActivity.this, LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);

				} else {
					DialogFactory.showErrorDialog(CreateUserActivity.this, "Error", e.getLocalizedMessage());
				}
				// TODO: all string in resources!
			}
		});
	}

	public void onClick(View v) {
		if (v.getId() == R.id.btnCreate) {
			// TODO: all string in resources!
			// TODO: fix three inner if
			// TODO: local string with values
			if (!entLogin.getText().toString().equals("") && !entPassword.getText().toString().equals("")
					&& !entEmail.getText().toString().equals("")) {
				if (entPassword.getText().toString().equals(entRPassword.getText().toString())) {
					createUser();

					Intent intent = new Intent(this, LoginActivity.class);
					startActivity(intent);

					Toast.makeText(getApplicationContext(), "User " + entLogin.getText().toString() + " created!",
							Toast.LENGTH_SHORT).show();

					finish();
				} else {
					Toast.makeText(getApplicationContext(), "Use different passwords", Toast.LENGTH_SHORT).show();
				}
			} else { // login or password or @ !=0
				Toast.makeText(getApplicationContext(), "Enter data", Toast.LENGTH_SHORT).show();
			}

		}
	}
}