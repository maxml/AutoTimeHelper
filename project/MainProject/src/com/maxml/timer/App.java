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
import com.maxml.timer.util.NetworkStatus;
import com.parse.Parse;
import com.parse.ParseInstallation;

public class App extends Application {


	@Override
	public void onCreate() {
		super.onCreate();

		Parse.enableLocalDatastore(this);

		Parse.initialize(this, "v7xyeLy701EXeS70aggBiMDzRIlTH0q1tiYlZqFV",
				"oxzucbjTHAKP20t9o4UjBCRBoKN0gk4jL4Mm9UjK");

		ParseInstallation.getCurrentInstallation().saveInBackground();

//		NetworkReceiver networkReceiver = new NetworkReceiver();  проект крешился на реальных устройствах, но работал на виртуальных
//		networkReceiver.onReceive(this, new Intent());
//		 
//		 NetworkStatus.isNetworkAvailable(this);
//		 Log.i("Slice",""+NetworkStatus.isConnected);
//		 
//		 
//		    Point point = new Point(999,999);
//			Line line = new Line(point,point);
//			Table table = new Table();
//			for(int i = 0; i<10; i++){
//			Slice slice = new Slice("User", line, new Date(), new Date(), "ololo" + i, SliceType.WALK);
//			table.addSlise(slice);
//			}
//			SliceCRUD sliceCRUD = new SliceCRUD();
//			sliceCRUD.sync(table);
			
//		sliceCRUD.read("User");
	}


}
