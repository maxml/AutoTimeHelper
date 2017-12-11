package com.maxml.timer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.maxml.timer.api.SliceCRUD;
import com.maxml.timer.controllers.TableController;
import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.User;
import com.maxml.timer.ui.activity.CalendarActivity;
import com.maxml.timer.ui.fragments.CalendarFragment;
import com.maxml.timer.ui.fragments.GoogleMapFragment;
import com.maxml.timer.ui.fragments.ManualActivityFragment;
import com.maxml.timer.ui.fragments.SettingsFragment;
import com.maxml.timer.ui.fragments.SliceListViewFragment;
import com.maxml.timer.ui.fragments.TablesFragment;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.FragmentUtils;
import com.maxml.timer.util.SharedPrefUtils;
import com.maxml.timer.util.SliceType;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FRAGMENT_TAG = "CURRENT_FRAGMENT";

    private DrawerLayout drawerLayout;
    private ProgressBar progressBar;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    // // TODO: test
//    SliceCRUD c;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        hideProgressBar();

/*
        // todo test
        c = new SliceCRUD(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case Constants.RESULT_OK:
                        List<Slice> resultList = (List<Slice>) msg.obj;
                        Slice slice = (Slice) resultList.get(0);
                        slice.setDescription("updated");

                        c.setHandler(new Handler());
                        c.update(slice);
                    break;
                }
            }
        });
        c.read("update");
*/



/*
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
*/

        initDrawer();
        if (savedInstanceState == null)
            setupFragment(new SliceListViewFragment());
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

        User user = SharedPrefUtils.getCurrentUser(this);
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (user.getUsername() != null) {
                name.setText(user.getUsername());
            } else name.setText(user.getEmail());
            Picasso.with(this)
                    .load(user.getPhoto())
                    .placeholder(R.drawable.ic_contact_picture)
                    .into(icon);
        }

/*
        btnLogIn = (Button) header.findViewById(R.id.btnLogin);

        if (user == null || TextUtils.isEmpty(user.getId())) {
            btnLogIn.setText(R.string.btnLogin);
            name.setText("Vertys");
        } else {
            btnLogIn.setText(R.string.btnLogOut);
            name.setText(user.getName());
        }

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
*/

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
                setupFragment(new ManualActivityFragment());
                break;
            case R.id.person:
                MyLog.d("Select person");
                setupFragment(new TablesFragment());
                break;
            case R.id.search:
                MyLog.d("Select search");
                setupFragment(new GoogleMapFragment());
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
}