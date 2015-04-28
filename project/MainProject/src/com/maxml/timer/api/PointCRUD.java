package com.maxml.timer.api;

import com.maxml.timer.entity.Point;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import com.parse.ParseQuery;

import android.os.Handler;
import android.util.Log;

public class PointCRUD {

	private Handler handler;

	public PointCRUD(Handler handler) {
		this.handler = handler;
	}

	public void create(Point point) {
		ParseObject pair = new ParseObject("Point");
		pair.put("User", point.getUser());
		pair.put("x", point.getX());
		pair.put("y", point.getY());
		pair.saveInBackground();
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
						Log.i("Point",
								"Read: User = " + object.getString("User")
										+ ", x = " + object.getNumber("x")
										+ ", y = " + object.getNumber("y"));
					} catch (ParseException e1) {
						Log.d("Point", "Read: object not nul, but" + e1);
						e1.printStackTrace();
					}
					handler.sendEmptyMessage(0);
					Log.i("Point", "Read: User = " + object.getString("User")
							+ ", x = " + object.getNumber("x") + ", y = "
							+ object.getNumber("y"));

					Log.i("Point", "Read: Error " + e);
				}

			}
		});
	}

	public void update(final Point point) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Point");
		// Retrieve the object by id
		query.getInBackground(point.getId(), new GetCallback<ParseObject>() {
			public void done(ParseObject parsePoint, ParseException e) {
				if (e == null) {
					parsePoint.put("User", point.getUser());
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

}
