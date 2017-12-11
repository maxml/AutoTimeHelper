package com.maxml.timer.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.maxml.timer.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Lantar on 02.04.2015.
 * modified by Igor Shaula
 */
public class ManagerAdapter extends ArrayAdapter {

    public static final String DURATION = "Duration: ";
    public static final String START_DATE = "Start date: ";
    public static final String END_DATE = "End date: ";

    private int colorForWalk, colorForCall, colorForRest, colorForWork;

    private Context context;

    public ManagerAdapter(Context context, int resource, List<Object> items) {
        super(context, resource, items);

        colorForWalk = ContextCompat.getColor(context, R.color.color1);
        colorForCall = ContextCompat.getColor(context, R.color.color2);
        colorForRest = ContextCompat.getColor(context, R.color.color3);
        colorForWork = ContextCompat.getColor(context, R.color.color4);

        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
//        Slice essence = (Slice) getItem(position);
//
//        if (convertView == null) {
//            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//
////            convertView = mInflater.inflate(R.itemRootLayout.item_list, parent); // UnsupportedOperationException: addView(View, LayoutParams) is not supported in AdapterView
//            convertView = mInflater.inflate(R.layout.item_list, null);
//            // TODO: 28.02.2016 how to avoid null above or make studio agree with it ?
//
//            holder = new ViewHolder();
//            holder.duration = (TextView) convertView.findViewById(R.id.duration);
//            holder.startDate = (TextView) convertView.findViewById(R.id.textViewStartDate);
//            holder.endDate = (TextView) convertView.findViewById(R.id.textViewEndDate);
//            holder.description = (TextView) convertView.findViewById(R.id.description);
//            holder.itemRootLayout = convertView.findViewById(R.id.itemRootLayout);
//
//            // saving convertView inside the holder \
//            convertView.setTag(holder);
//
//        } else
//            holder = (ViewHolder) convertView.getTag();
//
//        long duration = essence.getEndDate().getTime()
//                - essence.getStartDate().getTime();
//        holder.duration.setText(String.valueOf(DURATION + (duration / 6000)));
//
////        Date currentDate = new Date();
//        SimpleDateFormat dateFormat;
//        dateFormat = new SimpleDateFormat("k:m", Locale.getDefault());
//
//        holder.startDate.setText(String.valueOf(START_DATE + dateFormat.format(essence.getStartDate())));
//        holder.endDate.setText(String.valueOf(END_DATE + dateFormat.format(essence.getEndDate())));
//        holder.description.setText(essence.getDescription());
//        holder.itemRootLayout = convertView.findViewById(R.id.itemRootLayout);
//
//        if (essence.getType().equals(SliceType.WALK))
//            holder.itemRootLayout.setBackgroundColor(colorForWalk);
//        else if (essence.getType().equals(SliceType.CALL))
////        if (essence.getType().equals(Slice.SliceType.CALL))
//            holder.itemRootLayout.setBackgroundColor(colorForCall);
//        else if (essence.getType().equals(SliceType.REST))
////        if (essence.getType().equals(Slice.SliceType.REST))
//            holder.itemRootLayout.setBackgroundColor(colorForRest);
//        else if (essence.getType().equals(SliceType.WORK))
////        if (essence.getType().equals(Slice.SliceType.WORK))
//            holder.itemRootLayout.setBackgroundColor(colorForWork);
//        // TODO: 28.02.2016 is it possible to convert this ladder of if-else to switch ?
//
        return convertView;
    }

    public static class ViewHolder {
        // ImageView imageView;
        TextView duration;
        TextView startDate;
        TextView endDate;
        TextView description;
        View itemRootLayout;
    }
}