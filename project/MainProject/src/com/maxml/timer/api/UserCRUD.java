package com.maxml.timer.api;

import android.util.Log;

import com.maxml.timer.App;
import com.maxml.timer.api.interfaces.OnDbUserResult;
import com.maxml.timer.entity.User;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class UserCRUD {
	
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String EMAIL = "email";
	private static final String USER = "User";
	public OnDbUserResult onresult;
	
	public void create(User user) {
		Log.d("User", "start method create in UserCRUD");
		ParseObject usr = new ParseObject(USER);
		usr.put(USERNAME, user.getUsername());
		usr.put(PASSWORD, user.getPassword());
		usr.put(EMAIL, user.getEmail());
		usr.saveInBackground();
		usr.pinInBackground();
	}
	
	public void read(String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(USER);
		if (!App.isNetworkAvailable)
			query.fromLocalDatastore();
		
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject parseUser, ParseException e) {
				if (parseUser == null) {
					Log.d("User", "User didnt find");
				} else {
					try {
						parseUser.fetch();
						User user = new User();
						user.setUsername(parseUser.getString(USERNAME));
						user.setEmail(parseUser.getString(EMAIL));
						user.setObjectId(parseUser.getObjectId());
						
						onresult.onResult(user);
						
					} catch (ParseException e1) {
						Log.d("User", "Read: object not nul, but" + e1);
						e1.printStackTrace();
					}
					
				}
			}
		});
	}
	
	public void update(final User user) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(USER);
		if (!App.isNetworkAvailable)
			query.fromLocalDatastore();
		
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject parseUser, ParseException e) {
				if (parseUser == null) {
					Log.d("User", "Read: The getFirst request failed.");
				} else {
					try {
						parseUser.fetch();
						parseUser.put(USERNAME, user.getUsername());
						parseUser.put(EMAIL, user.getEmail());
						parseUser.put(PASSWORD, user.getPassword());
						
						parseUser.saveInBackground();
						parseUser.pinInBackground();
					} catch (ParseException e1) {
						Log.d("User", "User update faild, u ask why? Because" + e1);
						e1.printStackTrace();
					}
				}
			}
		});
	}
	
	public void delete(String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(USER);
		query.getInBackground(id, new GetCallback<ParseObject>() {
			public void done(ParseObject parseUser, ParseException e) {
				if (e == null) {
					parseUser.deleteInBackground();
					Log.i("User", "Delete: point deleted");
				}
			}
		});
	}
	
	public void sync(User user) {
		Log.d("User", "start method sync");
		if (user.getObjectId() == null) {
			Log.d("User", "create user");
			create(user);
		} else {
			Log.d("User", "update user");
			update(user);
		}
	}
}
