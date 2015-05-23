package com.maxml.timer.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maxml.timer.R;

public class MainUserPageActivity extends Activity {
	
	private Button btnChangePicture;
	private Button btnChangeName;
	private Button btnChangeEmail;
	private Button btnChangeOk;
	
	private EditText etSetName;
	private EditText etSetEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_user_page);
		
		btnChangeEmail = buttonsAutorisation(R.id.btnEmail);
		btnChangeName = buttonsAutorisation(R.id.btnName);
		btnChangePicture = buttonsAutorisation(R.id.btnChangePicture);
		btnChangeOk = buttonsAutorisation(R.id.btnOk);
		btnChangeOk.setVisibility(View.INVISIBLE);
		
		etSetName = editTextAutorisation(R.id.tvName);
		etSetEmail = editTextAutorisation(R.id.tvEmail);
		etSetName.setEnabled(false);
		etSetEmail.setEnabled(false);
		
		btnChangeEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				etSetEmail.setEnabled(true);
				btnChangeOk.setVisibility(View.VISIBLE);
				btnChangeOk.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						etSetEmail.setEnabled(false);
						btnChangeOk.setVisibility(View.INVISIBLE);
					}
				});
			}
		});
		
	btnChangeName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				etSetName.setEnabled(true);
				btnChangeOk.setVisibility(View.VISIBLE);
				btnChangeOk.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						etSetName.setEnabled(false);
						btnChangeOk.setVisibility(View.INVISIBLE);
					}
				});
			}
		});
		
	}
	
	private EditText editTextAutorisation(int idintification) {
		EditText et = (EditText) findViewById(idintification);
		return et;
	}
	
	private Button buttonsAutorisation(int idintification) {
		Button btn = (Button) findViewById(idintification);
		return btn;
	}
	
}
