package com.maxml.timer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.maxml.timer.activity.authorization.LoginActivity;
import com.parse.Parse;
import com.parse.ParseInstallation;

public class OnStart extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);

		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
}
