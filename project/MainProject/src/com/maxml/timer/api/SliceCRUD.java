package com.maxml.timer.api;

import java.util.Date;
import java.util.List;

import android.os.Handler;
import android.util.Log;

import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Slice.SliceType;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SliceCRUD {

	private Handler handler;

	public SliceCRUD(Handler handler) {
		this.handler = handler;
	}

	public void create(String user, Line path, Date startDate, Date endDate,
			String description, SliceType type) {
		try {
			ParseObject slice = new ParseObject("Slice");
			slice.put("User", user);
			// slice.put("Line", path);
			slice.put("startDate", startDate);
			slice.put("endDate", endDate);
			slice.put("Description", description);
			if (type.equals(SliceType.CALL))
				slice.put("SliceType", "CALL");
			if (type.equals(SliceType.WALK))
				slice.put("SliceType", "WALK");
			if (type.equals(SliceType.WORK))
				slice.put("SliceType", "WORK");
			if (type.equals(SliceType.REST))
				slice.put("SliceType", "REST");
			slice.saveInBackground();
			Log.i("SliceCreate", " Slice create ");
		} catch (Exception e) {
			Log.i("SliceCreate", " Slice dont create " + e);
		}

	}

	public void read(final String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Slice");
		query.whereEqualTo("objectId", id);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (object == null) {
					Log.i("SliceRead", "The getFirst request failed.");
				} else {
					try {
						object.fetch();
						Slice slice = new Slice();
						slice.setUser(object.getString("User"));
						slice.setId(id);
						slice.setStartDate(object.getDate("startDate"));
						slice.setEndDate(object.getDate("endDate"));
						slice.setDescription(object.getString("Description"));
						String sliceType = object.getString("SliceType");
						if (sliceType.equals("WALK"))
							slice.setType(SliceType.WALK);
						if (sliceType.equals("CALL"))
							slice.setType(SliceType.CALL);
						if (sliceType.equals("REST"))
							slice.setType(SliceType.REST);
						if (sliceType.equals("WORK"))
							slice.setType(SliceType.WORK);
						Log.i("SliceRead", "object " + object.toString());
						Log.i("SliceRead", "slice " + slice.toString());
						Log.i("SliceRead",
								"Slice Type  " + object.getString("SliceType"));
						handler.sendEmptyMessage(7);

					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Log.i("SliceRead", "Retrieved the object.");

				}
				
			}
		});

	}

	public void update(final Slice slice) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Slice");
		// Retrieve the object by id
		query.getInBackground(slice.getId(), new GetCallback<ParseObject>() {
			public void done(ParseObject parseSlice, ParseException e) {
				if (e == null) {
					// Now let's update it with some new data. In this case,
					// only cheatMode and score
					// will get sent to the Parse Cloud. playerName hasn't
					// changed.
					parseSlice.put("User", slice.getUser());
					// parseSlice.put("id", slice.getId());
					// parseSlice.put("path", slice.getPath());

					parseSlice.put("startDate", slice.getStartDate());
					parseSlice.put("endDate", slice.getEndDate());
					parseSlice.put("Description", slice.getDescription());
					parseSlice.put("SliceType", "" + slice.getType());
					parseSlice.saveInBackground();

					Log.i("SliceUpdate", "update User " + slice.getUser());
					Log.i("SliceUpdate",
							"update startDate " + slice.getStartDate());
					Log.i("SliceUpdate", "update endDate " + slice.getEndDate());
					Log.i("SliceUpdate",
							"update Description " + slice.getDescription());
					Log.i("SliceUpdate",
							"update SliceType " + "" + slice.getType());

				}
			}
		});
	}

	public void delete(final String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Slice");
		// Retrieve the object by id
		query.getInBackground(id, new GetCallback<ParseObject>() {
			public void done(ParseObject parseSlice, ParseException e) {
				if (e == null) {
					// Now let's update it with some new data. In this case,
					// only cheatMode and score
					// will get sent to the Parse Cloud. playerName hasn't
					// changed.
					Log.i("delete", "" + id + "deleted");
					parseSlice.deleteInBackground();

				}
			}
		});
	}

}
