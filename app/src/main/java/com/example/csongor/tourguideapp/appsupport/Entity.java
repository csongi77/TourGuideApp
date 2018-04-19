package com.example.csongor.tourguideapp.appsupport;

import android.graphics.Bitmap;
import android.os.Parcelable;

/**
 * Interface for different Entity classes. At the moment two classes available:
 * 1) Place class - it holds all required information
 * 2) NullPlace class - instead throwing Exception this class is instantiated
 */
public interface Entity extends Parcelable {
    int getId();
    int getCategoryId();
    String getTitle();
    String getAddress();
    String getFromTo();
    boolean isChildFriendly();
    boolean isPetAllowed();
    String getDescription();
    boolean isPictureAvialable();
    Bitmap getImage();
    void setImage(Bitmap image);
    Bitmap getIconImage();
    void setIconImage();
}
