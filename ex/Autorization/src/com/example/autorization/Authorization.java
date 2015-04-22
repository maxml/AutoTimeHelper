package com.example.autorization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Authorization extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        
	}
	
	public void onclick(View v){
		switch(v.getId()){
		case R.id.btnLogin:
			break;
		case R.id.btnSignIn:
			 Intent intent = new Intent(this, CreateUserActivity.class);
             startActivityForResult(intent, 1);
		}
	}
}
