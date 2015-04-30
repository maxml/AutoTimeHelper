package com.maxml.timer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.maxml.timer.activity.authorization.LoginActivity;

public class OnStart extends Activity {

	private DeviceHandler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
<<<<<<< HEAD

		/*Parse.initialize(this, "v7xyeLy701EXeS70aggBiMDzRIlTH0q1tiYlZqFV",
				"oxzucbjTHAKP20t9o4UjBCRBoKN0gk4jL4Mm9UjK");
		ParseInstallation.getCurrentInstallation().saveInBackground();
		Parse.enableLocalDatastore(this);*/
=======
>>>>>>> 872c8cbaee1aa9d3e3490b3722889c485b4589e5

		mHandler = new DeviceHandler();

		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();

	}

	class DeviceHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}

	}
}
