package com.maxml.timer.api;

import java.util.ArrayList;
import java.util.Date;

import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Table;
import com.maxml.timer.entity.Slice.SliceType;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class CallReceiver extends BroadcastReceiver {
	String phoneNumber = "";
	private ArrayList<Slice> list = new ArrayList<Slice>();
	private Table table = new Table(list);

	public void onReceive(Context context, Intent intent) {
		if (intent.getAction()
				.equals("android.intent.action.NEW_OUTGOING_CALL")) {
			// We obtain a reference number
			phoneNumber = intent.getExtras().getString(
					"android.intent.extra.PHONE_NUMBER");
		} else if (intent.getAction().equals(
				"android.intent.action.PHONE_STATE")) {
			String phone_state = intent
					.getStringExtra(TelephonyManager.EXTRA_STATE);
			if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				// phone calls, receive incoming number
				phoneNumber = intent
						.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			} else if (phone_state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
				// the phone is in call mode (dial / call)
				Slice start = new Slice();
				start.setStartDate(new Date());
				start.setType(SliceType.CALL);
				table.addSlise(start);
			} else if (phone_state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
				// the phone is in standby mode. This event occurs at the end of the conversation when we already 
				// know the number and the fact that the call
				Slice end = new Slice();
				end.setEndDate(new Date());
				end.setType(SliceType.CALL);
				table.addSlise(end);
			}
		}
	}

}