package com.demo.databasedemo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Login extends ActionBarActivity implements OnClickListener {

	private Button btnReg;
	private Button btnLogin;
	private EditText etNick;
	private EditText etEmail;
	public Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginmain);
		Parse.initialize(this, "WznVOUle48rO59oeYjf0e4zuVf7m7DESc5F5G1W6",
				"PnuKImXNUNK6yFEXgj4r9bjcw6c0hVBo8aMLiFgk");

		etEmail = (EditText) findViewById(R.id.etEmail);
		etNick = (EditText) findViewById(R.id.etNick);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);

		btnReg = (Button) findViewById(R.id.btnReg);
		btnReg.setOnClickListener(this);

		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				Toast.makeText(Login.this, "Please wait", Toast.LENGTH_SHORT)
						.show();
				login();
			}
		};
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			check();
			break;

		case R.id.btnReg:
			Intent intent = new Intent(this, Registration.class);
			startActivity(intent);
			this.finish();
			break;

		default:
			break;
		}

	}

	private void check() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Exsmple");
		query.whereEqualTo("name", etNick.getText().toString());
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> scoreList, ParseException e) {
				if (e == null) {
					if (scoreList.size() > 0) {
						handler.sendEmptyMessage(0);
					} else {
						Toast.makeText(Login.this, "Hellow illegal",
								Toast.LENGTH_SHORT).show();
					}
					Log.d(AppConstants.MY_TAG, "Retrieved " + scoreList.size()
							+ " " + scoreList.toString() + " scores");
				} else {
					Log.d(AppConstants.MY_TAG, "Error: " + e.getMessage());
				}
			}
		});
	}

	private void login() {
		Intent intent = new Intent(this, AfterAll.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loginmain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
