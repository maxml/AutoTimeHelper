package com.maxml.timer.api;

import android.os.Handler;
import android.util.Log;

import com.maxml.timer.entity.Line;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LineCRUD {

	private String LOG_TAG = "All_about_line";
	private boolean networkDataBaseAvailable = false;
	private Handler handler = new Handler();

	public void create(Line line) {
		ParseObject lineage = new ParseObject("Line");
		lineage.put("User", line.getUser());
		lineage.put("distance", line.getDistance());
		lineage.put("start", line.getStart());
		lineage.put("finish", line.getFinish());
		lineage.saveEventually();
	}

	public void read(String id) {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
		query.whereEqualTo("objectId", id);
		if(!networkDataBaseAvailable){
			query.fromLocalDatastore();
		}
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (object == null) {
					Log.i(LOG_TAG, "Read: The getFirst request failed.");
				} else {
					try {
						object.fetch();
						
						Log.i(LOG_TAG,
								"Read: User = " + object.getString("User")
										+ ", distance = "
										+ object.getNumber("distance")
										+ ", start = "
										+ object.getString("start")
										+ ", finish = "
										+ object.getString("finish"));
					} catch (ParseException ex) {
						Log.d(LOG_TAG, "Read: object not null, but" + ex);
						ex.printStackTrace();
					}
					handler.sendEmptyMessage(0);
					Log.i(LOG_TAG, "Read: Error " + e);
				}

			}
		});
	}

	public void update(final Line line, String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Line");
		query.getInBackground(id, new GetCallback<ParseObject>() {
			
			@Override
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					object.put("distance", line.getDistance());
					object.put("start", line.getStart());
					object.put("finish", line.getFinish());
					object.saveEventually();
				} else {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void deleted(String id){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Line");
		query.getInBackground(id, new GetCallback<ParseObject>() {
			
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

	public boolean getIsNetworkDataBaseAvailable() {
		return networkDataBaseAvailable;
	}

	public void setIsNetworkDataBaseAvailable(boolean isNetworkDataBaseAvailable) {
		this.networkDataBaseAvailable = isNetworkDataBaseAvailable;
	}

}
