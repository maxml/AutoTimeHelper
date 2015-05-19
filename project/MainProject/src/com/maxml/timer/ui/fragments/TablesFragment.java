package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.maxml.timer.R;

public class TablesFragment extends FragmentActivity {
	// When requested, this adapter returns a DemoObjectFragment,
	// representing an object in the collection.
	CollectionPagerAdapter mDemoCollectionPagerAdapter;
	ViewPager mViewPager;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tables);
		
		// ViewPager and its adapters use support library
		// fragments, so use getSupportFragmentManager.
		Log.d("Tag", "support managet :" + getSupportFragmentManager());
		
		mDemoCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mDemoCollectionPagerAdapter);
		mViewPager.setOffscreenPageLimit(1);
		
	}
	
}
