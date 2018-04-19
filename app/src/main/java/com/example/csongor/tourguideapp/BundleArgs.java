package com.example.csongor.tourguideapp;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.example.csongor.tourguideapp.BundleArgs.EVENTS;
import static com.example.csongor.tourguideapp.BundleArgs.HISTORICAL_PLACES;
import static com.example.csongor.tourguideapp.BundleArgs.RESTAURANTS;
import static com.example.csongor.tourguideapp.BundleArgs.SPORTS;

/**
 * This Annotation is used for passing values from Activity to Fragment in order to
 * determine which Category has to be loaded.
 */
@IntDef({HISTORICAL_PLACES, EVENTS, SPORTS, RESTAURANTS})
@Retention(RetentionPolicy.SOURCE)
public @interface BundleArgs {
    int HISTORICAL_PLACES = 1;
    int EVENTS = 2;
    int SPORTS = 3;
    int RESTAURANTS = 4;
}
