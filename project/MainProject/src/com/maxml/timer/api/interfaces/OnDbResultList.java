package com.maxml.timer.api.interfaces;

import java.util.ArrayList;

import com.maxml.timer.entity.Slice;

public interface OnDbResultList {
	public void onResult(ArrayList<Slice> listSlice);
}
