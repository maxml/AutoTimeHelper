package com.maxml.timer.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.util.Log;

import com.maxml.timer.App;
import com.maxml.timer.api.interfaces.OnDbResult;
import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.util.NetworkStatus;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class LineCRUD implements OnDbResult {

	ParseObject point1;
	ParseObject point2;
	Point startPoint = null;
	LineCRUD lineCRUD = this;
	public OnDbResult onresultLine;
	

	private static final String USER = "User";
	private static final String START = "start";
	private static final String FINISH = "finish";
	private static final String LINE = "Line";
	private static final String POINT = "Point";
	private static final String X = "x";
	private static final String Y = "y";
	private static final String TG = "myboolshit";

	private String user = "nullUser";



	public void create(Slice slice) {
		Log.i("Line", " Line starting create");
		PointCRUD pointCRUD = new PointCRUD();
		pointCRUD.onresult = this;
		pointCRUD.create(slice.getPath().getStart(),slice.getPath().getFinish(), slice);
	}

	@Override
	public void onResult(ParseObject parsePointStart,ParseObject parsePointFinish, final Slice slice) {
			
			final ParseObject parseLine = new ParseObject("Line");
			parseLine.put("start", parsePointStart);
			parseLine.put("finish", parsePointFinish);
			parseLine.put("User", slice.getUser());
			parseLine.put("UUID", ""+UUID.randomUUID());
			parseLine.put("startUUID", parsePointFinish.getString("UUID"));
			parseLine.put("finishUUID", ""+parsePointFinish.getString("UUID"));
			if(NetworkStatus.isConnected){
			parseLine.saveInBackground(
			new SaveCallback() {
				@Override
				public void done(ParseException e) {
					Log.i("Line", "line is created " + parseLine.getObjectId());
					parseLine.pinInBackground();
					onresultLine.onResult(parseLine, slice);
				}
			}

			);
			
		}else{
			Log.i("Line", "line is created ofline UUID" + parseLine.getString("UUID"));
			parseLine.saveInBackground();
			parseLine.pinInBackground();
			onresultLine.onResult(parseLine, slice);
		}
	}



	public void read(final String UUID, final List<Slice> sliceList) {

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Line");
		if(!NetworkStatus.isConnected)
			query.fromLocalDatastore();
			
		query.whereEqualTo("UUID", UUID);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject parseLine, ParseException e) {
				if (parseLine == null) {
					Log.i("Line", "Read: The getFirst request failed.");
				} else {

//					try {
						Log.i("Line", "Starting read");
//						parseLine.fetch();
						Line line = new Line();
						line.setId(parseLine.getString("UUID"));
						line.setStartUUID(parseLine.getString("startUUID"));
						line.setFinishUUID(parseLine.getString("finishUUID"));
						PointCRUD pointCRUD = new PointCRUD();
						pointCRUD.onresult = lineCRUD;
						Log.i("Line", "Line id:" + line.getId());
						for(Slice slice: sliceList)
							if(slice.getLineUUID().equals(line.getId()))
								slice.setPath(line);
						pointCRUD.read(parseLine.getString("startUUID"),parseLine, sliceList);
						pointCRUD.read(parseLine.getString("finishUUID"),parseLine, sliceList);
//					} catch (ParseException e1) {
//						 TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					
				}
			}
		});
	}
	
	
	@Override
	public void onResult(Point point, List<Slice> sliceList) {
		
		Point finishPoint;
		if (startPoint == null) {
			startPoint = point;
		} else {
			for(Slice slice: sliceList){
				if(slice.getPath().getStartUUID().equals(point.getObjectId()))
					slice.getPath().setStart(startPoint);
					slice.getPath().setFinish(point);
			}
			onresultLine.onResultRead(sliceList);
		}
		
	}
	
	public void update(final Slice slice) {

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Line");
		if(!NetworkStatus.isConnected)
			query.fromLocalDatastore();
			
		query.whereEqualTo("UUID", slice.getPath().getId());
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject parseLine, ParseException e) {
				if (parseLine == null) {
					Log.i("Line", "Read: The getFirst request failed.");
				} else {

					try {
						Log.i("Line", "Starting update");
						parseLine.fetch();
						parseLine.put("User", slice.getUser());
						parseLine.put("UUID", ""+ slice.getPath().getId());
						parseLine.put("startUUID", "" + slice.getPath().getStartUUID());
						parseLine.put("finishUUID", "" + slice.getPath().getFinishUUID());
						parseLine.saveInBackground();
						parseLine.pinInBackground();
						parseLine.saveEventually();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			}
		});
	}
	

	public void deleted(Line line) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(LINE);
		query.getInBackground(line.getId(), new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject oLine, ParseException e) {
				if (e == null) {
					oLine.deleteInBackground();
				} else {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void emputySliceList(List<Slice> sliceListNull1){
		onresultLine.onResultRead(sliceListNull1);
	}

	@Override
	public void onResult(ParseObject parseLine, Slice slice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResultRead(List<Slice> sliceList) {
		// TODO Auto-generated method stub
		
	}


}
