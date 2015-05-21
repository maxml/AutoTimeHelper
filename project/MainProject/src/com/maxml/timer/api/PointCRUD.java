package com.maxml.timer.api;

import java.util.List;
import java.util.UUID;

import android.util.Log;

import com.maxml.timer.App;
import com.maxml.timer.api.interfaces.OnDbResult;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class PointCRUD {

	PointCRUD pointCRUD = this;
	public OnDbResult onresult;

	public void create(Point point, final Slice slice) {
		Log.i("Point", " Point starting create");
		final ParseObject parsePoint = new ParseObject("Point");
		parsePoint.put("x", point.getX());
		parsePoint.put("y", point.getY());
		parsePoint.put("User", slice.getUser());
		parsePoint.put("UUID", ""+UUID.randomUUID());
		if(App.isNetworkAvailable){
		parsePoint.saveInBackground(new SaveCallback() {
		    @Override
		    public void done(ParseException e) {
		    	
		        onresult.onResult(parsePoint, slice);
		        Log.i("Point", "Create: Point created " + parsePoint.getObjectId());
		        parsePoint.saveEventually();
		    }
		});
		}else{
		parsePoint.saveInBackground();
		parsePoint.pinInBackground();
		onresult.onResult(parsePoint, slice);
		Log.i("Point", "Create: Point created offline. UUID: " + parsePoint.getString("UUID"));
		}
		
	}

	
	public void read(String UUID, ParseObject parseLine,
			final List<Slice> sliceList) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Point");
		if(!App.isNetworkAvailable)
			query.fromLocalDatastore();
			
		query.whereEqualTo("UUID", UUID);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject parsePoint, ParseException e) {
				if (parsePoint == null) {
					Log.i("Point", "Read: The getFirst request failed.");
				} else {
					try {
						Log.i("Point", "Starting read");
						parsePoint.fetch();
						Point point = new Point(parsePoint.getDouble("x"),parsePoint.getDouble("y"));
						point.setObjectId(parsePoint.getString("UUID"));
						onresult.onResult(point,sliceList);
					} catch (ParseException e1) {
						Log.d("Point", "Read: object not nul, but" + e1);
						e1.printStackTrace();
					}
					
					
				}
			}
		});
	}

	public void update(final Point point) {

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Point");
		if(!App.isNetworkAvailable)
			query.fromLocalDatastore();
			
		query.whereEqualTo("UUID", point.getObjectId());
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject parsePoint, ParseException e) {
				if (parsePoint == null) {
					Log.i("Point", "Read: The getFirst request failed.");
				} else {

					try {
						Log.i("Point", "Starting update");
						parsePoint.fetch();
						parsePoint.put("x",point.getX());
						parsePoint.put("y",point.getY());
						parsePoint.saveInBackground();
						parsePoint.pinInBackground();
						parsePoint.saveEventually();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			}
		});
	}

	public void delete(String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Point");
		// Retrieve the object by id
		query.getInBackground(id, new GetCallback<ParseObject>() {
			public void done(ParseObject parseSlice, ParseException e) {
				if (e == null) {

					parseSlice.deleteInBackground();
					Log.i("Point", "Delete: point deleted");

				}
			}
		});
	}


	
	


	


	

}
