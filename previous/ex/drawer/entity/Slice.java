package userEntity;

import java.util.Date;

public class Slice {
	private String id;
	private Line path;
	private Date startDate;
	private Date endDate;
	private String description;
	private SliceType type;

	enum SliceType {
		WORK, REST, CALL, WALK
	}

	public Slice(String id, Line path, Date startDate, Date endDate,
			String description, SliceType type) {
		super();
		this.id = id;
		this.path = path;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Line getPath() {
		return path;
	}

	public void setPath(Line path) {
		this.path = path;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SliceType getType() {
		return type;
	}

	public void setType(SliceType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Slice [id=" + id + ", path=" + path + ", startDate="
				+ startDate + ", endDate=" + endDate + ", description="
				+ description + ", type=" + type + "]";
	}

}
