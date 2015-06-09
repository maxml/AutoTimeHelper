package com.maxml.timer.ui.fragments;

import java.util.ArrayList;
import java.util.Date;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.maxml.timer.R;
import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Slice.SliceType;
import com.maxml.timer.util.MenegerAdapter;

public class Listview_fragment extends Fragment{

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.listview_fragment, container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
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

		
	}

}
