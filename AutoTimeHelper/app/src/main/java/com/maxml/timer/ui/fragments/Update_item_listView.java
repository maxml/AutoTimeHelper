package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.maxml.timer.R;
import com.maxml.timer.controllers.TableControllerService;

import java.util.Date;

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
		textDescription.setText(SliceListViewFragment.slice.getDescription());
		btnSave = (BootstrapButton) getActivity().findViewById(R.id.save);
		
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				TableControllerService tableControllerService = new TableControllerService();
				SliceListViewFragment.slice.setDescription(textDescription.getText()
						.toString());
				SliceListViewFragment.slice.setUpdateDate(new Date());
				tableControllerService.update(SliceListViewFragment.slice);
				setupFragment(new SliceListViewFragment());
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
