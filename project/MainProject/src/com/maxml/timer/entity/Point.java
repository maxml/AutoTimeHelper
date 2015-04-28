package com.maxml.timer.entity;

public class Point {
	private String User;
	private double x;
	private double y;
	private String id;
	public Point(String user, double x, double y) {
		super();
		User = user;
		this.x = x;
		this.y = y;
	}
	public String getUser() {
		return User;
	}
	public void setUser(String user) {
		User = user;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Point [User=" + User + ", x=" + x + ", y=" + y + ", id=" + id
				+ "]";
	}



}
