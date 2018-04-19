package com.example.csongor.tourguideapp;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
    private @ResolutionConst
    String mResolution;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;


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

        /* Check and assign device resolution for retrieving correct images and icons at fragments. */
        // todo move it into the fragment
        mResolution = getResolutionString();
        // initializing FragmentManager
        mFragmentManager = MainActivity.this.getSupportFragmentManager();

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
        // todo: rename Resources
        if (id == R.id.nav_camera) {

            mFragmentTransaction=mFragmentManager.beginTransaction();
            Fragment historicalPlacesFragment=new ListFragmentToDisplay();
            Bundle toPut=new Bundle();
            toPut.putInt(BundleStringArgs.BUNDLE_TO_LOAD_ARG,BundleArgs.HISTORICAL_PLACES);
            historicalPlacesFragment.setArguments(toPut);
            mFragmentTransaction.add(R.id.fragment_container,historicalPlacesFragment);
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.commit();
        } else if (id == R.id.nav_gallery) {
// todo: on click start new fragment to load
        } else if (id == R.id.nav_slideshow) {
// todo: on click start new fragment to load
        } else if (id == R.id.nav_manage) {
// todo: on click start new fragment to load
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
    @ResolutionConst
    String getResolutionString() {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        switch (mDisplayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return ResolutionConst.LDPI;
            case DisplayMetrics.DENSITY_MEDIUM:
                return ResolutionConst.MDPI;
            case DisplayMetrics.DENSITY_HIGH:
                return ResolutionConst.HDPI;
            case DisplayMetrics.DENSITY_XHIGH:
                return ResolutionConst.XHDPI;
            case DisplayMetrics.DENSITY_XXHIGH:
                return ResolutionConst.XXHDPI;
            case DisplayMetrics.DENSITY_XXXHIGH:
                return ResolutionConst.XXXHDPI;
            default:
                if (mDisplayMetrics.densityDpi == DisplayMetrics.DENSITY_560) {
                    return ResolutionConst.XXHDPI;
                } else if (mDisplayMetrics.densityDpi <= DisplayMetrics.DENSITY_420) {
                    return ResolutionConst.XHDPI;
                } else return ResolutionConst.HDPI;
        }

    }
}