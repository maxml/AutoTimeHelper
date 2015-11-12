package com.maxml.timer.entity;

public class Coordinates {
	private double latitude;
	private double longtitude;

	public static double getDistanceInMeter(Coordinates c1, Coordinates c2) {
		double theta = c1.getLong() - c2.getLong();
		double dist = Math.sin(degTOrad(c1.getLat())) * Math.sin(degTOrad(c2.getLat()))
				+ Math.cos(degTOrad(c1.getLat())) * Math.cos(degTOrad(c2.getLat())) * Math.cos(degTOrad(theta));
		dist = Math.acos(dist);
		dist = radTOdeg(dist);
		dist = dist * 1.609344 * 1000;
		return (Math.abs(dist));
	}

	public double getLat() {
		return latitude;
	}

	public void setLat(double firstLat) {
		this.latitude = firstLat;
	}

	public double getLong() {
		return longtitude;
	}

	public void setLong(double firstLong) {
		this.longtitude = firstLong;
	}

	private static double degTOrad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double radTOdeg(double rad) {
		return (rad * 180 / Math.PI);
	}

	private double toRadians(double a) {
		Math.toRadians(a);
		return 0;
	}
}