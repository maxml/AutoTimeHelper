package com.maxml.timer.api.interfaces;

import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;

import java.util.List;

public interface OnDbResult {

//	void onResult(ParseObject parseLine, Slice slice);
//
//	void onResult(ParseObject parsePoint, ParseObject parsePointFinish, Slice slice);

	void onResultRead(List<Slice> sliceList);

	void onResult(Point point, List<Slice> sliceList);

}
