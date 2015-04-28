package com.maxml.timer;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Data;
import android.util.Log;

import com.maxml.timer.activity.authorization.LoginActivity;
import com.maxml.timer.api.PointCRUD;
import com.maxml.timer.api.SliceCRUD;
import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Slice.SliceType;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

public class OnStart extends Activity {
	
	private DeviceHandler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		Parse.enableLocalDatastore(this);

		Parse.initialize(this, "v7xyeLy701EXeS70aggBiMDzRIlTH0q1tiYlZqFV",
				"oxzucbjTHAKP20t9o4UjBCRBoKN0gk4jL4Mm9UjK");
		ParseInstallation.getCurrentInstallation().saveInBackground();

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
