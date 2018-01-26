package com.maxml.timer.entity;

public class User {
	private String id;
	private String username;
	private String email;
	private String photoUri;
	private boolean isAnonymously;

	public User() {
		id = "";
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

	public String getPhotoUri() {
		return photoUri;
	}

	public void setPhotoUri(String photoUri) {
		this.photoUri = photoUri;
	}

	public boolean isAnonymously() {
		return isAnonymously;
	}

	public void setAnonymously(boolean anonymously) {
		isAnonymously = anonymously;
	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", username='" + username + '\'' +
				", email='" + email + '\'' +
				", photoUri='" + photoUri + '\'' +
				", isAnonymously=" + isAnonymously +
				'}';
	}
}