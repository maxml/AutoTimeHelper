package com.maxml.timer.controllers;

import java.util.List;

import android.util.Log;

import com.maxml.timer.api.SliceCRUD;
import com.maxml.timer.api.interfaces.OnResultList;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Table;
import com.maxml.timer.ui.fragments.GoogleMapFragment;
import com.parse.ParseUser;

public class TableController implements OnResultList {
	public OnResultList onResult;
	private Table table = new Table();
	private SliceCRUD sliceCRUD = new SliceCRUD();
	private GoogleMapFragment googlemapF;
	
	public void getListSlice() {
		sliceCRUD.onresultList = this;
		sliceCRUD.read(ParseUser.getCurrentUser().getObjectId());
		
	}
	
	public void addSlise(Slice slice) {
		Table tableAdd = new Table();
		if (slice.getPath().equals(null)) {
			slice.setPath(googlemapF.listLine.get(googlemapF.listLine.size() - 1));
		}
		if (slice.getStartDate().equals(null)) {
			slice.setEndDate(googlemapF.listSlice.get(googlemapF.listSlice.size() - 1).getEndDate());
			slice.setStartDate(googlemapF.listSlice.get(googlemapF.listSlice.size() - 1).getStartDate());
		}
		tableAdd.addSlise(slice);
		sliceCRUD.sync(tableAdd);
	}
	
	public void update(Slice slice) {
		Table tableUpdate = new Table();
		tableUpdate.addSlise(slice);
		sliceCRUD.sync(tableUpdate);
	}
	
	public Table getTable() {
		return table;
	}
	
	public void setTable(Table table) {
		this.table = table;
	}
	
	@Override
	public void OnResultSlices(List<Slice> sliceList) {
		for (Slice slice : sliceList) {
			Log.i("Slice", "Slice UUID: " + slice.getId());
			Log.i("Slice", "Slice updateDdat: " + slice.getUpdatedat());
			Log.i("Slice", "Slice Description: " + slice.getDescription());
			Log.i("SliceRead", " Read finish 3");
			onResult.OnResultSlices(sliceList);
		}
	}
	
	public void delete(Slice slice) {
		sliceCRUD.delete(slice.getId());
	}
	
}
