package com.maxml.timer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maxml.timer.controllers.ActionController;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Events;
import com.maxml.timer.entity.ShowFragmentListener;
import com.maxml.timer.entity.ShowProgressListener;
import com.maxml.timer.entity.StatisticControl;
import com.maxml.timer.entity.User;
import com.maxml.timer.entity.WifiState;
import com.maxml.timer.ui.activity.LoginActivity;
import com.maxml.timer.ui.dialog.DialogCallback;
import com.maxml.timer.ui.dialog.DialogFactory;
import com.maxml.timer.ui.fragments.DayCalendarFragment;
import com.maxml.timer.ui.fragments.HomeFragment;
import com.maxml.timer.ui.fragments.MainUserPageFragment;
import com.maxml.timer.ui.fragments.MonthCalendarFragment;
import com.maxml.timer.ui.fragments.SelectTagsFragment;
import com.maxml.timer.ui.fragments.SettingsFragment;
import com.maxml.timer.ui.fragments.WeekCalendarFragment;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.FragmentUtils;
import com.maxml.timer.util.ImageUtil;
import com.maxml.timer.util.NetworkUtil;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ShowProgressListener, StatisticControl, ShowFragmentListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ProgressBar pbLoad;
    private Toolbar toolbar;
    private LinearLayout statisticLayout;
    private TextView eventTime;

    private DbController dbController;
    private ActionController actionController;
    private EventBus eventBus;
    private int settingRequest;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pbLoad = findViewById(R.id.pb_load);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        statisticLayout = findViewById(R.id.layoutStatistic);
        eventTime = findViewById(R.id.tvTime);

        drawerLayout = findViewById(R.id.drawer_layout);
        hideProgressBar();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        initDrawer();
        initController();
        setHomeFragment();
        checkOnWifi();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        switch (item.getItemId()) {
            case R.id.i_home:
                if (!(fragment instanceof HomeFragment)) {
                    setupFragment(new HomeFragment());
                }
                break;
            case R.id.i_user:
                if (!(fragment instanceof MainUserPageFragment)) {
                    setupFragment(new MainUserPageFragment());
                }
                break;
            case R.id.i_tags:
                if (!(fragment instanceof SelectTagsFragment)) {
                    setupFragment(new SelectTagsFragment());
                }
                break;
            case R.id.i_calendar_month:
                if (!(fragment instanceof MonthCalendarFragment)) {
                    setupFragment(new MonthCalendarFragment());
                }
                break;
            case R.id.i_calendar_week:
                if (!(fragment instanceof WeekCalendarFragment)) {
                    setupFragment(new WeekCalendarFragment());
                }
                break;
            case R.id.i_calendar_day:
                if (!(fragment instanceof DayCalendarFragment)) {
                    setupFragment(new DayCalendarFragment());
                }
                break;
            case R.id.i_setting:
                Log.d(Constants.TAG, "Select setting");
                if (!(fragment instanceof SettingsFragment)) {
                    setupFragment(new SettingsFragment());
                }
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        eventBus.register(this);
        actionController.registerEventBus(eventBus);
        actionController.setMineActivityEventBus();

        dbController.getCurrentUser();
        super.onStart();
    }

    @Override
    protected void onStop() {
        eventBus.unregister(this);
        actionController.unregisterEventBus(eventBus);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (!FragmentUtils.backFragment(this)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_title_exit)
                    .setMessage(R.string.dialog_message_exit)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            actionController.closeApp();
                            finish();
                        }
                    })
                    .create()
                    .show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (NetworkUtil.isNetworkAvailable(this)) {
                        if (data != null && data.getData() != null) {
                            loadImageFromGallery(data);
                        } else if (ImageUtil.fPhoto != null) {
                            loadImageFromCamera();
                        }
                        showProgressBar();

                        dbController.getCurrentUser();
                    } else {
                        Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case Constants.REQUEST_CHECK_LOCATION_SETTING:
                if (resultCode == RESULT_OK) {
                    Log.d(Constants.LOG, "Dialog turn on navigation ok");
                    // do logic under method
                    eventBus.post(new Events.TurnOnGeolocation(Constants.EVENT_TURN_ON_SUCCESSFUL, settingRequest));
                } else {
                    Log.d(Constants.LOG, "Dialog turn on navigation cancel");
                    eventBus.post(new Events.TurnOnGeolocation(Constants.EVENT_TURN_ON_DENY, settingRequest));
                }
                break;
        }
    }

    @Override
    public void showStatisticLayout() {
        statisticLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideStatisticLayout() {
        statisticLayout.setVisibility(View.GONE);
    }

    @Override
    public void setEventTime(String time) {
        String placeholder = getString(R.string.text_work_statistic);
        String text = placeholder + " " + time;
        eventTime.setText(text);
    }

    @Override
    public void showFragment(Fragment fragment) {
        setupFragment(fragment);
    }

    @Subscribe
    public void turnOnGeolocation(Events.TurnOnGeolocation event) {
        if (event.getMessage().equals(Constants.EVENT_TURN_ON_GEOLOCATION)) {
            settingRequest = event.getRequest();
            DialogFactory.showGpsSwitchAlert(this, new DialogCallback() {
                @Override
                public void onTrue() {
                    Log.d(Constants.LOG, "Turn on navigation ok");
                    eventBus.post(new Events.TurnOnGeolocation(Constants.EVENT_TURN_ON_SUCCESSFUL, settingRequest));
                }

                @Override
                public void onShowSettingDialog() {
                    Log.d(Constants.LOG, "Show setting dialog");
                    // catch result in onActivityResult()
                }

                @Override
                public void onFalse() {
                    Log.d(Constants.LOG, "Turn on navigation cancel");
                    eventBus.post(new Events.TurnOnGeolocation(Constants.EVENT_TURN_ON_DENY, settingRequest));
                }
            });
        }
    }

    @Subscribe
    public void onReceiveUser(User user) {
        initDrawerHeader(user);
        if (user.isAnonymously()) {
            showMessageAboutAnonymously();
        }
    }

    @Subscribe
    public void onDatabaseEvent(Events.DbResult event) {
        switch (event.getResultStatus()) {
            case Constants.EVENT_DB_RESULT_OK:
                if (FragmentUtils.getCurrentFragment(this) instanceof MainUserPageFragment) {
                    ((MainUserPageFragment) FragmentUtils.getCurrentFragment(this))
                            .updateUI();

                    dbController.getCurrentUser();

                    hideProgressBar();
                }
                break;
        }
    }

    public void showProgressBar() {
        pbLoad.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        pbLoad.setVisibility(View.INVISIBLE);
    }

    public void setupFragment(Fragment fragment) {
        FragmentUtils.setFragment(this, fragment, fragment.getClass().getName());
    }

    private void initController() {
        eventBus = new EventBus();
        dbController = new DbController(this, eventBus);
        actionController = new ActionController(this, eventBus);
    }

    private void setHomeFragment() {
        FragmentUtils.setFragment(this, new HomeFragment());
    }

    private void initDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerLayout.setStatusBarBackground(R.color.primary_dark);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView = findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                return false;
            }
        });

        View header = navigationView.getHeaderView(0);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupFragment(new MainUserPageFragment());
                drawerLayout.closeDrawers();
            }
        });
    }

    private void initDrawerHeader(User user) {
        if (user == null) {
            return;
        }
        View header = navigationView.getHeaderView(0);

        TextView name = header.findViewById(R.id.user_name);
        ImageView icon = header.findViewById(R.id.profile_image);

        navigationView.setNavigationItemSelectedListener(this);

        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (user.getUsername() != null) {
                name.setText(user.getUsername());
            } else name.setText(user.getEmail());
            if (user.getPhoto() != null && !user.getPhoto().equalsIgnoreCase("")) {
                if (user.getPhoto().contains("content")) {
                    Picasso.with(this)
                            .load(user.getPhoto())
                            .into(icon);
                } else {
                    File file = new File(user.getPhoto());

                    Picasso.with(this)
                            .load(file)
                            .into(icon);
                }
            }
        }
    }

    private void loadImageFromGallery(Intent data) {
        dbController.updateUserPhoto(data.getData().toString());
    }

    private void loadImageFromCamera() {
        dbController.updateUserPhoto(ImageUtil.fPhoto.toString());
    }

    private void showMessageAboutAnonymously() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.container), R.string.warning_about_anonymously, Snackbar.LENGTH_LONG)
                .setAction("Sign in", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.divider));
        snackbar.show();
    }

    private void checkOnWifi() {
        WifiState wifiState = NetworkUtil.getCurrentWifi(getApplicationContext());

        if (wifiState.getId() != null && !wifiState.getId().equalsIgnoreCase("")) {
            dbController.wifiActivated(wifiState);
        }
    }
}