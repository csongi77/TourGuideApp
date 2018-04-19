package com.example.csongor.tourguideapp;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.example.csongor.tourguideapp.ResolutionConst.HDPI;
import static com.example.csongor.tourguideapp.ResolutionConst.LDPI;
import static com.example.csongor.tourguideapp.ResolutionConst.MDPI;
import static com.example.csongor.tourguideapp.ResolutionConst.XHDPI;
import static com.example.csongor.tourguideapp.ResolutionConst.XXHDPI;
import static com.example.csongor.tourguideapp.ResolutionConst.XXXHDPI;

@Retention(RetentionPolicy.SOURCE)
@StringDef({LDPI, MDPI, HDPI, XHDPI, XXHDPI, XXXHDPI})
public @interface ResolutionConst {
    public static final String LDPI = "ldpi";
    public static final String MDPI = "mdpi";
    public static final String HDPI = "hdpi";
    public static final String XHDPI = "xhdpi";
    public static final String XXHDPI = "xxhdpi";
    public static final String XXXHDPI = "xxxhdpi";
}
