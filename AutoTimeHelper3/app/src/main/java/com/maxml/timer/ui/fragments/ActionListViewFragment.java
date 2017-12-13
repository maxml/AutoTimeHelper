package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxml.timer.R;

public class ActionListViewFragment extends Fragment {
	
	TextView titleText;
	ProgressBar progressBar;
	String LOG_TAG = "my_Log";

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.slice_listview, container, false);
		
		view.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					// do something
				}
				return true;
			}
		});
		return inflater.inflate(R.layout.slice_listview, container, false);
	}
	
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
///*
//
//		GeneralService tableControler = new GeneralService();
//		tableControler.onResult = this;
//		tableControler.getListSlice();
//
//		progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
//		progressBar.setVisibility(ProgressBar.VISIBLE);
//*/
//
//	}
//
//	@Override
//	public void OnResultSlices(final List<Slice> listSlice) {
//
//		progressBar.setVisibility(ProgressBar.INVISIBLE);
//
//		final ListView listMain = (ListView) getActivity().findViewById(R.id.listView);
//
//		final ManagerAdapter adapter = new ManagerAdapter(getActivity(), R.layout.item_list, listSlice);
//
//		LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplication()
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		final View popupView = layoutInflater.inflate(R.layout.popup, null);
//		final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT);
//
//		for (int j = 0; j < listSlice.size(); j++) {
//			Log.d("Slice_List", "slice " + j + " - " + listSlice.get(j).getDescription());
//		}
//
//		SimpleDateFormat dateFormat = null;
//		dateFormat = new SimpleDateFormat("k:m");
//
//		listMain.setAdapter(adapter);
//		long durationWork = 0;
//		long durationCall = 0;
//		long durationWalk = 0;
//		long durationRest = 0;
//
//		for (Slice slice : listSlice) {
//			if (slice.getType().equals(SliceType.WORK))
//				durationWork = slice.getEndDate().getTime() - slice.getStartDate().getTime();
//			if (slice.getType().equals(SliceType.REST))
//				durationRest = slice.getEndDate().getTime() - slice.getStartDate().getTime();
//			if (slice.getType().equals(SliceType.CALL))
//				durationCall = slice.getEndDate().getTime() - slice.getStartDate().getTime();
//			if (slice.getType().equals(SliceType.WALK))
//				durationWalk = slice.getEndDate().getTime() - slice.getStartDate().getTime();
//		}
//
//		titleText = (TextView) getActivity().findViewById(R.id.titleText);
//		titleText.setText("WorkAction: " + durationWork / 60 + "s, CallAction: " + durationCall / 60 + "s, Walk: "
//				+ durationWalk / 60 + "s, Rest: " + durationRest / 60 + "s");
//
//		listMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//		int position = 0;
//		listMain.setItemChecked(position, true);
//
//		listMain.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
//				Log.d(LOG_TAG, "itemClick: position = " + position + ", id = " + id);
//
//				if (popupWindow.isShowing()) {
//					popupWindow.dismiss();
//				}
//
//				Button btnDelete = (Button) popupView.findViewById(R.id.btndelete);
//				Button btnUpgrade = (Button) popupView.findViewById(R.id.btnupgrade);
//				Button btnDismiss = (Button) popupView.findViewById(R.id.btnDismiss);
//
//				btnDismiss.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						popupWindow.dismiss();
//					}
//				});
//
//				btnDelete.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						GeneralService controler = new GeneralService();
//						controler.delete(listSlice.get(position));
//						listSlice.remove(position);
//						Toast.makeText(getActivity(),
//								"You time is DELETED   description :" + listSlice.get(position).getDescription(),
//								Toast.LENGTH_SHORT).show();
//						OnResultSlices(listSlice);
//						popupWindow.dismiss();
//					}
//				});
//
//				btnUpgrade.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						MainActivity mainActivity = new MainActivity();
//						setupFragment(new Update_item_listView());
//						slice = listSlice.get(position);
//						popupWindow.dismiss();
//					}
//				});
//				popupWindow.showAsDropDown(view);
//			}
//		});
//
//	}
//
//	private static final String FRAGMENT_TAG = "CURRENT_FRAGMENT";
//
//	public void setupFragment(Fragment fragment) {
//		FragmentManager fragmentManager = getFragmentManager();
//		Fragment currentFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
//		if (currentFragment == null || !currentFragment.getClass().equals(fragment.getClass())) {
//			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, FRAGMENT_TAG)
//					.commit();
//		}
//	}
}
