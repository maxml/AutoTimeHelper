package com.maxml.timer.api;

import java.util.ArrayList;

import com.maxml.timer.entity.Slice;

public interface OnResultList {
	public void onResult(ArrayList<Slice> listSlice);
}
