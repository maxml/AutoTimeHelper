package com.maxml.timer.entity;

import android.net.Uri;

public class User {
	private String objectId;
	private String username;
	private String email;
	private Uri photo;

	public User() {
	}

	public Uri getPhoto() {
		return photo;
	}

	public void setPhoto(Uri photo) {
		this.photo = photo;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	@Override
	public String toString() {
		return "User [objectId=" + objectId + ", username=" + username + ", email=" + email + /*
																							 * ", password="
																							 * +
																							 * password
																							 * +
																							 */"]";
	}

}