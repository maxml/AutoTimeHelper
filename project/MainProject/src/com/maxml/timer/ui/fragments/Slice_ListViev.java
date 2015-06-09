package com.maxml.timer.ui.fragments;

import java.util.ArrayList;
import java.util.Date;

import com.maxml.timer.R;
import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Slice.SliceType;
import com.maxml.timer.util.MenegerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class Slice_ListViev extends Fragment {

	TextView titleText;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.slice_listview, container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ListView listMain = (ListView) getActivity()
				.findViewById(R.id.listView);

		final ArrayList<Slice> list = new ArrayList<Slice>();
		Point point = new Point(999, 999);
		Line line = new Line(point, point);
		for (int i = 0; i < 5; i++) {

			Slice slice = new Slice("User", line, new Date(), new Date(),
					"desc" + i, SliceType.WALK);
			list.add(slice);
		}
		for (int i = 0; i < 5; i++) {
			Slice slice = new Slice("User", line, new Date(), new Date(),
					"desc" + i, SliceType.REST);
			list.add(slice);
		}
		for (int i = 0; i < 5; i++) {
			Slice slice = new Slice("User", line, new Date(), new Date(),
					"desc" + i, SliceType.WORK);
			list.add(slice);

		}

		for (int i = 0; i < 5; i++) {
			Slice slice = new Slice("User", line, new Date(), new Date(),
					"desc" + i, SliceType.CALL);
			list.add(slice);

		}

		final MenegerAdapter adapter = new MenegerAdapter(getActivity(),
				R.layout.item_list, list);

		listMain.setAdapter(adapter);
		int durationWork = 0;
		int durationCall = 0;
		int durationWalk = 0;
		int durationRest = 0;
		for (Slice slice : list) {
			if (slice.getType().equals(SliceType.WORK))
				durationWork += slice.getStartDate().getTime()
						- slice.getEndDate().getTime();
			if (slice.getType().equals(SliceType.REST))
				durationWork += slice.getStartDate().getTime()
						- slice.getEndDate().getTime();
			if (slice.getType().equals(SliceType.CALL))
				durationWork += slice.getStartDate().getTime()
						- slice.getEndDate().getTime();
			if (slice.getType().equals(SliceType.WALK))
				durationWork += slice.getStartDate().getTime()
						- slice.getEndDate().getTime();
		}

		titleText = (TextView) getActivity().findViewById(R.id.titleText);
		titleText.setText("Work: " + durationWork + "Call: " + durationCall
				+ " Walk: " + durationWalk + " Rest: " + durationRest);
	}

}
