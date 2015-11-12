package userEntity;

public class Line {
	private double distance;
	private Point start;
	private Point finish;

	public Line(double distance, Point start, Point finish) {
		super();
		this.distance = distance;
		this.start = start;
		this.finish = finish;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
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
		return "Line [distance=" + distance + ", start=" + start + ", finish="
				+ finish + "]";
	}
}
