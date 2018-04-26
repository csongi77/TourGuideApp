package com.example.csongor.tourguideapp;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.example.csongor.tourguideapp.ResolutionConst.HDPI;
import static com.example.csongor.tourguideapp.ResolutionConst.ICON;
import static com.example.csongor.tourguideapp.ResolutionConst.IMAGE;
import static com.example.csongor.tourguideapp.ResolutionConst.LDPI;
import static com.example.csongor.tourguideapp.ResolutionConst.MDPI;
import static com.example.csongor.tourguideapp.ResolutionConst.XHDPI;
import static com.example.csongor.tourguideapp.ResolutionConst.XXHDPI;
import static com.example.csongor.tourguideapp.ResolutionConst.XXXHDPI;

/**
 * Instead Enums we create StringDef. These values has to be used for creating RESTful get URLs
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({LDPI, MDPI, HDPI, XHDPI, XXHDPI, XXXHDPI, IMAGE, ICON})
public @interface ResolutionConst {
    String LDPI = "ldpi";
    String MDPI = "mdpi";
    String HDPI = "hdpi";
    String XHDPI = "xhdpi";
    String XXHDPI = "xxhdpi";
    String XXXHDPI = "xxxhdpi";

    String IMAGE="image";
    String ICON="icon";
}
