package com.maxml.timer;

import java.util.Date;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.maxml.timer.api.SliceCRUD;
import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Slice.SliceType;
import com.maxml.timer.entity.Table;
import com.maxml.timer.receivers.NetworkReceiver;
import com.parse.Parse;
import com.parse.ParseInstallation;

public class App extends Application {
	
	public static boolean isNetworkAvailable = false ;

	@Override
	public void onCreate() {
		super.onCreate();

		Parse.enableLocalDatastore(this);

		Parse.initialize(this, "v7xyeLy701EXeS70aggBiMDzRIlTH0q1tiYlZqFV", "oxzucbjTHAKP20t9o4UjBCRBoKN0gk4jL4Mm9UjK");

		ParseInstallation.getCurrentInstallation().saveInBackground();
		
		NetworkReceiver networkReceiver = new NetworkReceiver();
		networkReceiver.onReceive(this,new Intent());
		isNetworkAvailable =  networkReceiver.isNetworkAvailable(this);
		Log.i("Slice", " isNetworkAvailable is  " + isNetworkAvailable );
		test();
	}
	
	public void test(){
		SliceCRUD sliceCRUD = new SliceCRUD();
		
		Table table = new Table();
		for(int i = 0; i<5; i++){
		Point point = new Point(i,i);
		Line line = new Line(point,point);
		
		Slice slice = new Slice("Lantar",line,new Date(),new Date(), "Descriprion "+i, SliceType.WALK);
		table.addSlise(slice);
		}
		sliceCRUD.sync(table);
	
//			sliceCRUD.read("Lantar");
		
		
		
	}
	
}
