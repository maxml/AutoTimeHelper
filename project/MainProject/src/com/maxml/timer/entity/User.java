package com.maxml.timer.entity;

public class User {
	private int objectId;
	private String username;
	private String email;
	private String password;

	public User(int objectId, String username, String email, String password) {
		super();
		this.objectId = objectId;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [objectId=" + objectId + ", username=" + username
				+ ", email=" + email + ", password=" + password + "]";
	}

}