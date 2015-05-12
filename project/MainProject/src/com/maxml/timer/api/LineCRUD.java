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

	public interface OnFinished {
		void done(List<Line> lines);

		void error();
	}

	private String LOG_TAG = "All_about_line";
	private Handler handler = new Handler();
	
	
	
public void create(Point pointStart, Point pointFinish) {
		
		PointCRUD pointCRUD = new PointCRUD();
		pointCRUD.onresult = this;
		
		pointCRUD.create(pointStart);
		pointCRUD.create(pointFinish);
	}
		
	public void create(String pointStartId, String pointFinishId){
		
		PointCRUD pointCRUD = new PointCRUD();
		pointCRUD.onresult = this;
		pointCRUD.read(pointStartId);
		pointCRUD.read(pointFinishId);
		
	}


	@Override
	public void onResult(ParseObject object) {
		// TODO Auto-generated method stub
		Log.i("Point", "мне вернулась точка");
		if(point1 == null){
			point1 = object;
		}else {
			point2 = object;
			ParseObject pair = new ParseObject("Line");
			//tyt dolgen bit USER!!!!!!!!!!!!!!!!!!!!!!!1
			pair.put("start", point1);
			pair.put("finish", point2);
			pair.saveInBackground();
//			onresultR.onResult(pair);
		}
				
		
}
	
	

	public void read(final OnFinished listener) {

		// if(!NetworkAvailiabilityHelper.getIsNetworkAvailable()){
		// query.fromLocalDatastore();
		// TODO querry dad]ta from locakll deetdtda base
		// return;
		// }

		ParseQuery<ParseObject> query = ParseQuery.getQuery(POINT);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> points, ParseException e) {
				if (e == null) {

					final Map<String, Point> myPointMap = new HashMap<String, Point>();
					for (ParseObject poijnt : points) {

						int x = poijnt.getInt(X);
						int y = poijnt.getInt(Y);
						String s = poijnt.getString(USER);
						String id = poijnt.getObjectId();
						// Log.d("TAG", "x: " + x + " y: " + y + " user:" + s
						// + " id: " + id);
//						myPointMap.put(id, new Point(s, x, y));
					}

					ParseQuery<ParseObject> querySecond = ParseQuery
							.getQuery(LINE);
					querySecond
							.findInBackground(new FindCallback<ParseObject>() {
								public void done(List<ParseObject> LineList,
										ParseException e) {
									if (e == null) {
										Log.d("TAG",
												"Retrieved " + LineList.size()
														+ " lines");
										List<Line> lines = new LinkedList<Line>();

										for (ParseObject object : LineList) {
											ParseObject start = object
													.getParseObject(START);
											ParseObject finish = object
													.getParseObject(FINISH);

											String id1 = start.getObjectId();
											String id2 = finish.getObjectId();

//											Line line = new Line(myPointMap
//													.get(id1), myPointMap
//													.get(id2));
//											lines.add(line);
										}

										if (listener != null) {
											listener.done(lines);
										}
									} else {
										Log.d("TAG", "Error: " + e.getMessage());
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

	public void update(final Line newline, final String idLine) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(LINE);
		query.getInBackground(idLine, new GetCallback<ParseObject>() {

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

	public void deleted(String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(LINE);
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

}
