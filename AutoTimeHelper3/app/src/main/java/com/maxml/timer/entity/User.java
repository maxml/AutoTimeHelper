package com.maxml.timer.entity;

import android.net.Uri;

public class User {
	private String id;
	private String username;
	private String email;
	private String photoUri;

	public User() {
	}

	public  String getPhoto() {
		return photoUri;
	}

	public void setPhoto(String photoUri) {
		this.photoUri = photoUri;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		return "User [id=" + id + ", username=" + username + ", email=" + email + /*
																							 * ", password="
																							 * +
																							 * password
																							 * +
																							 */"]";
	}

}