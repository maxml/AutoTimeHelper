package com.maxml.timer;

import com.parse.Parse;
import com.parse.ParseInstallation;

import android.app.Application;

public class App extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}
	
	private void init(){
		Parse.enableLocalDatastore(this);
		
		Parse.initialize(this, "v7xyeLy701EXeS70aggBiMDzRIlTH0q1tiYlZqFV",
				"oxzucbjTHAKP20t9o4UjBCRBoKN0gk4jL4Mm9UjK");
		
		ParseInstallation.getCurrentInstallation().saveInBackground();
	}
}
