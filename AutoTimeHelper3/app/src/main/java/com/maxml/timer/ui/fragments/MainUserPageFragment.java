package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.maxml.timer.R;
import com.maxml.timer.api.UserAPI;

public class MainUserPageFragment extends Fragment {

    private BootstrapButton btnChangePicture;
    private BootstrapButton btnChangeName;
    private BootstrapButton btnChangeEmail;
    private BootstrapButton btnChangeOk;

    private EditText etSetName;
    private EditText etSetEmail;
    private UserAPI us;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        us= new UserAPI(getActivity(), new Handler());
        return inflater.inflate(R.layout.activity_main_user_page, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnChangeEmail = (BootstrapButton) getActivity().findViewById(R.id.btnEmail);
        btnChangeName = buttonsAuthorisation(R.id.btnName);
        btnChangePicture = buttonsAuthorisation(R.id.btnChangePicture);
        btnChangeOk = buttonsAuthorisation(R.id.btnOk);

        etSetName = editTextAuthorisation(R.id.tvName);
        etSetEmail = editTextAuthorisation(R.id.tvEmail);
        etSetEmail.setText(us.getCurrentUser().getEmail());
        etSetName.setText(us.getCurrentUser().getUsername());
        etSetName.setEnabled(false);
        etSetEmail.setEnabled(false);


//		btnChangeEmail.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
////				etSetEmail.setEnabled(true);
////				btnChangeOk.setVisibility(View.VISIBLE);
//				btnChangeOk.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
////						etSetEmail.setEnabled(false);
////						btnChangeOk.setVisibility(View.INVISIBLE);
//						us.updateEmail(etSetEmail.getText().toString(), ParseUser.getCurrentUser()
//								.getId());
//					}
//				});
//			}
//		});
//		
//		btnChangeName.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				etSetName.setEnabled(true);
//				btnChangeOk.setVisibility(View.VISIBLE);
//				btnChangeOk.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						etSetName.setEnabled(false);
//						btnChangeOk.setVisibility(View.INVISIBLE);
//						us.updateName(etSetName.getText().toString(), ParseUser.getCurrentUser().getId());
//					}
//				});
//			}
//		});
//		
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEmail:
                //
                etSetEmail.setEnabled(true);
                btnChangeOk.setVisibility(View.VISIBLE);
                break;
            case R.id.btnName:
                //
                etSetName.setEnabled(true);
                break;

            case R.id.btnChangePicture:
                //
                break;

            case R.id.btnOk:
                //
                etSetEmail.setEnabled(false);
//                us.updateEmail(etSetEmail.getText().toString(), ParseUser.getCurrentUser()
//                        .getId());
//                us.updateName(etSetName.getText().toString(), ParseUser.getCurrentUser().getId());
                break;
        }
    }

    private EditText editTextAuthorisation(int idintification) {
        EditText et = (EditText) getActivity().findViewById(idintification);
        return et;
    }

    private BootstrapButton buttonsAuthorisation(int idintification) {
        BootstrapButton btn = (BootstrapButton) getActivity().findViewById(idintification);
        return btn;
    }

}
