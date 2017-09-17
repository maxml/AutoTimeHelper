package com.maxml.timer.api;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxml.timer.api.interfaces.OnDbResult;
import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class LineCRUD implements OnDbResult {

//	ParseObject point1;
//	ParseObject point2;
	Point startPoint = null;
	LineCRUD lineCRUD = this;
	public OnDbResult onresultLine;

	private DatabaseReference lineReference;
	private Handler handler;

	private static final String USER = "User";
	private static final String START = "start";
	private static final String FINISH = "finish";
	private static final String LINE = "Line";
	private static final String POINT = "Point";
	private static final String X = "x";
	private static final String Y = "y";
	private static final String TG = "myboolshit";

	private String user = "nullUser";

	public LineCRUD(Handler handler) {
		lineReference = FirebaseDatabase.getInstance().getReference().child(Constants.LINE_DATABASE_PATH);
		this.handler = handler;
	}

	public void create(Line line) {
		Log.i("Line", " Line starting create");
		// get Firebase id
		String key = lineReference.push().getKey();
		// set id entity
		line.setId(key);
		try {
			lineReference.child(key).setValue(line).addOnCompleteListener(new OnCompleteListener<Void>() {
				@Override
				public void onComplete(@NonNull Task<Void> task) {
					if (task.isSuccessful()) {
						handler.sendEmptyMessage(Constants.DB_RESULT_OK);
					} else {
						handler.sendEmptyMessage(Constants.DB_RESULT_FALSE);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public void onResult(ParseObject parsePointStart,ParseObject parsePointFinish, final Slice slice) {
//
//			final ParseObject parseLine = new ParseObject("Line");
//			parseLine.put("start", parsePointStart);
//			parseLine.put("finish", parsePointFinish);
//			parseLine.put("User", slice.getUserId());
//			parseLine.put("UUID", ""+UUID.randomUUID());
//			parseLine.put("startUUID", parsePointFinish.getString("UUID"));
//			parseLine.put("finishUUID", ""+parsePointFinish.getString("UUID"));
//			if(NetworkStatus.isConnected){
//			parseLine.saveInBackground(
//			new SaveCallback() {
//				@Override
//				public void done(ParseException e) {
//					Log.i("Line", "line is created " + parseLine.getId());
//					parseLine.pinInBackground();
//					onresultLine.onResult(parseLine, slice);
//				}
//			}
//
//			);
//
//		}else{
//			Log.i("Line", "line is created ofline UUID" + parseLine.getString("UUID"));
//			parseLine.saveInBackground();
//			parseLine.pinInBackground();
//			onresultLine.onResult(parseLine, slice);
//		}
//	}



	public void read(String user) {

		if (user == null) return;
		lineReference.orderByChild(Constants.LINE_USER)
				.equalTo(user)
				.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						if (dataSnapshot.exists()) {
							List<Line> list = new ArrayList<>();
							for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
								Line line = snapshot.getValue(Line.class);
								if (!line.isDelete()) {
									list.add(line);
								}
							}
							// send result
							Message m = handler.obtainMessage(Constants.DB_RESULT_LIST, list);
							handler.sendMessage(m);
						} else {
							handler.sendEmptyMessage(Constants.DB_RESULT_FALSE);
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {
						handler.sendEmptyMessage(Constants.DB_RESULT_FALSE);
					}
				});

//		ParseQuery<ParseObject> query = ParseQuery.getQuery("Line");
//		if(!NetworkStatus.isConnected)
//			query.fromLocalDatastore();
//
//		query.whereEqualTo("UUID", UUID);
//		query.getFirstInBackground(new GetCallback<ParseObject>() {
//			public void done(ParseObject parseLine, ParseException e) {
//				if (parseLine == null) {
//					Log.i("Line", "Read: The getFirst request failed.");
//				} else {
//
////					try {
//						Log.i("Line", "Starting read");
////						parseLine.fetch();
//						Line line = new Line();
//						line.setId(parseLine.getString("UUID"));
//						line.setStartUUID(parseLine.getString("startUUID"));
//						line.setFinishUUID(parseLine.getString("finishUUID"));
//						PointCRUD pointCRUD = new PointCRUD();
//						pointCRUD.onResult = lineCRUD;
//						Log.i("Line", "Line id:" + line.getId());
//						for(Slice slice: sliceList)
//							if(slice.getLineUUID().equals(line.getId()))
//								slice.setPath(line);
//						pointCRUD.read(parseLine.getString("startUUID"),parseLine, sliceList);
//						pointCRUD.read(parseLine.getString("finishUUID"),parseLine, sliceList);
////					} catch (ParseException e1) {
////						 TODO Auto-generated catch block
////						e1.printStackTrace();
////					}
//
//				}
//			}
//		});
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

//		ParseQuery<ParseObject> query = ParseQuery.getQuery("Line");
//		if(!NetworkStatus.isConnected)
//			query.fromLocalDatastore();
//
//		query.whereEqualTo("UUID", slice.getPath().getId());
//		query.getFirstInBackground(new GetCallback<ParseObject>() {
//			public void done(ParseObject parseLine, ParseException e) {
//				if (parseLine == null) {
//					Log.i("Line", "Read: The getFirst request failed.");
//				} else {
//
//					try {
//						Log.i("Line", "Starting update");
//						parseLine.fetch();
//						parseLine.put("User", slice.getUserId());
//						parseLine.put("UUID", ""+ slice.getPath().getId());
//						parseLine.put("startUUID", "" + slice.getPath().getStartUUID());
//						parseLine.put("finishUUID", "" + slice.getPath().getFinishUUID());
//						parseLine.saveInBackground();
//						parseLine.pinInBackground();
//						parseLine.saveEventually();
//					} catch (ParseException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//
//				}
//			}
//		});
	}
	

	public void deleted(Line line) {
//		ParseQuery<ParseObject> query = ParseQuery.getQuery(LINE);
//		query.getInBackground(line.getId(), new GetCallback<ParseObject>() {
//
//			@Override
//			public void done(ParseObject oLine, ParseException e) {
//				if (e == null) {
//					oLine.deleteInBackground();
//				} else {
//					e.printStackTrace();
//				}
//			}
//		});
	}
	
	public void emputySliceList(List<Slice> sliceListNull1){
		onresultLine.onResultRead(sliceListNull1);
	}

//	@Override
//	public void onResult(ParseObject parseLine, Slice slice) {
//		// TODO Auto-generated method stub
//
//	}

	@Override
	public void onResultRead(List<Slice> sliceList) {
		// TODO Auto-generated method stub
		
	}


}
