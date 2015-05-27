package com.maxml.timer.api.interfaces;

import java.util.List;

import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.User;
import com.parse.ParseObject;

public interface OnDbResult {
<<<<<<< HEAD

	void onResult(ParseObject parseLine, Slice slice);

	void onResult(ParseObject parsePoint, ParseObject parsePointFinish, Slice slice);

	void onResultRead(List<Slice> sliceList);

	void onResult(Point point, List<Slice> sliceList);

=======
	
	void onResult(Point point, List<Slice> sliceList);
	
	void onResult(ParseObject parsePoint, Slice slice);
	
	void onResultRead(List<Slice> sliceList);
	
	void onResult(ParseObject parsePoint);
	
>>>>>>> 02b27d3eca81262aed00546e5ae8d7972d812680
}
