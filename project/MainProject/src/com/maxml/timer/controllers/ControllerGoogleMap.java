package com.maxml.timer.controllers;

import java.util.Date;

import android.util.Log;

import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;

public class ControllerGoogleMap {
	
	private Slice googleMapSlice;
	private TableController controller = new TableController();
	private Point start = new Point();
	private Date date = new Date();
	
	// TODO: Must be in TableController - move!
	public void addSlise(Slice slice) {
		if (googleMapSlice != null) {
			Log.d("DERMO", "googleMapSlice not null");
			if (googleMapSlice.getType() == slice.getType()) {
				// finish.setX(slice.getPath().getFinish().getX());
				// finish.setY(slice.getPath().getFinish().getY());
				// date = slice.getEndDate();
			} else {
				update(googleMapSlice);
				start.setX(slice.getPath().getStart().getX());
				start.setY(slice.getPath().getStart().getY());
				date = slice.getStartDate();
				// controller.addSlise(slice);
			}
		} else {
			Log.d("DERMO", "googleMapSlice null");
			// controller.addSlise(slice);
			start.setX(slice.getPath().getStart().getX());
			start.setY(slice.getPath().getStart().getY());
			date = slice.getStartDate();
			Log.d("DERMO", "set start: " + start.toString());
		}
		this.googleMapSlice = slice;
	}
	
	private void update(Slice slice) {
		Log.d("DERMO", "start = " + start.toString());
		if (date != null) {
			slice.setStartDate(date);
			slice.getPath().setStart(start);
		}
		controller.addSlise(slice);
	}
}
