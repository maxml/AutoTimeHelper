package com.maxml.timer.controllers;

import java.util.List;

import android.util.Log;

import com.maxml.timer.api.SliceCRUD;
import com.maxml.timer.api.interfaces.OnResultList;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Table;

public class TableController implements OnResultList {
	private Table table = new Table();
	private SliceCRUD sliceCRUD = new SliceCRUD();

	public void getListSlice(String userID) {
		sliceCRUD.onresultList = this;
		sliceCRUD.read(userID);

	}

	public void addSlise(Slice slice) {
		Table tableAdd = new Table();
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
	public void OnResultSlices(List<Slice> listSlice) {
		for (Slice slice : listSlice) {
			Log.i("Slice", "Slice UUID: " + slice.getId());
			Log.i("Slice", "Slice updateDdat: " + slice.getUpdatedat());
			Log.i("Slice", "Slice Description: " + slice.getDescription());
		}
	}

}
