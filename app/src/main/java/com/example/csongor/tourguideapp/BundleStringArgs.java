package com.example.csongor.tourguideapp;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.example.csongor.tourguideapp.BundleStringArgs.BUNDLE_ENTITY;
import static com.example.csongor.tourguideapp.BundleStringArgs.BUNDLE_ENTITY_CATEGORY;
import static com.example.csongor.tourguideapp.BundleStringArgs.BUNDLE_IMAGE_TYPE;
import static com.example.csongor.tourguideapp.BundleStringArgs.BUNDLE_PARCELABLE_ENTITY_ARRAY_LIST;
import static com.example.csongor.tourguideapp.BundleStringArgs.BUNDLE_RESOLUTION;

/**
 * These Annotations are defining String constants for passing values from Activity to Fragment.
 * We use this to avoid Enums
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({BUNDLE_ENTITY_CATEGORY, BUNDLE_PARCELABLE_ENTITY_ARRAY_LIST, BUNDLE_ENTITY, BUNDLE_RESOLUTION, BUNDLE_IMAGE_TYPE})
public @interface BundleStringArgs {

    String BUNDLE_ENTITY_CATEGORY = "BUNDLE_ENTITY_CATEGORY";
    String BUNDLE_PARCELABLE_ENTITY_ARRAY_LIST = "BUNDLE_PARCELABLE_ENTITY_ARRAY_LIST";
    String BUNDLE_ENTITY = "BUNDLE_ENTITY";
    String BUNDLE_RESOLUTION="BUNDLE_RESOLUTION";
    String BUNDLE_IMAGE_TYPE="IMAGE_TYPE";

}
