package com.maxml.timer.ui.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Slice_ListViev extends Fragment implements OnResultList {

	TextView titleText;
	ProgressBar progressBar;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.slice_listview, container, false);
		
		
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TableController tableControler = new TableController();
		tableControler.onResult = this;
		tableControler.getListSlice();
		
		progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
	}

	@Override
	public void OnResultSlices(List<Slice> listSlice) {
		// TODO Auto-generated method stub
		
		progressBar.setVisibility(ProgressBar.INVISIBLE);
		
		ListView listMain = (ListView) getActivity()
				.findViewById(R.id.listView);

		

		final MenegerAdapter adapter = new MenegerAdapter(getActivity(),
				R.layout.item_list, listSlice);

		SimpleDateFormat dateFormat = null;
		dateFormat = new SimpleDateFormat("k:m");
		
		listMain.setAdapter(adapter);
		long  durationWork = 0;
		long  durationCall = 0;
		long  durationWalk = 0;
		long  durationRest = 0;
		
		
		for  (Slice slice : listSlice) {
			if (slice.getType().equals(SliceType.WORK))
				durationWork = slice.getEndDate().getTime() - slice.getStartDate().getTime();
			if (slice.getType().equals(SliceType.REST))
				durationWork = slice.getEndDate().getTime() - slice.getStartDate().getTime() ;
			if (slice.getType().equals(SliceType.CALL))
				durationWork = slice.getEndDate().getTime() - slice.getStartDate().getTime();
			if (slice.getType().equals(SliceType.WALK))
				durationWork = slice.getEndDate().getTime() - slice.getStartDate().getTime();
		}
		
		
		

		titleText = (TextView) getActivity().findViewById(R.id.titleText);
		titleText.setText("Work: " + durationWork/60 + " m, Call: " + durationCall/60
				+ " m, Walk: " + durationWalk/60 + " m, Rest: " + durationRest/60 + " m");
	}

}
