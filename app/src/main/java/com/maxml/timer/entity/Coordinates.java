package com.maxml.timer.entity;

import com.google.firebase.database.Exclude;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.maxml.timer.database.CoordinateDAO;
import com.maxml.timer.util.Constants;

import java.util.Date;

@DatabaseTable(tableName = Constants.TABLE_COORDINATES, daoClass = CoordinateDAO.class)
public class Coordinates {
    @DatabaseField(canBeNull = false, generatedId = true)
    private long id;
    @DatabaseField(dataType = DataType.DATE)
    private Date date;
    @DatabaseField(dataType = DataType.DOUBLE)
    private double latitude;
    @DatabaseField(dataType = DataType.DOUBLE)
    private double longitude;
    @DatabaseField(foreign = true)
    private Path path;

    public Coordinates() {
    }

    public static double getDistanceInMeter(Coordinates c1, Coordinates c2) {
        double theta = c1.getLongitude() - c2.getLongitude();
        double dist = Math.sin(degToRad(c1.getLatitude())) * Math.sin(degToRad(c2.getLatitude()))
                + Math.cos(degToRad(c1.getLatitude())) * Math.cos(degToRad(c2.getLatitude())) * Math.cos(degToRad(theta));
        dist = Math.acos(dist);
        dist = radToDeg(dist);
        dist = dist * 1.609344 * 1000;
        return (Math.abs(dist));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double firstLat) {
        this.latitude = firstLat;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double firstLong) {
        this.longitude = firstLong;
    }

    @Exclude
    private static double degToRad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    @Exclude
    private static double radToDeg(double rad) {
        return (rad * 180 / Math.PI);
    }

    @Exclude
    private double toRadians(double a) {
        Math.toRadians(a);
        return 0;
    }
}