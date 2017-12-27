package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxml.timer.R;
import com.maxml.timer.adapter.ActionsAdapter;
import com.maxml.timer.entity.Action;

import java.util.ArrayList;

public class ActionListViewFragment extends Fragment {

	private ActionsAdapter aAdapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.slice_listview, container, false);
		
		initView(rootView);

		return rootView;
	}

	private void initView(View rootView) {
		RecyclerView rvList = (RecyclerView) rootView.findViewById(R.id.rvList);

		aAdapter = new ActionsAdapter(new ArrayList<Action>());
		rvList.setAdapter(aAdapter);
	}


//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
///*
//
//		ReceiverService tableControler = new ReceiverService();
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
//		final ManagerAdapter adapter = new ManagerAdapter(getActivity(), R.layout.item_action, listSlice);
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
//						ReceiverService controler = new ReceiverService();
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
