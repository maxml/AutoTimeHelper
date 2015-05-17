package com.maxml.timer.api;

import com.maxml.timer.entity.User;
import com.parse.ParseUser;

public class UserAPI {

	String name = User.getInstance().getUsername();;
	String email = User.getInstance().getEmail();
	String objectId = User.getInstance().getObjectId();

	public void authorithation(ParseUser parseUser) {

		name = parseUser.getEmail();
		email = parseUser.getUsername();
		objectId = parseUser.getObjectId();

		User.getInstance().setUsername(name);
		User.getInstance().setEmail(email);
		User.getInstance().setObjectId(objectId);
	}

	public String getUserName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getObjectId() {
		return objectId;
	}

}
