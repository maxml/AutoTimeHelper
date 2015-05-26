package com.maxml.timer.api.interfaces;

import java.util.List;

import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.User;
import com.parse.ParseObject;

public interface OnDbResult {
	
	void onResult(Point point, List<Slice> sliceList);
	
	void onResult(ParseObject parsePoint, Slice slice);
	
	void onResultRead(List<Slice> sliceList);
	
	void onResult(ParseObject parsePoint);
	
}
