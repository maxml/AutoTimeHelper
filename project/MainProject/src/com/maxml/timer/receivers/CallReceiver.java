package com.maxml.timer.receivers;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.maxml.timer.controllers.TableController;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Slice.SliceType;

public class CallReceiver extends BroadcastReceiver {
	private String phoneNumber = "";
	private TableController controller = new TableController();
	// TODO: why?
	private Slice call = new Slice();

	public void onReceive(Context context, Intent intent) {
		//TODO: get out three inner cycles
		if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
			// We obtain a reference number
			phoneNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
		} else if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
			String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
			if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				// phone calls, receive incoming number
				phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

				Log.d("myLog", phoneNumber);

			} else if (phone_state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
				// the phone is in call mode (dial / call)
				if (controller.getTable().getList().isEmpty()) {
					call.setStartDate(new Date());
					call.setType(SliceType.CALL);
					controller.getTable().addSlise(call);

					/*
					 * Log.d("myLog", controller .getTable() .getList()
					 * .get(controller.getTable().getList().size() - 1)
					 * .getStartDate().toString());
					 */

				} else if (phone_state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
					// the phone is in standby mode. This event occurs at the
					// end of the conversation when we already
					// know the number and the fact that the call
					call.setEndDate(new Date());
					controller.getTable().addSlise(call);

					/*
					 * Log.d("myLog", controller .getTable() .getList()
					 * .get(controller.getTable().getList().size() - 1)
					 * .getEndDate().toString());
					 */
				}
			}
		}

	}
}