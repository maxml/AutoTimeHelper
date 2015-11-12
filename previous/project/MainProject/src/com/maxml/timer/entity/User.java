package com.maxml.timer.entity;

public class User {
	private String objectId;
	private String username;
	private String email;
	// TODO: we need this field?
	private String password;

	private static User instance;

	public User() {
	}

	public static User getInstance() {
		if (instance == null) {
			instance = new User();
		}
		return instance;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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