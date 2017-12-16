package com.maxml.timer.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.util.Log;

import com.maxml.timer.api.interfaces.OnDbResult;
import com.maxml.timer.api.interfaces.OnResultList;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Slice.SliceType;
import com.maxml.timer.entity.Table;
import com.maxml.timer.util.NetworkStatus;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class SliceCRUD implements OnDbResult {
	
	public OnResultList onresultList;
	private int readCount = 0;
	SliceCRUD sliceCRUD = this;
	
	public void create(final Slice slice) {
		try {
			Log.i("Slice", " Slice starting create");
			LineCRUD lineCRUD = new LineCRUD();
			lineCRUD.onresultLine = this;
			lineCRUD.create(slice);
		} catch (Exception e) {
			Log.i("Slice", " Slice dont create " + e);
		}
		
	}
	
	@Override
	// linieCRUD - read or create
	public void onResult(final ParseObject parseLine, Slice slice) {
		
		final ParseObject parseSlice = new ParseObject("Slice");
		parseSlice.put("User", slice.getUser());
		parseSlice.put("startDate", slice.getStartDate());
		parseSlice.put("endDate", slice.getEndDate());
		parseSlice.put("Description", slice.getDescription());
		if (slice.getType().equals(SliceType.CALL))
			parseSlice.put("SliceType", "CALL");
		if (slice.getType().equals(SliceType.WALK))
			parseSlice.put("SliceType", "WALK");
		if (slice.getType().equals(SliceType.WORK))
			parseSlice.put("SliceType", "WORK");
		if (slice.getType().equals(SliceType.REST))
			parseSlice.put("SliceType", "REST");
		parseSlice.put("UUID", "" + UUID.randomUUID());
		parseSlice.put("Line", parseLine);
		parseSlice.put("LineUUID", "" + parseLine.getString("UUID"));
		parseSlice.put("deleted", false);
		if (NetworkStatus.isConnected)
			parseSlice.saveInBackground(
			
			new SaveCallback() {
				@Override
				public void done(ParseException e) {
					parseSlice.pinInBackground();
					Log.i("Slice", "Slice is created id: " + parseSlice.getObjectId() + " Line UUID: "
							+ parseLine.getString("UUID"));
				}
			}
			
			);
		if (!NetworkStatus.isConnected) {
			parseSlice.saveInBackground();
			parseSlice.pinInBackground();
			Log.i("Slice", "Slice is created offline, id:" + parseSlice.getString("UUID"));
			// parseSlice.saveEventually();
		}
		
	}
	
	public void read(final String user) {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Slice");
		if (!NetworkStatus.isConnected)
			query.fromLocalDatastore();
		query.whereEqualTo("User", user);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> parseSliceList, ParseException e) {
				if (parseSliceList == null) {
					Log.i("Slice", "The getFirst request failed.");
					
				} else {
					
					if (parseSliceList.size() == 0) {
						List<Slice> sliceListNull1 = new ArrayList<Slice>();
						LineCRUD lineCRUD = new LineCRUD();
						sliceListNull1.add(new Slice("", null, new Date(), new Date(), "your list is emputy",
								SliceType.REST));
						lineCRUD.onresultLine = sliceCRUD;
						lineCRUD.emputySliceList(sliceListNull1);
					} else {
						
						Log.i("SliceRead", "Starting read");
						List<Slice> sliceList = new ArrayList<Slice>();
						for (ParseObject parseSlice : parseSliceList) {
							Log.i("SliceRead", "Slice: " + parseSlice.getString("UUID"));
							
							Slice slice = new Slice();
							slice.setUser(parseSlice.getString("User"));
							slice.setStartDate(parseSlice.getDate("startDate"));
							slice.setEndDate(parseSlice.getDate("endDate"));
							slice.setDescription(parseSlice.getString("Description"));
							slice.setUpdatedat(parseSlice.getUpdatedAt());
							
							String sliceType = parseSlice.getString("SliceType");
							if (sliceType.equals("WALK"))
								slice.setType(SliceType.WALK);
							if (sliceType.equals("CALL"))
								slice.setType(SliceType.CALL);
							if (sliceType.equals("REST"))
								slice.setType(SliceType.REST);
							if (sliceType.equals("WORK"))
								slice.setType(SliceType.WORK);
							slice.setId(parseSlice.getString("UUID"));
							slice.setLineUUID(parseSlice.getString("LineUUID"));
							
							if (!parseSlice.getBoolean("deleted")) {
								sliceList.add(slice);
								LineCRUD lineCRUD = new LineCRUD();
								lineCRUD.onresultLine = sliceCRUD;
								lineCRUD.read(parseSlice.getString("LineUUID"), sliceList);
							}
							
							Log.i("SliceRead", "" + parseSliceList.size());
							Log.d("SliceCRUD_Special", "slice: " + parseSlice.getString("Description"));
							
						}
						
					}
				}
			}
		});
		
	}
	
	@Override
	public void onResultRead(List<Slice> sliceList) {
		// TODO Auto-generated method stub
		readCount++;
		Log.i("SliceRead", " Read finish");
		if (readCount == sliceList.size()) {
			Log.i("SliceRead", " Read finish 2");
			Log.i("Slice", "Slice list size: " + sliceList.size());
			Collections.sort(sliceList, Slice.sliceComparator);
			onresultList.OnResultSlices(sliceList);
		}
	}
	
	public void update(final Slice slice) throws InterruptedException {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Slice");
		if (!NetworkStatus.isConnected)
			query.fromLocalDatastore();
		
		query.whereEqualTo("UUID", "" + slice.getId());
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject parseSlice, ParseException e) {
				if (parseSlice == null) {
					Log.i("SliceUpdate", "Update: The getFirst request failed.");
				} else {
					
					try {
						
						parseSlice.fetch();
						Log.i("SliceUpdate", "Starting update");
						parseSlice.put("User", slice.getUser());
						parseSlice.put("startDate", slice.getStartDate());
						parseSlice.put("endDate", slice.getEndDate());
						parseSlice.put("Description", slice.getDescription());
						if (slice.getType().equals(SliceType.CALL))
							parseSlice.put("SliceType", "CALL");
						if (slice.getType().equals(SliceType.WALK))
							parseSlice.put("SliceType", "WALK");
						if (slice.getType().equals(SliceType.WORK))
							parseSlice.put("SliceType", "WORK");
						if (slice.getType().equals(SliceType.REST))
							parseSlice.put("SliceType", "REST");
						parseSlice.put("UUID", "" + UUID.randomUUID());
						parseSlice.put("LineUUID", "" + slice.getPath().getId());
						LineCRUD lineCRUD = new LineCRUD();
						lineCRUD.update(slice);
						PointCRUD pointCRUD = new PointCRUD();
						pointCRUD.update(slice.getPath().getStart());
						pointCRUD.update(slice.getPath().getFinish());
						parseSlice.saveInBackground();
						parseSlice.pinInBackground();
						Log.i("Slice", "Slice is update, UUID:" + parseSlice.getString("UUID"));
						parseSlice.saveEventually();
						
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			}
		});
	}
	
	public void delete(final String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Slice");
		if (!NetworkStatus.isConnected)
			query.fromLocalDatastore();
		
		query.whereEqualTo("UUID", "" + id);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject parseSlice, ParseException e) {
				if (parseSlice == null) {
					
					Log.i("Slice", "Deleted: The getFirst request failed.");
				} else {
					Log.i("Slice", "Slice " + id + " is deleted");
					parseSlice.put("deleted", true);
					parseSlice.pinInBackground();
					parseSlice.saveInBackground();
				}
			}
		});
	}
	
	public void sync(Table table) {
		Log.i("Slice", "" + NetworkStatus.isConnected);
		
		Log.i("Slice", "Slice synchronized start");
		for (final Slice slice : table.getList()) {
			if (slice.getId() == null) {
				create(slice);
			} else {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Slice");
				if (!NetworkStatus.isConnected)
					query.fromLocalDatastore();
				
				query.whereEqualTo("UUID", slice.getId());
				query.getFirstInBackground(new GetCallback<ParseObject>() {
					public void done(ParseObject parseSlice, ParseException e) {
						if (parseSlice == null) {
							Log.i("Slice", "Sync: The getFirst request failed.");
						} else {
							if (!slice.getUpdatedat().equals(parseSlice.getUpdatedAt()))// ----------
								try {
									update(slice);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							Log.i("Slice", "Sync: update dont need");
						}
					}
				});
			}
		}
		
	}
	
	@Override
	public void onResult(Point point, List<Slice> sliceList) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onResult(ParseObject parsePoint, ParseObject parsePointFinish, Slice slice) {
		// TODO Auto-generated method stub
		
	}
	
}
