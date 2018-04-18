package com.example.csongor.tourguideapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * variable for proper image and icon download. Possible values:
     * ldpi, mdpi, hdpi, xhdpi, xxhdpi and xxxhdpi
     * These values will be passed to AsyncTaskLoader object for retrieving appropriate images and
     * icons via RESTful service.
     */
    private @ResolutionAnn
    String mResolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /* Checking internet connection in order to access data of Entities */
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(networkInfo != null && networkInfo.isConnectedOrConnecting()))
            Snackbar.make(findViewById(R.id.coordinator_root_layout), R.string.not_connected, Snackbar.LENGTH_INDEFINITE).show();

        /* Check and assign device resolution for retrieving correct images and icons. */
        mResolution = getResolutionString();


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // todo: on click start new fragment to load
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * method for determining screen resolution
     *
     * @return ldpi, mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi
     */
    private @NonNull
    @ResolutionAnn
    String getResolutionString() {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        switch (mDisplayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return ResolutionAnn.LDPI;
            case DisplayMetrics.DENSITY_MEDIUM:
                return ResolutionAnn.MDPI;
            case DisplayMetrics.DENSITY_HIGH:
                return ResolutionAnn.HDPI;
            case DisplayMetrics.DENSITY_XHIGH:
                return ResolutionAnn.XHDPI;
            case DisplayMetrics.DENSITY_XXHIGH:
                return ResolutionAnn.XXHDPI;
            case DisplayMetrics.DENSITY_XXXHIGH:
                return ResolutionAnn.XXXHDPI;
            default:
                if (mDisplayMetrics.densityDpi == DisplayMetrics.DENSITY_560) {
                    return ResolutionAnn.XXHDPI;
                } else if (mDisplayMetrics.densityDpi <= DisplayMetrics.DENSITY_420) {
                    return ResolutionAnn.XHDPI;
                } else return ResolutionAnn.HDPI;
        }

    }
}