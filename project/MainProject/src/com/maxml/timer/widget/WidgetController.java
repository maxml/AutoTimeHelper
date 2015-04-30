package com.maxml.timer.widget;

import java.util.ArrayList;

import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Table;

public class WidgetController {
	// public static Slice sliseStart;
	// public static Slice sliseNext;
	//
	// public static SliceType type;
	private ArrayList<Slice> list = new ArrayList<Slice>();
	private Table table = new Table(list);
	

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	};

}
