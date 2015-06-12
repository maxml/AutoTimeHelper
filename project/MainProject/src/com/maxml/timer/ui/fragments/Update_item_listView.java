package com.maxml.timer.ui.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.beardedhen.androidbootstrap.BootstrapButton;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Update_item_listView extends Fragment {

	private TextView textDescription;
	BootstrapButton btnSave;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.update_item_listview, container, false);

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		textDescription = (TextView) getActivity().findViewById(R.id.description);
		textDescription.setText(Slice_ListViev.slice.getDescription());
		btnSave = (BootstrapButton) getActivity().findViewById(R.id.save);
		
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				TableController tableController = new TableController();
				Slice_ListViev.slice.setDescription(textDescription.getText()
						.toString());
				Slice_ListViev.slice.setUpdatedat(new Date());
				tableController.update(Slice_ListViev.slice);
				setupFragment(new Slice_ListViev());
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
