package com.maxml.timer.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.maxml.timer.R;

//Since this is an object collection, use a FragmentStatePagerAdapter,
//and NOT a FragmentPagerAdapter.
public class CollectionPagerAdapter extends PagerAdapter {
	
	private static final int AMOUNT_OF_WEEK_DAYS = 3;
	
	// private TableController c = new TableController();
	
	private Context ctx;
	
	public CollectionPagerAdapter(Context ctx) {
		this.ctx = ctx;
	}
	
	@Override
	public Object instantiateItem(ViewGroup collection, int position) {
		View view = LayoutInflater.from(ctx).inflate(R.layout.fragment_tables, collection, false);
		ListView lv = (ListView) view.findViewById(R.id.list);
		List<String> lst = new ArrayList<String>();
		lst.add("Here");
		lst.add("Will");
		lst.add("Be");
		lst.add("Our");
		lst.add("Tables");
		lst.add("this page can swipe ->");
		
		SmallAdapter sm = new SmallAdapter(collection.getContext(), lst);
		lv.setAdapter(sm);
		collection.addView(view);
		return view;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	@Override
	public Parcelable saveState() {
		return null;
	}
	
	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}
	
	@Override
	public void startUpdate(View arg0) {
	}
	
	@Override
	public void finishUpdate(View arg0) {
	}
	
	@Override
	public int getCount() {
		return AMOUNT_OF_WEEK_DAYS;
	}
	
	public class SmallAdapter extends ArrayAdapter<String> {
		
		public SmallAdapter(Context context, List<String> lst) {
			super(context, 0, lst);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Get the data item for this position
			String str = getItem(position);
			// Check if an existing view is being reused, otherwise inflate the view
			
			if (convertView == null) {
				// Log.d(LOG_TAG, "convert view does not null");
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
			}
			// Lookup view for data population
			TextView tvName = (TextView) convertView.findViewById(R.id.tvM);
			// Populate the data into the template view using the data object
			tvName.setText(str);
			// Log.d(LOG_TAG, "position = " + str);
			// Return the completed view to render on screen
			return convertView;
		}
	}
	
}
