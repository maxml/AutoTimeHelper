package com.maxml.timer.ui.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.maxml.timer.MainActivity;
import com.maxml.timer.R;
import com.maxml.timer.api.interfaces.OnResultList;
import com.maxml.timer.controllers.TableController;
import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Slice.SliceType;
import com.maxml.timer.util.MenegerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Slice_ListViev extends Fragment implements OnResultList {

	TextView titleText;
	ProgressBar progressBar;
	String LOG_TAG = "my_Log";
	public static Slice slice;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.slice_listview, container, false);

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TableController tableControler = new TableController();
		tableControler.onResult = this;
		tableControler.getListSlice();

		progressBar = (ProgressBar) getActivity()
				.findViewById(R.id.progressBar);
		progressBar.setVisibility(ProgressBar.VISIBLE);

	}

	@Override
	public void OnResultSlices(final List<Slice> listSlice) {
		// TODO Auto-generated method stub

		progressBar.setVisibility(ProgressBar.INVISIBLE);

		final ListView listMain = (ListView) getActivity().findViewById(
				R.id.listView);

		final MenegerAdapter adapter = new MenegerAdapter(getActivity(),
				R.layout.item_list, listSlice);

		SimpleDateFormat dateFormat = null;
		dateFormat = new SimpleDateFormat("k:m");

		listMain.setAdapter(adapter);
		long durationWork = 0;
		long durationCall = 0;
		long durationWalk = 0;
		long durationRest = 0;

		for (Slice slice : listSlice) {
			if (slice.getType().equals(SliceType.WORK))
				durationWork = slice.getEndDate().getTime()
						- slice.getStartDate().getTime();
			if (slice.getType().equals(SliceType.REST))
				durationWork = slice.getEndDate().getTime()
						- slice.getStartDate().getTime();
			if (slice.getType().equals(SliceType.CALL))
				durationWork = slice.getEndDate().getTime()
						- slice.getStartDate().getTime();
			if (slice.getType().equals(SliceType.WALK))
				durationWork = slice.getEndDate().getTime()
						- slice.getStartDate().getTime();
		}

		titleText = (TextView) getActivity().findViewById(R.id.titleText);
		titleText.setText("Work: " + durationWork / 60 + "s, Call: "
				+ durationCall / 60 + "s, Walk: " + durationWalk / 60
				+ "s, Rest: " + durationRest / 60 + "s");

		listMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		int position = 0;
		listMain.setItemChecked(position, true);

		listMain.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, final long id) {
				Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
						+ id);

				PopupMenu popup = new PopupMenu(getActivity().getApplication(),
						listMain);
				// Inflating the Popup using xml file
				popup.getMenuInflater().inflate(R.menu.popup_menu,
						popup.getMenu());

				// registering popup with OnMenuItemClickListener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {

						switch ("" + item.getTitle()) {
						case "Update description":
							MainActivity mainActivity = new MainActivity();
							setupFragment(new Update_item_listView());
							slice = listSlice.get(position);

							break;
						case "Delete":
							TableController controler = new TableController();
							controler.delete(listSlice.get(position));
							listSlice.remove(position);
							Toast.makeText(
									getActivity(),
									"You time is DELETED   description :"
											+ listSlice.get(position)
													.getDescription(),
									Toast.LENGTH_SHORT).show();
							OnResultSlices(listSlice);
							break;
						}

						return true;
					}
				});

				popup.show();// showing popup menu

			}
		});

		listMain.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(LOG_TAG, "itemSelect: position = " + position + ", id = "
						+ id);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				Log.d(LOG_TAG, "itemSelect: nothing");
			}
		});

	}

	private static final String FRAGMENT_TAG = "CURRENT_FRAGMENT";

	public void setupFragment(Fragment fragment) {
		FragmentManager fragmentManager = getFragmentManager();
		Fragment currentFragment = fragmentManager
				.findFragmentByTag(FRAGMENT_TAG);
		if (currentFragment == null
				|| !currentFragment.getClass().equals(fragment.getClass())) {
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment, FRAGMENT_TAG)
					.commit();
		}
	}
}
