package com.maxml.timer.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.maxml.timer.ImageManager;
import com.maxml.timer.R;
import com.maxml.timer.api.UserAPI;
import com.maxml.timer.controllers.Controller;
import com.maxml.timer.entity.User;
import com.maxml.timer.ui.activity.LoginActivity;
import com.maxml.timer.util.Constants;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainUserPageFragment extends Fragment implements View.OnClickListener {

    private BootstrapButton bbChangePicture;
    private BootstrapButton bbChangeName;
    private BootstrapButton bbChangeEmail;
    private BootstrapButton bbOk;

    private EditText etName;
    private EditText etEmail;

    private ImageView ivUser;

    private Controller controller;
    private EventBus eventBus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main_user_page, container, false);

        eventBus = new EventBus();
        controller = new Controller(getContext(), eventBus);

        initUI(rootView);
        setListeners();

        return rootView;
    }

    @Subscribe
    public void onReceiveUser(UserAPI userAPI) {
        updateUI(userAPI.getCurrentUser());
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
        controller.registerEventBus(eventBus);
        controller.sentUser();
    }

    @Override
    public void onStop() {
        controller.unregisterEventBus(eventBus);
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
                ImageManager imageManager = new ImageManager(getActivity());

                Intent intent = imageManager.createIntentForLoadImage();
                if (intent != null) {
                    getActivity().startActivityForResult(intent, Constants.REQUEST_CODE_TAKE_PHOTO);
                }
                break;
            case R.id.bb_ok:
                bbOk.setVisibility(View.GONE);
                etEmail.setEnabled(false);
                etName.setEnabled(false);

                controller.updateUserEmail(etEmail.getText().toString());
                controller.updateUserName(etName.getText().toString());

                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
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
            controller.logout();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateImage(Uri uri) {
        if (uri != null) {
            Picasso.with(getActivity())
                    .load(uri)
                    .into(ivUser);
        }
    }

    private void setListeners() {
        bbChangePicture.setOnClickListener(this);
        bbChangeName.setOnClickListener(this);
        bbChangeEmail.setOnClickListener(this);
        bbOk.setOnClickListener(this);
    }

    private void initUI(View view) {
        bbChangeEmail = (BootstrapButton) view.findViewById(R.id.bb_email);
        bbChangeName = (BootstrapButton) view.findViewById(R.id.bb_name);
        bbChangePicture = (BootstrapButton) view.findViewById(R.id.bb_change_picture);
        bbOk = (BootstrapButton) view.findViewById(R.id.bb_ok);

        etName = (EditText) view.findViewById(R.id.bet_name);
        etEmail = (EditText) view.findViewById(R.id.bet_email);

        ivUser = (ImageView) view.findViewById(R.id.iv_user);
    }

    private void updateUI(User user) {
        if (user == null) {
            return;
        }
        etEmail.setText(user.getEmail());
        etName.setText(user.getUsername());
        updateImage(Uri.parse(user.getPhoto()));
    }
}
