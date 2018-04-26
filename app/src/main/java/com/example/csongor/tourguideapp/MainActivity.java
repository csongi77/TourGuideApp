package com.example.csongor.tourguideapp;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.AnimatorRes;
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

    private static final String BUNDLE_SCREEN_RESOLUTION = "BUNDLE_SCREEN_RESOLUTION";

    public String getmResolution() {
        return mResolution;
    }

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

        /* Check and assign device resolution for retrieving correct images and icons at fragments.
         * Also handle screen rotation, getting saved value from Bundle instead
         * check it via getResolution method */
        if(savedInstanceState!=null) {
            mResolution = savedInstanceState.getString(BUNDLE_SCREEN_RESOLUTION);
        } else {
            mResolution = getResolutionString();
        }
        // initializing FragmentManager
        mFragmentManager = MainActivity.this.getSupportFragmentManager();
        mResolution = getResolutionString();
    }


    /**
     * On Back pressed, if Drawer is opened, it will be closed.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mFragmentManager.getBackStackEntryCount()>0){
            mFragmentManager.popBackStack();
        }
        else {
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

    /**
     *  On clicking Navigation Drawer item it opens the appropriate Fragment
     * @param item - selected Navigation Drawer item (Historical Places, Events, Sports, Restaurants)
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        // Put screen resolution into Bundle and the Category of Entity, too
        int id = item.getItemId();
        Bundle bundleToSendToFragment=new Bundle();
        bundleToSendToFragment.putString(BundleStringArgs.BUNDLE_RESOLUTION,mResolution);
        if (id == R.id.historical_places) {
            mFragmentTransaction=mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            Fragment historicalPlacesFragment=new ListFragmentToDisplay();
            bundleToSendToFragment.putInt(BundleStringArgs.BUNDLE_ENTITY_CATEGORY,BundleArgs.HISTORICAL_PLACES);
            historicalPlacesFragment.setArguments(bundleToSendToFragment);
            mFragmentTransaction.replace(R.id.fragment_container,historicalPlacesFragment);
            mFragmentTransaction.commit();
        } else if (id == R.id.events) {
            mFragmentTransaction=mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            Fragment historicalPlacesFragment=new ListFragmentToDisplay();
            bundleToSendToFragment.putInt(BundleStringArgs.BUNDLE_ENTITY_CATEGORY,BundleArgs.EVENTS);
            historicalPlacesFragment.setArguments(bundleToSendToFragment);
            mFragmentTransaction.replace(R.id.fragment_container,historicalPlacesFragment);
            mFragmentTransaction.commit();
        } else if (id == R.id.sports) {
            mFragmentTransaction=mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            Fragment historicalPlacesFragment=new ListFragmentToDisplay();
            bundleToSendToFragment.putInt(BundleStringArgs.BUNDLE_ENTITY_CATEGORY,BundleArgs.SPORTS);
            historicalPlacesFragment.setArguments(bundleToSendToFragment);
            mFragmentTransaction.replace(R.id.fragment_container,historicalPlacesFragment);
            mFragmentTransaction.commit();
        } else if (id == R.id.restaurants) {
            mFragmentTransaction=mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            Fragment historicalPlacesFragment=new ListFragmentToDisplay();
            bundleToSendToFragment.putInt(BundleStringArgs.BUNDLE_ENTITY_CATEGORY,BundleArgs.RESTAURANTS);
            historicalPlacesFragment.setArguments(bundleToSendToFragment);
            mFragmentTransaction.replace(R.id.fragment_container,historicalPlacesFragment);
            mFragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(BundleStringArgs.BUNDLE_RESOLUTION,mResolution);
        super.onSaveInstanceState(outState);
    }
// todo make it a singleton class!!!
    /**
     * method for determining screen resolution in order to load appropriate image/icon size
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