package com.maxml.timer.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.*;

import com.maxml.timer.R;
import com.maxml.timer.R.color;
import com.maxml.timer.entity.Slice;

/**
 * Created by Lantar on 02.04.2015.
 */
public class MenegerAdapter extends ArrayAdapter {

	private Context context;

	public MenegerAdapter(Context context, int resource, List<Slice> items) {
		super(context, resource, items);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Slice essence = (Slice) getItem(position);

		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			convertView = mInflater.inflate(R.layout.item_list, null);

			holder = new ViewHolder();
			holder.duration = (TextView) convertView
					.findViewById(R.id.duration);
			holder.startDate = (TextView) convertView
					.findViewById(R.id.textViewStartDate);
			holder.endDate = (TextView) convertView
					.findViewById(R.id.textViewEndDate);
			holder.description = (TextView) convertView
					.findViewById(R.id.description);
			holder.layout = convertView.findViewById(R.id.layout);
			if (essence.getType().equals(Slice.SliceType.WALK))
				holder.layout.setBackgroundColor(Color.rgb(100, 100, 250));
			if (essence.getType().equals(Slice.SliceType.CALL))
				holder.layout.setBackgroundColor(Color.rgb(210, 225, 90));
			if (essence.getType().equals(Slice.SliceType.REST))
				holder.layout.setBackgroundColor(Color.rgb(50, 250, 100));
			if (essence.getType().equals(Slice.SliceType.WORK))
				holder.layout.setBackgroundColor(Color.rgb(250, 100, 25));
			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();
		long duration = essence.getEndDate().getTime()
				- essence.getStartDate().getTime();
		holder.duration.setText("  Duration: " + duration/6000);

		Date currentDate = new Date();
		SimpleDateFormat dateFormat = null;
		dateFormat = new SimpleDateFormat("k:m");

		holder.startDate.setText("Start date: "
				+ dateFormat.format(essence.getStartDate()));
		holder.endDate.setText("  End date: "
				+ dateFormat.format(essence.getEndDate()));
		holder.description.setText( essence.getDescription());
		holder.layout = convertView.findViewById(R.id.layout);
		if (essence.getType().equals(Slice.SliceType.WALK))
			holder.layout.setBackgroundColor(Color.rgb(100, 100, 250));
		if (essence.getType().equals(Slice.SliceType.CALL))
			holder.layout.setBackgroundColor(Color.rgb(210, 225, 90));
		if (essence.getType().equals(Slice.SliceType.REST))
			holder.layout.setBackgroundColor(Color.rgb(50, 250, 100));
		if (essence.getType().equals(Slice.SliceType.WORK))
			holder.layout.setBackgroundColor(Color.rgb(250, 100, 25));

		return convertView;
	}
}

class ViewHolder {
	// ImageView imageView;
	TextView duration;
	TextView startDate;
	TextView endDate;
	TextView description;
	View layout;
}
