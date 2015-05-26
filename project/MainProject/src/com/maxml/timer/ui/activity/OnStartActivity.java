//package com.maxml.timer.ui.activity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//
//public class OnStartActivity extends Activity {
//	
//	@SuppressWarnings("unused")
//	private DeviceHandler mHandler;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		// setContentView(R.layout.activity_main);
//		
//		mHandler = new DeviceHandler();
//		
//		Intent intent = new Intent(this, LoginActivity.class);
//		startActivity(intent);
//		finish();
//		
//	}
//	
//	class DeviceHandler extends Handler {
//		
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//		}
//		
//	}
//}
