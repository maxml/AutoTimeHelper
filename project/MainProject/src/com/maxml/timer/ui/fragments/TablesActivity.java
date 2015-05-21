package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.maxml.timer.R;

public class TablesActivity extends FragmentActivity {
	// When requested, this adapter returns a ObjectFragment,
	// representing an object in the collection.
	CollectionPagerAdapter CollectionPagerAdapter;
	ViewPager mViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tables);
		
		// ViewPager and its adapters use support library
		// fragments, so use getSupportFragmentManager.
		// Log.d("Tag", "support manager :" + getSupportFragmentManager());
		
		CollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(CollectionPagerAdapter);
		mViewPager.setOffscreenPageLimit(1);
	}
	
}
