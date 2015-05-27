package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.maxml.timer.R;
import com.maxml.timer.api.UserAPI;
import com.parse.ParseUser;

public class MainUserPageFragment extends Fragment {
	
	private Button btnChangePicture;
	private Button btnChangeName;
	private Button btnChangeEmail;
	private Button btnChangeOk;
	
	private EditText etSetName;
	private EditText etSetEmail;
	private UserAPI us = new UserAPI();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_main_user_page, container, false);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		btnChangeEmail = buttonsAutorisation(R.id.btnEmail);
		btnChangeName = buttonsAutorisation(R.id.btnName);
		btnChangePicture = buttonsAutorisation(R.id.btnChangePicture);
		btnChangeOk = buttonsAutorisation(R.id.btnOk);
		btnChangeOk.setVisibility(View.INVISIBLE);
		
		etSetName = editTextAutorisation(R.id.tvName);
		etSetEmail = editTextAutorisation(R.id.tvEmail);
		etSetEmail.setText(us.getCurrentUser().getEmail());
		etSetName.setText(us.getCurrentUser().getUsername());
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
						us.updateEmail(etSetEmail.getText().toString(), ParseUser.getCurrentUser()
								.getObjectId());
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
						us.updateName(etSetName.getText().toString(), ParseUser.getCurrentUser().getObjectId());
					}
				});
			}
		});
		
	}
	
	private EditText editTextAutorisation(int idintification) {
		EditText et = (EditText) getActivity().findViewById(idintification);
		return et;
	}
	
	private Button buttonsAutorisation(int idintification) {
		Button btn = (Button) getActivity().findViewById(idintification);
		return btn;
	}
	
}
