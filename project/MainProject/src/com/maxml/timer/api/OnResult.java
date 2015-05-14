package com.maxml.timer.api;

import com.maxml.timer.entity.Slice;
import com.parse.ParseObject;

public interface OnResult {

	void onResult(ParseObject object);

	void onResult(ParseObject pair, Slice slice);

	

}
