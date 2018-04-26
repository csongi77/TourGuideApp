package com.example.csongor.tourguideapp;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.example.csongor.tourguideapp.FragmentStringArgs.LIST_FRAGMENT;

/**
 * StringDef annotation for Fragment Strings instead of Enums
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({LIST_FRAGMENT})
public @interface FragmentStringArgs {
    String LIST_FRAGMENT="LIST_FRAGMENT";
}
