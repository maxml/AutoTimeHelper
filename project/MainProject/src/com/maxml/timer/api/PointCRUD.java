package com.maxml.timer.api;

import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.*;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.os.Handler;
import android.util.Log;

public class PointCRUD {

	
	
	public OnResult onresult;

	

	public void create(Point point) {
		final ParseObject pair = new ParseObject("Point");
		pair.put("x", point.getX());
		pair.put("y", point.getY());
		pair.saveInBackground(new SaveCallback() {
		    @Override
		    public void done(ParseException e) {
		        onresult.onResult(pair);
		    }
		});
		Log.i("Point", "Create: Point created");
	}

	public void read(String id) {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Point");
		query.whereEqualTo("objectId", id);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (object == null) {
					Log.i("Point", "Read: The getFirst request failed.");
				} else {
					try {
						object.fetch();
						
					} catch (ParseException e1) {
						Log.d("Point", "Read: object not nul, but" + e1);
						e1.printStackTrace();
					}
					
					onresult.onResult(object);
				}
			}
		});
	}

	public void update(final Point point) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Point");
		// Retrieve the object by id
		query.getInBackground(point.getObjectId(), new GetCallback<ParseObject>() {
			public void done(ParseObject parsePoint, ParseException e) {
				if (e == null) {
					parsePoint.put("x", point.getX());
					parsePoint.put("y", point.getY());
					parsePoint.saveInBackground();
					Log.i("Point", "Update: point update");
				} else {
					Log.i("Point", "Update: " + e);
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

	public void create(Point point, final Slice slice) {
		// TODO Auto-generated method stub

		final ParseObject pair = new ParseObject("Point");
		pair.put("x", point.getX());
		pair.put("y", point.getY());
		pair.saveInBackground(new SaveCallback() {
		    @Override
		    public void done(ParseException e) {
		        onresult.onResult(pair,slice);
		    }
		});
		Log.i("Point", "Create: Point created");
	}

	

}
