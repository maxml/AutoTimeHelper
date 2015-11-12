package com.demo.databasedemo;

import java.util.ArrayList;

import com.parse.Parse;
import com.parse.ParseObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Registration extends ActionBarActivity implements OnClickListener {

	private static final String Key = "Saved";
	private Button btnActivityReg;
	private EditText etNick;
	private EditText etEmail;
	private EditText etPhoneNumber;
	public static ArrayList<String> logins = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(AppConstants.MY_TAG, "start onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);

		Parse.initialize(this, "WznVOUle48rO59oeYjf0e4zuVf7m7DESc5F5G1W6",
				"PnuKImXNUNK6yFEXgj4r9bjcw6c0hVBo8aMLiFgk");

		etEmail = (EditText) findViewById(R.id.etEmail2);
		etNick = (EditText) findViewById(R.id.etNick2);
		etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);

		btnActivityReg = (Button) findViewById(R.id.btnActivityReg);
		btnActivityReg.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnActivityReg:
			registration();
			this.finish();
			break;

		default:
			break;
		}
	}

	private void registration() {
		ParseObject gameScore = new ParseObject("Exsmple");
		gameScore.put("name", etNick.getText().toString());
		gameScore.put("email", etEmail.getText().toString());
		gameScore.put("phone", etPhoneNumber.getText().toString());
		gameScore.saveInBackground();
		Log.e(AppConstants.MY_TAG, "start method registration");
		Intent intent = new Intent(this, Login.class);
		startActivity(intent);
		etEmail.setText("");
		etNick.setText("");
		etPhoneNumber.setText("");

	}
}
