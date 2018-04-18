package com.example.csongor.tourguideapp;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.example.csongor.tourguideapp.ResolutionAnn.HDPI;
import static com.example.csongor.tourguideapp.ResolutionAnn.LDPI;
import static com.example.csongor.tourguideapp.ResolutionAnn.MDPI;
import static com.example.csongor.tourguideapp.ResolutionAnn.XHDPI;
import static com.example.csongor.tourguideapp.ResolutionAnn.XXHDPI;
import static com.example.csongor.tourguideapp.ResolutionAnn.XXXHDPI;

@Retention(RetentionPolicy.SOURCE)
@StringDef({LDPI, MDPI, HDPI, XHDPI, XXHDPI, XXXHDPI})
public @interface ResolutionAnn {
    public static final String LDPI = "ldpi";
    public static final String MDPI = "mdpi";
    public static final String HDPI = "hdpi";
    public static final String XHDPI = "xhdpi";
    public static final String XXHDPI = "xxhdpi";
    public static final String XXXHDPI = "xxxhdpi";
}
