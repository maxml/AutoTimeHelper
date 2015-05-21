package com.maxml.timer.controllers;

import com.maxml.timer.api.SliceCRUD;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Table;

public class TableController {
	private Table table = new Table();
	private SliceCRUD sliceCRUD = new SliceCRUD();

	public void addSlise(Slice slice) {
		table.addSlise(slice);
		sliceCRUD.sync(table);
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	};

}
