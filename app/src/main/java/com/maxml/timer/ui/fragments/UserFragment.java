package com.maxml.timer.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Events;
import com.maxml.timer.entity.ShowProgressListener;
import com.maxml.timer.R;
import com.maxml.timer.entity.User;
import com.maxml.timer.ui.activity.LoginActivity;
import com.maxml.timer.ui.dialog.DialogFactory;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.ImageUtil;
import com.maxml.timer.util.NetworkUtil;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class UserFragment extends Fragment implements View.OnClickListener {

    private BootstrapButton bbOk;
    private EditText etName;
    private EditText etEmail;
    private ImageView ivUser;
    private Toolbar toolbar;

    private DbController dbController;
    private EventBus eventBus;

    private ShowProgressListener progressListener;
    private Uri imageUri;

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
            case R.id.iv_user:
                UserFragmentPermissionsDispatcher.showWithPermissionCheck(UserFragment.this);
                break;
            case R.id.bb_ok:
                dbController.updateUserEmail(etEmail.getText().toString());
                dbController.updateUserName(etName.getText().toString());

                showProgress();
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
            DialogFactory.messageDialog(getContext(), getResources().getString(R.string.dialog_title_logout),
                    getResources().getString(R.string.dialog_message_logout), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbController.logout();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        }
                    }).show();
        }
        return super.onOptionsItemSelected(item);
    }


    @NeedsPermission({android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void show() {
        showDialog();
    }

    @OnShowRationale({android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationale(final PermissionRequest request) {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.permission_camera_rationale)
                .setPositiveButton(R.string.permission_button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.permission_button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.cancel();
                    }
                })
                .show();
    }

    @OnPermissionDenied({android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDenied() {
        Toast.makeText(getActivity(), R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAsk() {
        Toast.makeText(getActivity(), R.string.permission_neverask, Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        UserFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void showDialog() {
        Context context = getContext();
        if (context != null) {
            new MaterialDialog.Builder(context)
                    .items(getString(R.string.createhomesecond_gallery), getString(R.string.createhomesecond_camera))
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                            switch (position) {
                                case 0:
                                    openGallery();
                                    break;
                                case 1:
                                    openCamera();
                                    break;
                            }
                        }
                    }).show();
        }
    }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, Constants.ACTION_SELECT_IMAGE);
    }

    private void openCamera() {
        ContentValues value = new ContentValues();
        value.put(MediaStore.Images.Media.TITLE, "IMG");
        value.put(MediaStore.Images.Media.DESCRIPTION, "Camera");
        imageUri = getActivity().getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, Constants.ACTION_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == Constants.ACTION_SELECT_IMAGE) {
            imageUri = data.getData();
        }
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(imageUri, filePathColumn, null, null, null);
        int columnIndex;
        if (cursor != null) {
            columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            cursor.moveToFirst();
            imageUri = Uri.parse(cursor.getString(columnIndex));
            cursor.close();
        }
        if (imageUri != null || !TextUtils.isEmpty(imageUri.toString())) {
            Bitmap bitmap = ImageUtil.decodeSampledBitmapFromResource(imageUri.toString(), 300, 300);
            ivUser.setImageBitmap(bitmap);
            dbController.saveImage(bitmap);
            showProgress();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toolbar.setTitle(R.string.app_name);
    }

    @Subscribe
    public void onReceiveImageLoated(String url) {
        dbController.updateUserPhoto(url);
        progressListener.hideProgressBar();
        updateImage(Uri.parse(url));
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
        etName.setEnabled(false);
        etEmail.setEnabled(false);
        bbOk.setEnabled(false);
    }

    private void setListeners() {
        bbOk.setOnClickListener(this);
        ivUser.setOnClickListener(this);
    }

    private void initUI(View view) {
        bbOk = view.findViewById(R.id.bb_ok);
        etName = view.findViewById(R.id.bet_name);
        etEmail = view.findViewById(R.id.bet_email);
        ivUser = view.findViewById(R.id.iv_user);
        toolbar = getActivity().findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.user);
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
        Picasso.with(getActivity())
                .load(uri.toString())
                .into(ivUser);
    }

    private void showProgress() {
        if (NetworkUtil.isNetworkAvailable(getContext())) {
            progressListener.showProgressBar();
        }
    }
}