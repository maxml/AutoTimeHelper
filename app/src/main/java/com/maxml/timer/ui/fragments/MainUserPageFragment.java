package com.maxml.timer.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Events;
import com.maxml.timer.entity.ShowProgressListener;
import com.maxml.timer.util.ImageUtil;
import com.maxml.timer.R;
import com.maxml.timer.entity.User;
import com.maxml.timer.ui.activity.LoginActivity;
import com.maxml.timer.util.Constants;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

public class MainUserPageFragment extends Fragment implements View.OnClickListener {

    private BootstrapButton bbChangePicture;
    private BootstrapButton bbChangeName;
    private BootstrapButton bbChangeEmail;
    private BootstrapButton bbOk;
    private EditText etName;
    private EditText etEmail;
    private ImageView ivUser;

    private DbController dbController;
    private EventBus eventBus;

    private ShowProgressListener progressListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowProgressListener) {
            progressListener = (ShowProgressListener) context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main_user_page, container, false);

        registerEventBus();
        initUI(rootView);
        setListeners();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
        dbController.registerEventBus(eventBus);

        dbController.getCurrentUser();
    }

    @Override
    public void onStop() {
        dbController.unregisterEventBus(eventBus);
        eventBus.unregister(this);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bb_email:
                etEmail.setEnabled(true);
                bbOk.setVisibility(View.VISIBLE);
                break;
            case R.id.bb_name:
                etName.setEnabled(true);
                bbOk.setVisibility(View.VISIBLE);
                break;
            case R.id.bb_change_picture:

                Intent intent = ImageUtil.createIntentForLoadImage(getActivity());
                if (intent != null) {
                    getActivity().startActivityForResult(intent, Constants.REQUEST_CODE_TAKE_PHOTO);
                }
                break;
            case R.id.bb_ok:
                bbOk.setVisibility(View.GONE);
                etEmail.setEnabled(false);
                etName.setEnabled(false);

                dbController.updateUserEmail(etEmail.getText().toString());
                dbController.updateUserName(etName.getText().toString());

                progressListener.showProgressBar();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_page_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.i_log_out) {
            dbController.logout();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onReceiveUser(User user) {
        if (!user.isAnonymously()) {
            updateUI(user);
        } else {
            disableAccessToUI();
        }
    }

    @Subscribe
    public void onDatabaseEvent(Events.DbResult event) {
        switch (event.getResultStatus()) {
            case Constants.EVENT_DB_RESULT_OK:
                progressListener.hideProgressBar();
                break;
            case Constants.EVENT_DB_RESULT_ERROR:
                progressListener.hideProgressBar();
                dbController.getCurrentUser();
                break;
        }
    }

    private void registerEventBus() {
        eventBus = new EventBus();
        dbController = new DbController(getContext(), eventBus);
    }

    public void updateUI() {
        dbController.getCurrentUser();
    }


    private void disableAccessToUI() {
        bbChangeEmail.setEnabled(false);
        bbChangeName.setEnabled(false);
        bbChangePicture.setEnabled(false);
        bbOk.setEnabled(false);
    }

    private void setListeners() {
        bbChangePicture.setOnClickListener(this);
        bbChangeName.setOnClickListener(this);
        bbChangeEmail.setOnClickListener(this);
        bbOk.setOnClickListener(this);
    }

    private void initUI(View view) {
        bbChangeEmail = view.findViewById(R.id.bb_email);
        bbChangeName = view.findViewById(R.id.bb_name);
        bbChangePicture = view.findViewById(R.id.bb_change_picture);
        bbOk = view.findViewById(R.id.bb_ok);

        etName = view.findViewById(R.id.bet_name);
        etEmail = view.findViewById(R.id.bet_email);

        ivUser = view.findViewById(R.id.iv_user);
    }

    private void updateUI(User user) {
        if (user == null) {
            return;
        }
        etEmail.setText(user.getEmail());
        etName.setText(user.getUsername());
        updateImage(Uri.parse(user.getPhoto()));
    }

    private void updateImage(Uri uri) {
        if (uri != null) {
            if (uri.toString().contains("content")) {
                Picasso.with(getActivity())
                        .load(uri)
                        .into(ivUser);
            } else {
                File file = new File(uri.toString());

                Picasso.with(getActivity())
                        .load(file)
                        .into(ivUser);
            }
        }
    }
}