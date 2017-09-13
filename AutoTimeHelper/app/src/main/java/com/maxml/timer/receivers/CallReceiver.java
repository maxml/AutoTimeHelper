package com.maxml.timer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.maxml.timer.controllers.TableController;
import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.util.SliceType;

import java.util.Date;

public class CallReceiver extends BroadcastReceiver {
	private String phoneNumber = "";
	private TableController controller = new TableController();
	private Point point = new Point(1, 5);
	private Line line = new Line(point, point, "sada");
	private Slice call = new Slice("123", line, new Date(), new Date(),
			"ololo", SliceType.CALL);

	private static int lastState = TelephonyManager.CALL_STATE_IDLE;
	private static boolean isIncoming = false;

	public void onReceive(Context context, Intent intent) {

//		// TODO: get out three inner cycles
//		if (intent.getAction()
//				.equals("android.intent.action.NEW_OUTGOING_CALL")) {
//			// We obtain a reference number
//			phoneNumber = intent.getExtras().getString(
//					"android.intent.extra.PHONE_NUMBER");
//		} else if (intent.getAction().equals(
//				"android.intent.action.PHONE_STATE")) {
//			String phone_state = intent
//					.getStringExtra(TelephonyManager.EXTRA_STATE);
//			if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//				// phone calls, receive incoming number
//				phoneNumber = intent
//						.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//				Log.d("myLog", phoneNumber);
//			} else if (phone_state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
//				// the phone is in call mode (dial / call)
//				call.setStartDate(new Date());
//			//	controller.addOneSlise(call);
//
//
//			} else if (phone_state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//				// the phone is in standby mode. This event occurs at the
//				// end of the conversation when we already
//				// know the number and the fact that the call
//				call.setEndDate(new Date());
//
//
//			//	controller.addOneSlise(call);
//
//				/*
//				 * Log.d("myLog", controller .getTable() .getList()
//				 * .get(controller.getTable().getList().size() - 1)
//				 * .getEndDate().toString());
//				 */
//			}
//		}
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
