package com.maxml.timer.SliceControllers;

import com.maxml.timer.api.SliceCRUD;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Table;

public class Controller {
	private Table table = new Table();
	private SliceCRUD sCRUD = new SliceCRUD();

	public void addSlise(Slice slice) {
		table.addSlise(slice);
		sCRUD.sinc(table);
	}
	
	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	};

}
