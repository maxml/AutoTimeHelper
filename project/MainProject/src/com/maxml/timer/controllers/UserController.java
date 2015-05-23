package com.maxml.timer.controllers;

import android.util.Log;

import com.maxml.timer.api.UserCRUD;
import com.maxml.timer.entity.User;

public class UserController {
	private User user;
	private UserCRUD userAPI = new UserCRUD();
	
	public void addUser(User user) {
		Log.d("User", "start controller addUser method");
		userAPI.sync(user);
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
