package com.maxml.timer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxml.timer.api.UserAPI;
import com.maxml.timer.controllers.GeneralService;
import com.maxml.timer.entity.User;
import com.maxml.timer.ui.fragments.CalendarFragment;
import com.maxml.timer.ui.fragments.HomeFragment;
import com.maxml.timer.ui.fragments.MainUserPageFragment;
import com.maxml.timer.ui.fragments.SettingsFragment;
import com.maxml.timer.ui.fragments.ActionListViewFragment;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.FragmentUtils;
import com.maxml.timer.util.SharedPrefUtils;
import com.maxml.timer.util.Utils;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FRAGMENT_TAG = "CURRENT_FRAGMENT";

    private DrawerLayout drawerLayout;
    private ProgressBar progressBar;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    private User user;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        hideProgressBar();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        initDrawer();
        if (savedInstanceState == null)
            setupFragment(new ActionListViewFragment());
//        initService();

        setHomeFragment();
    }

    private void setHomeFragment() {
        FragmentUtils.setFragment(this, new HomeFragment());
    }

//    private void initService() {
//        // if service not instant yet, start one
//        if (!Utils.isServiceRunning(this, GeneralService.class)) {
//            MyLog.d("start new service instance");
//            Intent serviceIntent = new Intent(this, GeneralService.class);
//            startService(serviceIntent);
//        }
//    }

    private void initDrawer() {
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                initDrawerHeader();
            }
        };

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerLayout.setStatusBarBackground(R.color.primary_dark);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void initDrawerHeader() {

        NavigationView nv = (NavigationView) findViewById(R.id.navigationView);
        View header = nv.getHeaderView(0);

        TextView name = (TextView) header.findViewById(R.id.user_name);
        ImageView icon = (ImageView) header.findViewById(R.id.profile_image);

        nv.setNavigationItemSelectedListener(this);

        user = SharedPrefUtils.getCurrentUser(this);
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (user.getUsername() != null) {
                name.setText(user.getUsername());
            } else name.setText(user.getEmail());
            Picasso.with(this)
                    .load(user.getPhoto())
                    .placeholder(R.drawable.ic_contact_picture)
                    .into(icon);
        }
    }

    public void setupFragment(Fragment fragment) {
        FragmentUtils.setFragment(this, fragment, FRAGMENT_TAG);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.group:
                MyLog.d("Select group");
                setupFragment(new CalendarFragment());
                break;
            case R.id.map:
                MyLog.d("Select map");
                setupFragment(new HomeFragment());
                break;
            case R.id.person:
                MyLog.d("Select person");
                setupFragment(new MainUserPageFragment());
                break;
            case R.id.search:
                MyLog.d("Select search");
//                setupFragment(new GoogleMapFragment());
                break;
            case R.id.setting:
                MyLog.d("Select setting");
                setupFragment(new SettingsFragment());
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getData() != null) {
                    loadImageFromCamera(data);
                } else if (ImageManager.fPhoto != null) {
                    loadImageFromGallery();
                }
            }
        }
    }

    private void loadImageFromCamera(Intent data) {
        if (FragmentUtils.getCurrentFragment(this) instanceof MainUserPageFragment) {
            ((MainUserPageFragment) FragmentUtils.getCurrentFragment(this)).updateImage(data.getData());
        }

        user.setPhoto(data.getData().toString());
        UserAPI user = new UserAPI(this, new Handler());
        user.updatePhoto(data.getData());
    }

    private void loadImageFromGallery() {
        if (FragmentUtils.getCurrentFragment(this) instanceof MainUserPageFragment) {
            ((MainUserPageFragment) FragmentUtils.getCurrentFragment(this))
                    .updateImage(Uri.fromFile(ImageManager.fPhoto));
        }

        user.setPhoto(Uri.decode(ImageManager.fPhoto.toString()));
        UserAPI user = new UserAPI(this, new Handler());
        user.updatePhoto(Uri.fromFile(ImageManager.fPhoto));
    }
}