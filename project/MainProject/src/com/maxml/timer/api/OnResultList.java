package com.maxml.timer.api;

import java.util.ArrayList;
import java.util.List;

import com.maxml.timer.entity.Slice;
import com.parse.ParseObject;

public interface OnResultList {
	public void onResult(ArrayList<Slice> listSlice);
}
