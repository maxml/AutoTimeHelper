package com.maxml.timer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.maxml.timer.controllers.TableController;
import com.maxml.timer.entity.actions.CallAction;
import com.maxml.timer.entity.Slice;

import java.util.Date;

public class CallReceiver extends BroadcastReceiver {
	private String phoneNumber = "";
	private TableController controller = new TableController();
	private CallAction call = new CallAction();

	private static int lastState = TelephonyManager.CALL_STATE_IDLE;
	private static boolean isIncoming = false;

	public void onReceive(Context context, Intent intent) {
		int state = 0;
		String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
		if (stateStr != null) {
			if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
				state = TelephonyManager.CALL_STATE_IDLE;
			} else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
				state = TelephonyManager.CALL_STATE_OFFHOOK;
			} else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				state = TelephonyManager.CALL_STATE_RINGING;
			}
		}
		onCallStateChanged(state, intent);
	}

	private void onCallStateChanged(int state, Intent intent) {
		if (lastState == state) {
			return;
		}
		switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				if (lastState == TelephonyManager.CALL_STATE_RINGING) {
					// ring but not pick up, MISSING CALL
				} else {
					if (isIncoming) {
						//INCOMING CALL ENDED
					} else {
						//ONGOING CALL ENDED
					}
					// end ol type of calling
					call.setEndDate(new Date());
					controller.addSlice(call);
				}
				break;

			case TelephonyManager.CALL_STATE_RINGING:
				// RINGING INCOMING CALL
				isIncoming = true;

				break;

			case TelephonyManager.CALL_STATE_OFFHOOK:
				if (lastState != TelephonyManager.CALL_STATE_RINGING) {
					// outgoing call answered
					isIncoming = false;
					// todo test
					phoneNumber = intent.getExtras().getString("android.intent.extra.phone_number");
					call.setStartDate(new Date());
					call.setDescription(CallType.OUTGOING.name());
				} else {
					// incoming call answered
					isIncoming = true;
					phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
					call.setStartDate(new Date());
					call.setDescription(CallType.INCOMING.name());
				}
				break;
		}
		lastState = state;
	}

	public enum CallType{
		INCOMING, OUTGOING
	}

}
