package com.maxml.timer.entity;

public class Line {
	private Point start;
	private Point finish;

	public Line(Point start, Point finish) {
		super();
		this.start = start;
		this.finish = finish;
	}

	public double getDistance() {
		return Math.sqrt(Math.pow(start.getX() - finish.getX(), 2)
				+ Math.pow(start.getY() - finish.getY(), 2));
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

	@Override
	public String toString() {
		return "Line [distance=" + getDistance() + ", start=" + start + ", finish="
				+ finish + "]";
	}
}
