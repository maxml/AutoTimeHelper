package com.maxml.timer.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import com.maxml.timer.controllers.ControllerGoogleMap;
import com.maxml.timer.controllers.TableController;
import com.maxml.timer.entity.Table;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

//Since this is an object collection, use a FragmentStatePagerAdapter,
//and NOT a FragmentPagerAdapter.
public class CollectionPagerAdapter extends FragmentPagerAdapter {
	
	private static final int AMOUNT_OF_WEEK_DAYS = 3;
	
	// private TableController c = new TableController();
	
	public CollectionPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
	public Fragment getItem(int position) {
		List<String> list = new ArrayList<String>();
		// c.getTable().getList();
		list.add("discriotion 1");
		list.add("discriotion 2");
		list.add("discriotion 3");
		Fragment fragment = ObjectFragment.newInstance(list, position);
		return fragment;
	}
	
	@Override
	public int getCount() {
		return AMOUNT_OF_WEEK_DAYS;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return "OBJECT " + (position + 1);
	}
}
