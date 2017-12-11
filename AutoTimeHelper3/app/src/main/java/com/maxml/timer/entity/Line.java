package com.maxml.timer.entity;

public class Line {
	private Point start;
	private Point finish;
	private String id;
	private boolean isDelete =false;
	
	public Line(Point start, Point finish) {
		this.start = start;
		this.finish = finish;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Line() {
	}

	public double getDistance() {
		return Math.sqrt(Math.pow(start.getX() - finish.getX(), 2) + Math.pow(start.getY() - finish.getY(), 2));
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public Point getFinish() {
		return finish;
	}

	public void setFinish(Point finish) {
		this.finish = finish;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean delete) {
		isDelete = delete;
	}

}
