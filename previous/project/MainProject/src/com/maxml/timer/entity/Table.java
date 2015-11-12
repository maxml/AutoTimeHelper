package com.maxml.timer.entity;

import java.util.ArrayList;

public class Table {
	private ArrayList<Slice> list;

	public Table() {
		super();
		this.list = new ArrayList<Slice>();
	}

	public void addSlise(Slice slice) {
		list.add(slice);
	}

	public ArrayList<Slice> getList() {
		return list;
	}

	public void setList(ArrayList<Slice> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "Table [list=" + list + "]";
	}

}
