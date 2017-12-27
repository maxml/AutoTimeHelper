package com.maxml.timer;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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

import com.maxml.timer.database.UserDAO;
import com.maxml.timer.controllers.Controller;
import com.maxml.timer.entity.User;
import com.maxml.timer.ui.fragments.ActionListViewFragment;
import com.maxml.timer.ui.fragments.CalendarFragment;
import com.maxml.timer.ui.fragments.GoogleMapFragment;
import com.maxml.timer.ui.fragments.HomeFragment;
import com.maxml.timer.ui.fragments.MainUserPageFragment;
import com.maxml.timer.ui.fragments.SettingsFragment;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.FragmentUtils;
import com.maxml.timer.util.SharedPrefUtils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FRAGMENT_TAG = "CURRENT_FRAGMENT";

    private DrawerLayout drawerLayout;
    private ProgressBar progressBar;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    private Controller controller;
    private EventBus eventBus;

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

        initController();

        setHomeFragment();
    }

    public void setupFragment(Fragment fragment) {
        FragmentUtils.setFragment(this, fragment, FRAGMENT_TAG);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calendar:
                MyLog.d("Select calendar");
                setupFragment(new CalendarFragment());
                break;
            case R.id.map:
                MyLog.d("Select home");
                setupFragment(new HomeFragment());
                break;
            case R.id.user:
                MyLog.d("Select user");
                setupFragment(new MainUserPageFragment());
                break;
            case R.id.search:
                MyLog.d("Select search");
                GoogleMapFragment fragment = new GoogleMapFragment();
                setupFragment(fragment);
                break;
            case R.id.setting:
                MyLog.d("Select setting");
                setupFragment(new SettingsFragment());
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        eventBus.register(this);
        controller.registerEventBus(eventBus);
        controller.sentUser();
        super.onStart();
    }

    @Override
    protected void onStop() {
        controller.unregisterEventBus(eventBus);
        eventBus.unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onReceiveUser(UserDAO userDAO) {
        initDrawerHeader(userDAO.getCurrentUser());
    }


    private void initController() {
        eventBus = new org.greenrobot.eventbus.EventBus();
        controller = new Controller(this, eventBus);
    }

    private void setHomeFragment() {
        FragmentUtils.setFragment(this, new HomeFragment());
    }

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
                controller.sentUser();
            }
        };

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerLayout.setStatusBarBackground(R.color.primary_dark);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void initDrawerHeader(User user) {
        if (user == null) {
            return;
        }
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
            if (user.getPhoto() != null && !user.getPhoto().equalsIgnoreCase("")) {
                Picasso.with(this)
                        .load(user.getPhoto())
                        .placeholder(R.drawable.ic_contact_picture)
                        .into(icon);
            }
        }
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
        switch (requestCode) {
            case Constants.REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        loadImageFromCamera(data);
                    } else if (ImageManager.fPhoto != null) {
                        loadImageFromGallery();
                    }
                    controller.sentUser();
                }
                break;
            case CalendarFragment.REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    showMessageInstallPlayService();
                } else {
                    resultCalendarFragment();
                }
                break;
            case CalendarFragment.REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                this.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(CalendarFragment.PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        setCredentialAccountName(accountName);
                        resultCalendarFragment();
                    }
                }
                break;
            case CalendarFragment.REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    resultCalendarFragment();
                }
                break;
        }
    }

    private void setCredentialAccountName(String accountName) {
        if (FragmentUtils.getCurrentFragment(this) instanceof CalendarFragment) {
            ((CalendarFragment) FragmentUtils.getCurrentFragment(this))
                    .setCredentialAccountName(accountName);
        }
    }

    private void showMessageInstallPlayService() {
        if (FragmentUtils.getCurrentFragment(this) instanceof CalendarFragment) {
            ((CalendarFragment) FragmentUtils.getCurrentFragment(this))
                    .showMessageInstallPlayService();
        }
    }

    public void resultCalendarFragment() {
        if (FragmentUtils.getCurrentFragment(this) instanceof CalendarFragment) {
            ((CalendarFragment) FragmentUtils.getCurrentFragment(this))
                    .resultFromApi();
        }
    }

    private void loadImageFromCamera(Intent data) {
        if (FragmentUtils.getCurrentFragment(this) instanceof MainUserPageFragment) {
            ((MainUserPageFragment) FragmentUtils.getCurrentFragment(this))
                    .updateImage(data.getData());
        }

        controller.updateUserPhoto(data.getData().toString());
    }

    private void loadImageFromGallery() {
        if (FragmentUtils.getCurrentFragment(this) instanceof MainUserPageFragment) {
            ((MainUserPageFragment) FragmentUtils.getCurrentFragment(this))
                    .updateImage(Uri.parse(ImageManager.fPhoto.toString()));
        }

        controller.updateUserPhoto(ImageManager.fPhoto.toString());
    }
}