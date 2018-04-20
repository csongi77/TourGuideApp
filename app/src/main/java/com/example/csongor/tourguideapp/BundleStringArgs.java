package com.example.csongor.tourguideapp;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.example.csongor.tourguideapp.BundleStringArgs.BUNDLE_EVENTS;
import static com.example.csongor.tourguideapp.BundleStringArgs.BUNDLE_HISTORICAL_PLACES;
import static com.example.csongor.tourguideapp.BundleStringArgs.BUNDLE_RESTAURANTS;
import static com.example.csongor.tourguideapp.BundleStringArgs.BUNDLE_SPORTS;
import static com.example.csongor.tourguideapp.BundleStringArgs.BUNDLE_TO_LOAD_ARG;

/**
 * These Annotations are defining String constants for passing values from Activity to Fragment.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({BUNDLE_HISTORICAL_PLACES,BUNDLE_EVENTS,BUNDLE_SPORTS,BUNDLE_RESTAURANTS, BUNDLE_TO_LOAD_ARG})
public @interface BundleStringArgs {
    String BUNDLE_HISTORICAL_PLACES="BUNDLE_HISTORICAL_PLACES";
    String BUNDLE_EVENTS="BUNDLE_EVENTS";
    String BUNDLE_SPORTS="BUNDLE_SPORTS";
    String BUNDLE_RESTAURANTS="BUNDLE_RESTAURANTS";
    String BUNDLE_TO_LOAD_ARG="BUNDLE_TO_LOAD_ARG";
    String BUNDLE_PARCELABLE_ENTITY_ARRAY_LIST="BUNDLE_PARCELABLE_ENTITY_ARRAY_LIST";

}
