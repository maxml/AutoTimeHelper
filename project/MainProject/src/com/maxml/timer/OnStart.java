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

		Parse.enableLocalDatastore(this);

		Parse.initialize(this, "v7xyeLy701EXeS70aggBiMDzRIlTH0q1tiYlZqFV",
				"oxzucbjTHAKP20t9o4UjBCRBoKN0gk4jL4Mm9UjK");
		ParseInstallation.getCurrentInstallation().saveInBackground();

		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
}
