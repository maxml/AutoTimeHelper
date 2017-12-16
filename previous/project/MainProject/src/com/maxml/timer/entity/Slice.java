package com.maxml.timer.entity;

import java.util.Comparator;
import java.util.Date;

public class Slice {
	private String user;
	private String id;
	private Line path;
	public Date startDate;
	private Date endDate;
	private String description;
	private SliceType type;
	private String lineUUID;
	private Date updatedat;
	private boolean isDeleted = false;
	private int position;
	public long sortByDate;

	public enum SliceType {
		WORK, REST, CALL, WALK
	}

	public Slice(String user, Line path, Date startDate, Date endDate, String description, SliceType type) {
		super();
		this.user = user;
		this.path = path;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.type = type;
		this.sortByDate = (int) startDate.getTime() ;
	}

	public Slice() {

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
		this.sortByDate = startDate.getTime();
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
	

	public String getLineUUID() {
		return lineUUID;
	}

	public void setLineUUID(String lineUUID) {
		this.lineUUID = lineUUID;
	}
	

	public Date getUpdatedat() {
		return updatedat;
	}

	public void setUpdatedat(Date updatedat) {
		this.updatedat = updatedat;
	}
	

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	

	public  long getSortByDate() {
		return sortByDate;
	}

	public  void setSortByDate(int sortByDate) {
		this.sortByDate = sortByDate;
	}

	@Override
	public String toString() {
		return "Slice [user=" + user + ", id=" + id + ", path=" + path + ", startDate=" + startDate + ", endDate="
				+ endDate + ", description=" + description + ", type=" + type + ", isDeleted=" + isDeleted + "]";
	}
	
	public long compareTo(Slice compareSlice) {
		 
		long compareQuantity = ((Slice) compareSlice).getStartDate().getTime(); 
 
		//ascending order
		return this.sortByDate - compareQuantity;
 
		//descending order
		//return compareQuantity - this.quantity;
 
	}	
	
	public static Comparator<Slice> sliceComparator 
    = new Comparator<Slice>() {



@Override
public int compare(Slice slice1, Slice slice2) {

String sliceName1 = ""+slice1.getSortByDate();
String sliceName2 = ""+slice2.getSortByDate();
sliceName1.toUpperCase();
sliceName2.toUpperCase();
//ascending order
return sliceName2.compareTo(sliceName1);

//descending order
//return fruitName2.compareTo(fruitName1);
}

};

}
