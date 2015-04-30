package com.maxml.timer.googlemap;

public class Coordinates {
	private double Lat;
	private double Long;

	public double getDistance(Coordinates coordinats) {
		double distance = 0;
		// double φ1 = toRadians(this.Lat);
		// double φ2 = toRadians(coordinats.Lat);
		// double Δφ = toRadians((coordinats.Lat - this.Lat));
		// double Δλ = toRadians((coordinats.Long - this.Long));
		//
		// double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) + Math.cos(φ1)
		// * Math.cos(φ2) * Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
		// double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		// double R = 6378100;
		// distance = R * c;
		return distance;
	}

	private double toRadians(double a) {
		Math.toRadians(a);
		return 0;
	}

	public double getLat() {
		return Lat;
	}

	public void setLat(double firstLat) {
		this.Lat = firstLat;
	}

	public double getLong() {
		return Long;
	}

	public void setLong(double firstLong) {
		this.Long = firstLong;
	}
}