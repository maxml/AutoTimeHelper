package com.maxml.timer.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.maxml.timer.R;
//Instances of this class are fragments representing a single
//object in our collection.

public class ObjectFragment extends Fragment {
	public static final String ARG_OBJECT = "object";
	private ListView lv;
	private List<String> lst = new ArrayList<String>();
	private static int c = 10000;
	
	public static ObjectFragment newInstance(List<String> list) {
		ObjectFragment myFragment = new ObjectFragment();
		
		Bundle args = new Bundle();
		args.putInt("Size", list.size());
		int counter = 0;
		for (String str : list) {
			args.putString("" + counter, str);
			counter++;
		}
		myFragment.setArguments(args);
		
		return myFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_tables, container, false);
		rootView.setId(c++);
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// lv = new ListView(getActivity());
		// ViewGroup.LayoutParams p = new
		// ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		// lv.setLayoutParams(p);
		
		SmallAdapter a = new SmallAdapter(getActivity(), lst);
		lv = (ListView) getActivity().findViewById(R.id.list);
		lv.setAdapter(a);
		
		// getActivity().addContentView(lv, lv.getLayoutParams());
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getArguments();
		int size = b.getInt("Size");
		for (int i = 0; i < size; i++) {
			String f = b.getString("" + i);
			lst.add(f);
		}
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
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
			}
			// Lookup view for data population
			TextView tvName = (TextView) convertView.findViewById(R.id.tvM);
			// Populate the data into the template view using the data object
			tvName.setText(str);
			// Return the completed view to render on screen
			return convertView;
		}
	}
}