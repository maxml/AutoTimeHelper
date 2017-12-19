package com.maxml.timer.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.maxml.timer.ui.elements.ScrimInsetsFrameLayout;
import com.maxml.timer.util.Constants;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainUserPageFragment extends Fragment implements View.OnClickListener {

    private BootstrapButton bChangePicture;
    private BootstrapButton bChangeName;
    private BootstrapButton bChangeEmail;
    private BootstrapButton bOk;

    private EditText etSetName;
    private EditText etSetEmail;

    private ImageView ivUser;

    private Controller controller;
    private EventBus eventBus;
//    private UserAPI user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        user = new UserAPI(getActivity(), new Handler());
        View rootView = inflater.inflate(R.layout.activity_main_user_page, container, false);

        eventBus = new EventBus();
        controller = new Controller(getContext(), eventBus);

//        setUI(rootView);
        initUI(rootView);
        setListeners();
        return rootView;
    }

    @Subscribe
    public void onReceiveUser(User user){

    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
        controller.registerEventBus(eventBus);
    }

    @Override
    public void onStop() {
        controller.unregisterEventBus(eventBus);
        eventBus.unregister(this);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

//        if (id == R.id.b_email) {
//            etSetEmail.setEnabled(true);
//            bOk.setVisibility(View.VISIBLE);
//        } else if (id == R.id.b_name) {
//            etSetName.setEnabled(true);
//            bOk.setVisibility(View.VISIBLE);
//        } else if (id == R.id.b_change_picture) {
//            ImageManager imageManager = new ImageManager(getActivity());
//
//            Intent intent = imageManager.createIntentForLoadImage();
//            if (intent != null) {
//                getActivity().startActivityForResult(intent, Constants.REQUEST_CODE_TAKE_PHOTO);
//            }
//        } else if (id == R.id.b_ok) {
//            bOk.setVisibility(View.GONE);
//            etSetEmail.setEnabled(false);
//            etSetName.setEnabled(false);
//
//            user.updateEmail(etSetEmail.getText().toString());
//            user.updateName(etSetName.getText().toString());
//
//            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
//        }

        switch (id) {
            case R.id.b_email:
                etSetEmail.setEnabled(true);
                bOk.setVisibility(View.VISIBLE);
                break;
            case R.id.b_name:
                etSetName.setEnabled(true);
                bOk.setVisibility(View.VISIBLE);
                break;
            case R.id.b_change_picture:
                ImageManager imageManager = new ImageManager(getActivity());

                Intent intent = imageManager.createIntentForLoadImage();
                if (intent != null) {
                    getActivity().startActivityForResult(intent, Constants.REQUEST_CODE_TAKE_PHOTO);
                }
                break;
            case R.id.b_ok:
                bOk.setVisibility(View.GONE);
                etSetEmail.setEnabled(false);
                etSetName.setEnabled(false);

                controller.updateEmail(etSetEmail.getText().toString());
//                user.updateName(etSetName.getText().toString());

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
//            user.logout();
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

    private void updateUI(User user) {
        if (user == null) {
            return;
        }
        etSetEmail.setText(user.getEmail());
        etSetName.setText(user.getUsername());
        updateImage(Uri.parse(Uri.decode(user.getPhoto())));
    }


    private void initUI(View view) {
        bChangeEmail = (BootstrapButton) view.findViewById(R.id.b_email);
        bChangeName = (BootstrapButton) view.findViewById(R.id.b_name);
        bChangePicture = (BootstrapButton) view.findViewById(R.id.b_change_picture);
        bOk = (BootstrapButton) view.findViewById(R.id.b_ok);

        etSetName = (EditText) view.findViewById(R.id.tvName);
        etSetEmail = (EditText) view.findViewById(R.id.tv_email);

        ivUser = (ImageView) view.findViewById(R.id.iv_user);
    }

//    private void setUI(View view) {
//        bChangeEmail = (BootstrapButton) view.findViewById(R.id.b_email);
//        bChangeName = (BootstrapButton) view.findViewById(R.id.b_name);
//        bChangePicture = (BootstrapButton) view.findViewById(R.id.b_change_picture);
//        bOk = (BootstrapButton) view.findViewById(R.id.b_ok);
//
//        etSetName = (EditText) view.findViewById(R.id.tvName);
//        etSetEmail = (EditText) view.findViewById(R.id.tv_email);
//
//        ivUser = (ImageView) view.findViewById(R.id.iv_user);
//
//        etSetEmail.setText(user.getCurrentUser().getEmail());
//        etSetName.setText(user.getCurrentUser().getUsername());
//        updateImage(Uri.parse(Uri.decode(user.getCurrentUser().getPhoto())));
//
//        setListeners();
//    }


    private void setListeners() {
        bChangePicture.setOnClickListener(this);
        bChangeName.setOnClickListener(this);
        bChangeEmail.setOnClickListener(this);
        bOk.setOnClickListener(this);
    }

}
