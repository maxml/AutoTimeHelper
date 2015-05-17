package com.maxml.timer.api.interfaces;

import com.maxml.timer.entity.Slice;
import com.parse.ParseObject;

public interface OnDbResult {

	void onResult(ParseObject object);

	void onResult(ParseObject pair, Slice slice);

	

}
