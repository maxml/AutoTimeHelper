package com.maxml.timer.entity;

public class Line {
	private Point start;
	private Point finish;
	private String user;
	private String id;
	private String startUUID;
	private String finishUUID;
	
	public Line(Point start, Point finish) {
		this.start = start;
		this.finish = finish;
	}

	public Line(Point start, Point finish, String user) {
		this.start = start;
		this.finish = finish;
		this.user = user;
	}

	public Line(Point start, Point finish, String user, String id) {
		this.start = start;
		this.finish = finish;
		this.id = id;
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
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
	
	

	public String getStartUUID() {
		return startUUID;
	}

	public void setStartUUID(String startUUID) {
		this.startUUID = startUUID;
	}

	public String getFinishUUID() {
		return finishUUID;
	}

	public void setFinishUUID(String finishUUID) {
		this.finishUUID = finishUUID;
	}

	@Override
	public String toString() {
		return "Line [User =" + getUser() + " id =" + getId() + " ; " + " distance=" + getDistance() + ", start="
				+ start + ", finish=" + finish + "]";
	}
}
