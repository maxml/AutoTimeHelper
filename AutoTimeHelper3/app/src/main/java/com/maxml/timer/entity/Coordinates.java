package com.maxml.timer.entity;

import com.google.firebase.database.Exclude;

import java.util.Date;

public class Coordinates {
	private Date date;
	private double latitude;
	private double longitude;

	public Coordinates() {
	}

	public static double getDistanceInMeter(Coordinates c1, Coordinates c2) {
		double theta = c1.getLongitude() - c2.getLongitude();
		double dist = Math.sin(degTOrad(c1.getLatitude())) * Math.sin(degTOrad(c2.getLatitude()))
				+ Math.cos(degTOrad(c1.getLatitude())) * Math.cos(degTOrad(c2.getLatitude())) * Math.cos(degTOrad(theta));
		dist = Math.acos(dist);
		dist = radTOdeg(dist);
		dist = dist * 1.609344 * 1000;
		return (Math.abs(dist));
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double firstLat) {
		this.latitude = firstLat;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double firstLong) {
		this.longitude = firstLong;
	}

	@Exclude
	private static double degTOrad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	@Exclude
	private static double radTOdeg(double rad) {
		return (rad * 180 / Math.PI);
	}

	@Exclude
	private double toRadians(double a) {
		Math.toRadians(a);
		return 0;
	}
}