package com.maxml.timer.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.util.Log;

import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LineCRUD implements OnResult {
	
	private ParseObject point1;
	private ParseObject point2;
	public OnResult onresult;
	
	private static final String USER = "User";
	private static final String START = "start";
	private static final String FINISH = "finish";
	private static final String LINE = "Line";
	private static final String POINT = "Point";
	private static final String X = "x";
	private static final String Y = "y";
	private static final String OBJECTID = "objectId";
	private static final String TG = "myboolshit";
	
	public interface OnFinished {
		void done(List<Line> lines);
		
		void error();
	}
	
	private String user = "nullUser";
	private NetworkReceiver nt = new NetworkReceiver();
	
	private String LOG_TAG = "All_about_line";
	private Handler handler = new Handler();
	
	public void create(Point pointStart, Point pointFinish, String user) {
		Log.d(TG, "starting to create");
		PointCRUD pointCRUD = new PointCRUD();
		pointCRUD.onresult = this;
		pointCRUD.create(pointStart);
		pointCRUD.create(pointFinish);
		this.user = user;
	}
	
	public void create(String pointStartId, String pointFinishId, String user) {
		
		PointCRUD pointCRUD = new PointCRUD();
		pointCRUD.onresult = this;
		pointCRUD.read(pointStartId);
		pointCRUD.read(pointFinishId);
		this.user = user;
	}
	
	@Override
	public void onResult(ParseObject object) {
		Log.i("Point", "мне вернулась точка");
		if (point1 == null) {
			point1 = object;
		} else {
			point2 = object;
			ParseObject pair = new ParseObject(LINE);
			pair.put(START, point1);
			pair.put(FINISH, point2);
			pair.put(USER, user);
			pair.saveInBackground();
		}
		
	}
	
	public void read(final OnFinished listener) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(POINT);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> points, ParseException e) {
				if (e == null) {
					
					final Map<String, Point> myPointMap = new HashMap<String, Point>();
					for (ParseObject poijnt : points) {
						
						double x = poijnt.getInt(X);
						double y = poijnt.getInt(Y);
						String id = poijnt.getObjectId();
						myPointMap.put(id, new Point(x, y));
					}
					
					ParseQuery<ParseObject> querySecond = ParseQuery.getQuery(LINE);
					querySecond.findInBackground(new FindCallback<ParseObject>() {
						public void done(List<ParseObject> LineList, ParseException e) {
							if (e == null) {
								Log.d(TG, "Retrieved " + LineList.size() + " lines");
								List<Line> lines = new LinkedList<Line>();
								
								for (ParseObject object : LineList) {
									ParseObject start = object.getParseObject(START);
									ParseObject finish = object.getParseObject(FINISH);
									String user = object.getString(USER);
									
									String id1 = start.getObjectId();
									String id2 = finish.getObjectId();
									
									Line line = new Line(myPointMap.get(id1), myPointMap.get(id2), user);
									lines.add(line);
									Log.d(TG, "add line: start x = " + start.getInt(X) + " y = " + start.getInt(Y)
											+ " finish x = " + finish.getDouble(X) + " y = " + finish.getDouble(Y)
											+ " user = " + user);
								}
								
								if (listener != null) {
									listener.done(lines);
								}
							} else {
								Log.d(TG, "Error: " + e.getMessage());
								listener.error();
							}
						}
					});
				} else {
					listener.error();
				}
			}
		});
		
	}
	
	public void update(final Line newline) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(LINE);
		query.getInBackground(newline.getId(), new GetCallback<ParseObject>() {
			
			@Override
			public void done(ParseObject line, ParseException e) {
				if (e == null) {
					line.put(START, newline.getStart());
					line.put(FINISH, newline.getFinish());
				} else {
					e.printStackTrace();
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
	
}
