package com.maxml.timer.ui.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.maxml.timer.R;
import com.maxml.timer.controllers.TableController;
import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Slice.SliceType;
import com.parse.ParseUser;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ManualActivityFragment extends Fragment {
	private TableController controller = new TableController();
	private String SELECT_ACTION = "Select an action!";
	private Point point = new Point(1, 5);
	private Line line = new Line(point, point, "sada");
	private SliceType type;
	private Slice manualstart = new Slice(ParseUser.getCurrentUser()
			.getObjectId(), line, new Date(), new Date(), "", type);
	private Slice manualEnd = new Slice(ParseUser.getCurrentUser()
			.getObjectId(), line, new Date(), new Date(), "ololo", type);

	private TextView title;
	private ToggleButton butCall;
	private ToggleButton butWork;
	private ToggleButton butRest;
	private ToggleButton butWalk;
	
	private boolean btnIsPress = false;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.manual_activity_fragment, container,
				false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		butCall = (ToggleButton) getActivity().findViewById(R.id.butCall);
		butWork = (ToggleButton) getActivity().findViewById(R.id.butWork);
		butRest = (ToggleButton) getActivity().findViewById(R.id.butRest);
		butWalk = (ToggleButton) getActivity().findViewById(R.id.butWalk);

		title = (TextView) getActivity().findViewById(R.id.title);
		title.setText(SELECT_ACTION);

		butCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				title.setText(CharSequence());
				try {
					Log.d("my_Log","btn CALL press");
					buildButtonPendingIntentCopy(SliceType.CALL);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				butWork.setChecked(false);
				butWalk.setChecked(false);
				butRest.setChecked(false);

			}
		});

		butWork.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				title.setText(CharSequence());
				try {
					buildButtonPendingIntentCopy(SliceType.WORK);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				butCall.setChecked(false);
				butWalk.setChecked(false);
				butRest.setChecked(false);

			}
		});
		butRest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				title.setText(CharSequence());
				try {
					buildButtonPendingIntentCopy(SliceType.REST);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				butCall.setChecked(false);
				butWalk.setChecked(false);
				butWork.setChecked(false);

			}
		});
		butWalk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				title.setText(CharSequence());
				try {
					buildButtonPendingIntentCopy(SliceType.WALK);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				butCall.setChecked(false);
				butRest.setChecked(false);
				butWork.setChecked(false);

			}
		});

	}
	
	public void buildButtonPendingIntentCopy(SliceType type) throws InterruptedException{
	if (!btnIsPress) {
		Log.d("my_Log","metod start work first");
		manualstart.setStartDate(new Date());
			manualstart.setType(type);
			manualstart.setDescription(""+manualstart.getType());
			btnIsPress = true;
			
		} else {
			Log.d("my_Log","metod start work second");
			manualstart.setEndDate(new Date());
			ArrayList sliceList = new ArrayList();
			sliceList.add(manualstart);
			controller.addSlise(manualstart);
			btnIsPress = false;
			if(!manualstart.getType().equals(type))
				buildButtonPendingIntentCopy(type);
		}
	}

	public void buildButtonPendingIntent(SliceType type) throws InterruptedException {
		
		if (controller.getTable().getList().isEmpty()) {
			
			manualstart.setStartDate(new Date());
			manualstart.setEndDate(new Date());
			manualstart.setType(type);
		 //   controller.getTable().addSlise(manualstart);
		//	controller.addSlise(manualstart);
		//	controller.addOneSlise(manualstart);

			
		} else {
			ArrayList<Slice> slices = controller.getTable().getList();
			slices.get(slices.size() - 1).setEndDate(new Date());
			
		//	controller.ubdateSlise(manualstart);
			
			manualstart.setEndDate(new Date());
			manualEnd.setType(type);
			// slices.add(manualnext);
			
		//	controller.addOneSlise(manualEnd);
		//	controller.ubdateSlise(manualEnd);
		}

	}

	public String CharSequence() {
		SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
		String currentDateandTime = "Start at:" + sdf.format(new Date());
		return currentDateandTime;
	}
}

// public void onClick(View v) {
// switch (v.getId()) {
// case R.id.butCall:
// title.setText(CharSequence());
// buildButtonPendingIntent(SliceType.CALL);
// butWork.setChecked(false);
// butWalk.setChecked(false);
// butRest.setChecked(false);
// break;
// case R.id.butWork:
// title.setText(CharSequence());
// buildButtonPendingIntent(SliceType.WORK);
// butCall.setChecked(false);
// butWalk.setChecked(false);
// butRest.setChecked(false);
// break;
// case R.id.butRest:
// title.setText(CharSequence());
// buildButtonPendingIntent(SliceType.REST);
// butCall.setChecked(false);
// butWalk.setChecked(false);
// butWork.setChecked(false);
// break;
// case R.id.butWalk:
// title.setText(CharSequence());
// buildButtonPendingIntent(SliceType.WALK);
// butCall.setChecked(false);
// butRest.setChecked(false);
// butWork.setChecked(false);
// break;
// }
// }

// if (controller.getTable().getList().isEmpty()) {
// Slice manualstart = new Slice(ParseUser.getCurrentUser()
// .getObjectId(), line, new Date(), new Date(), "ololo", type);
// // manualstart.setStartDate(new Date());
// // manualstart.setType(type);
// // controller.getTable().addSlise(start);
//
// controller.addSlise(manualstart);
// }
//
// ArrayList<Slice> slices = controller.getTable().getList();
// slices.get(slices.size() - 1).setEndDate(new Date());
//
// Slice manualnext = new Slice(ParseUser.getCurrentUser().getObjectId(),
// line, new Date(), new Date(), "ololo", type);
// // manualnext.setStartDate(new Date());
// // manualnext.setType(type);
// // slices.add(manualnext);
// //controller.addSlise(manualnext);
// }
