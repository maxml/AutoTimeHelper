package com.maxml.timer.api;

import android.util.Log;

import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Slice.SliceType;
import com.maxml.timer.entity.Table;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SliceCRUD implements OnResult {

	public OnResult onresultSlice;

	public void create(final Slice slice) {
		try {
			
			LineCRUD lineCRUD = new LineCRUD();
			lineCRUD.onresultLine = this;
			lineCRUD.create(slice.getPath().getStart(), slice.getPath()
					.getFinish(), slice);
			

			Log.i("SliceCreate", " Slice create ");
		} catch (Exception e) {
			Log.i("SliceCreate", " Slice dont create " + e);
		}

	}

	@Override
	// linieCRUD - read or create
	public void onResult(ParseObject object,Slice slice) {
		Log.i("Slice", "��� ��������� Line & Slice");
		Log.i("Slice", "" + object.getString("User"));
		ParseObject sliceParse = new ParseObject("Slice");
		sliceParse.put("User", slice.getUser());
		sliceParse.put("startDate", slice.getStartDate());
		sliceParse.put("endDate", slice.getEndDate());
		sliceParse.put("Description", slice.getDescription());
		if (slice.getType().equals(SliceType.CALL))
			sliceParse.put("SliceType", "CALL");
		if (slice.getType().equals(SliceType.WALK))
			sliceParse.put("SliceType", "WALK");
		if (slice.getType().equals(SliceType.WORK))
			sliceParse.put("SliceType", "WORK");
		if (slice.getType().equals(SliceType.REST))
			sliceParse.put("SliceType", "REST");
		sliceParse.put("Line", object);
		sliceParse.saveInBackground();
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
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Log.i("SliceRead", "Retrieved the object.");
					onresultSlice.onResult(object);
				}

			}
		});

	}

	public void update(final Slice slice) {

		final LineCRUD lineCRUD = new LineCRUD();
		lineCRUD.onresultLine = this;

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Slice");
		// Retrieve the object by id
		query.getInBackground(slice.getId(), new GetCallback<ParseObject>() {
			public void done(final ParseObject parseSlice, ParseException e) {
				if (e == null) {
					parseSlice.put("User", slice.getUser());
					parseSlice.put("startDate", slice.getStartDate());
					parseSlice.put("endDate", slice.getEndDate());
					parseSlice.put("Description", slice.getDescription());
					parseSlice.put("SliceType", "" + slice.getType());
					LineCRUD lineCRUD = new LineCRUD();
					lineCRUD.update(slice.getPath());

					ParseQuery<ParseObject> query = ParseQuery.getQuery("Line");
					query.whereEqualTo("objectId", slice.getPath().getId());
					query.getFirstInBackground(new GetCallback<ParseObject>() {
						public void done(ParseObject object, ParseException e) {
							if (object == null) {
								Log.i("Line",
										"Read: The getFirst request failed.");
							} else {

								try {
									object.fetch();

									parseSlice.put("Line", object);
									parseSlice.saveInBackground();

								} catch (ParseException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					});


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
					Log.i("delete", "" + id + "deleted");
					parseSlice.deleteInBackground();

				}
			}
		});
	}

	public void sync(Table table) {

		for (final Slice slice : table.getList()) {
			if (slice.getId() == null) {
				create(slice);
			} else {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Slice");
				// Retrieve the object by id
				query.getInBackground(slice.getId(),
						new GetCallback<ParseObject>() {
							public void done(final ParseObject parseSlice,
									ParseException e) {
								if (e == null) {
									Slice sliceDummy = new Slice();
									sliceDummy.setId(parseSlice.getObjectId());
									sliceDummy.setUser(parseSlice
											.getString("User"));
									sliceDummy.setStartDate(parseSlice
											.getDate("startDate"));
									sliceDummy.setEndDate(parseSlice
											.getDate("endDate"));
									sliceDummy.setDescription(parseSlice
											.getString("Description"));
									String sliceType = parseSlice
											.getString("SliceType");
									if (sliceType.equals("WALK"))
										sliceDummy.setType(SliceType.WALK);
									if (sliceType.equals("CALL"))
										sliceDummy.setType(SliceType.CALL);
									if (sliceType.equals("REST"))
										sliceDummy.setType(SliceType.REST);
									if (sliceType.equals("WORK"))
										sliceDummy.setType(SliceType.WORK);
									if (slice.equals(sliceDummy)) {
										update(slice);
									}
								}
							}
						});
			}
		}

	}

	@Override
	public void onResult(ParseObject object) {
		// TODO Auto-generated method stub
		
	}

}
