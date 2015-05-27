package com.maxml.timer.api.interfaces;

import java.util.List;

import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.parse.ParseObject;

public interface OnDbResult {

	void onResult(ParseObject parseLine, Slice slice);

	void onResult(ParseObject parsePoint, ParseObject parsePointFinish, Slice slice);

	void onResultRead(List<Slice> sliceList);

	void onResult(Point point, List<Slice> sliceList);

}
