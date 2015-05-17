package com.example.listviewtimehelp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import entity.Slice;




/**
 * Created by Lantar on 22.04.2015.
 */
public class MenegerAdapter extends ArrayAdapter {

    private Context context;

    public MenegerAdapter(Context context, int resource, List<Slice> list) {
        super(context, resource, list);
        this.context=context;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        String type="";
        Slice slice = (Slice) getItem(position);



//        if(convertView == null){
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            switch (slice.getType()){
                case WORK:
                    convertView = mInflater.inflate(R.layout.slice_grey,null);
                    type = "WORK";
                    break;
                case REST:
                    convertView = mInflater.inflate(R.layout.slice_green,null);
                    type = "REST";
                    break;
                case CALL:
                    convertView = mInflater.inflate(R.layout.slice_yellow,null);
                    type = "CALL";
                    break;
                case WALK:
                    convertView = mInflater.inflate(R.layout.slice_blue,null);
                    type = "WALK";
                    break;

            }

            holder = new ViewHolder();
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.description = (TextView) convertView.findViewById(R.id.descriprion);
            holder.time = (TextView) convertView.findViewById(R.id.time);

            convertView.setTag(holder);

//        } else
            holder = (ViewHolder) convertView.getTag();

         int time = (int) ( slice.getEndDate().getTime()-slice.getStartDate().getTime());
         holder.type.setText(type);
         holder.description.setText(slice.getDescription());
         holder.time.setText(""+time);


        return convertView;
    }
}

class ViewHolder{

    TextView type;
    TextView description;
    TextView time;



}