package com.maxml.timer.controllers;

import android.os.Handler;
import android.util.Log;

import com.maxml.timer.api.SliceCRUD;
import com.maxml.timer.api.interfaces.OnResultList;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Table;
import com.maxml.timer.ui.fragments.GoogleMapFragment;

import java.util.Date;
import java.util.List;

public class TableController implements OnResultList {
	public OnResultList onResult;
	private Table table;
	private SliceCRUD sliceCRUD;
	private GoogleMapFragment googlemapF;


	public TableController(Handler handler) {
		sliceCRUD = new SliceCRUD(handler);
		table = new Table();
	}

	// // TODO: 13.09.17 update all calling with new constructor
	public TableController() {
	}

	public void getListSlice() {
		sliceCRUD.onresultList = this;
//		sliceCRUD.read(ParseUser.getCurrentUser().getObjectId());
	}
	
	public void addSlice(Slice slice) {
		Table tableAdd = new Table();
		if (slice.getPath().equals(null)) {
			slice.setPath(googlemapF.listLine.get(googlemapF.listLine.size() - 1));
		}
		if (slice.getStartDate().equals(null)) {
			slice.setEndDate(googlemapF.listSlice.get(googlemapF.listSlice.size() - 1).getEndDate());
			slice.setStartDate(googlemapF.listSlice.get(googlemapF.listSlice.size() - 1).getStartDate());
		}
		tableAdd.addSlice(slice);
		sliceCRUD.sync(tableAdd);
	}
	
	public void update(Slice slice) {
		Table tableUpdate = new Table();
		tableUpdate.addSlice(slice);
		sliceCRUD.sync(tableUpdate);
	}
	
	public Table getTable() {
		return table;
	}
	
	public Table getTable(Date date) {
		// // TODO: 13.09.17  
		return table;
	}
	
	public void setTable(Table table) {
		this.table = table;
	}
	
	@Override
	public void OnResultSlices(List<Slice> sliceList) {
		for (Slice slice : sliceList) {
			Log.i("Slice", "Slice UUID: " + slice.getId());
			Log.i("Slice", "Slice updateDdat: " + slice.getUpdateDate());
			Log.i("Slice", "Slice Description: " + slice.getDescription());
			Log.i("SliceRead", " Read finish 3");
			onResult.OnResultSlices(sliceList);
		}
	}
	
	public void delete(Slice slice) {
		sliceCRUD.delete(slice.getId());
	}
	
}