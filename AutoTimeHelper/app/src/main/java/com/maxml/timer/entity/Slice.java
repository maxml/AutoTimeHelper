package com.maxml.timer.entity;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.maxml.timer.util.SliceType;

import java.util.Comparator;
import java.util.Date;

@IgnoreExtraProperties
public class Slice {
    private String user;
    private String id;
    private Line path;
    public Date startDate;
    private Date endDate;
    private String description;
    private SliceType sliceType;
    private String lineUUID;
    private Date updateDate;
    private boolean isDeleted = false;
    private int position;
    public long sortByDate;


    public Slice(String user, Line path, Date startDate, Date endDate, String description, SliceType sliceType) {
        super();
        this.user = user;
        this.path = path;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.sliceType = sliceType;
        this.sortByDate = (int) startDate.getTime();
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

    // getter for default use
    // The Firebase data mapper will ignore this
    @Exclude
    public SliceType getType() {
        return sliceType;
    }

    // getter for Firebase
    public String getSliceType() {
        // Convert enum to string
        if (sliceType == null) {
            return null;
        } else {
            return sliceType.name();
        }
    }

    // setter for default use
    // The Firebase data mapper will ignore this
    @Exclude
    public void setType(SliceType type) {
        this.sliceType = type;
    }


    // setter for Firebase
    public void setSliceType(String sliceType) {
        // Get enum from string
        if (sliceType == null) {
            this.sliceType = null;
        } else {
            this.sliceType = SliceType.valueOf(sliceType);
        }
    }


    public String getLineUUID() {
        return lineUUID;
    }

    public void setLineUUID(String lineUUID) {
        this.lineUUID = lineUUID;
    }


    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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


    public long getSortByDate() {
        return sortByDate;
    }

    public void setSortByDate(int sortByDate) {
        this.sortByDate = sortByDate;
    }

    @Override
    public String toString() {
        return "Slice [user=" + user + ", id=" + id + ", path=" + path + ", startDate=" + startDate + ", endDate="
                + endDate + ", description=" + description + ", type=" + sliceType + ", isDeleted=" + isDeleted + "]";
    }

    public long compareTo(Slice compareSlice) {

        long compareQuantity = ((Slice) compareSlice).getStartDate().getTime();

        //ascending order
        return this.sortByDate - compareQuantity;

        //descending order
        //return compareQuantity - this.quantity;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Slice slice = (Slice) o;

        if (position != slice.position) return false;
        if (user != null ? !user.equals(slice.user) : slice.user != null) return false;
        if (path != null ? !path.equals(slice.path) : slice.path != null) return false;
        if (startDate != null ? !startDate.equals(slice.startDate) : slice.startDate != null)
            return false;
        if (endDate != null ? !endDate.equals(slice.endDate) : slice.endDate != null) return false;
        if (description != null ? !description.equals(slice.description) : slice.description != null)
            return false;
        if (sliceType != slice.sliceType) return false;
        if (lineUUID != null ? !lineUUID.equals(slice.lineUUID) : slice.lineUUID != null)
            return false;
        return updateDate != null ? updateDate.equals(slice.updateDate) : slice.updateDate == null;

    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (sliceType != null ? sliceType.hashCode() : 0);
        result = 31 * result + (lineUUID != null ? lineUUID.hashCode() : 0);
        result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
        result = 31 * result + position;
        return result;
    }

    public static Comparator<Slice> sliceComparator
            = new Comparator<Slice>() {


        @Override
        public int compare(Slice slice1, Slice slice2) {

            String sliceName1 = "" + slice1.getSortByDate();
            String sliceName2 = "" + slice2.getSortByDate();
            sliceName1.toUpperCase();
            sliceName2.toUpperCase();
//ascending order
            return sliceName2.compareTo(sliceName1);

//descending order
//return fruitName2.compareTo(fruitName1);
        }

    };

}
