package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxml.timer.R;

public class TablesFragment extends Fragment {
	// When requested, this adapter returns a ObjectFragment,
	// representing an object in the collection.
	CollectionPagerAdapter CollectionPagerAdapter;
	ViewPager mViewPager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_tables, container, false);
		
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// ViewPager and its adapters use support library
		// fragments, so use getSupportFragmentManager.
		// Log.d("Tag", "support manager :" + getSupportFragmentManager());
		
		CollectionPagerAdapter = new CollectionPagerAdapter(getActivity());
		mViewPager = (ViewPager) getActivity().findViewById(R.id.pager);
		mViewPager.setAdapter(CollectionPagerAdapter);
		// mViewPager.setOffscreenPageLimit(1);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
}
