package com.maxml.timer.entity;

public class Point {
	private double x;
	private double y;
	private String objectId;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point() {

	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
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

	@Override
	public String toString() {
		return "Point [x =" + getX() + " y =" + getY() + "]";
	}

}
