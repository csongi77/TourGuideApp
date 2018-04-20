package com.example.csongor.tourguideapp.appsupport;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class NullPlace implements Entity, Parcelable {

    private  int mId;
    private int mCategoryId;


    /**
     * Default constructor of NullPlace
     */
    public NullPlace() {
        mId=-1;
        mCategoryId =-1;
    }

    protected NullPlace(Parcel in) {
        mId = in.readInt();
        mCategoryId = in.readInt();
    }

    public static final Creator<NullPlace> CREATOR = new Creator<NullPlace>() {
        @Override
        public NullPlace createFromParcel(Parcel in) {
            return new NullPlace(in);
        }

        @Override
        public NullPlace[] newArray(int size) {
            return new NullPlace[size];
        }
    };

    /**
     * Base Id of Entity. NullPlace will automatically return -1.
     * Since this Id is the primary key in database, it is unique, therefore
     * equals() method has only checks this field.
     *
     * @return - the Id of Entity
     */
    @Override
    public int getId() {
        return mId;
    }

    /**
     * The category Id is required for ArrayAdapter objects. The icon of @+mId/list_item_icon_time
     * is mCategoryId-dependent. If the list contains only "Events", the icon is set to ic_event_black.
     * Otherwise it remains unchanged (ic_query_builder_black)
     *
     * @return - the Category Id
     * HISTORICAL_PLACES = 1;
     * EVENTS = 2;
     * SPORTS = 3;
     * RESTAURANTS = 4;
     */
    @Override
    public int getCategoryId() {
        return -1;
    }

    /**
     * Name/title of Place, Event
     *
     * @return - the name/title String of Entity
     */
    @Override
    public String getTitle() {
        return "NoTitle";
    }

    /**
     * Address of Place, Event
     *
     * @return - Address String
     */
    @Override
    public String getAddress() {
        return "";
    }

    /**
     * Work hours of Places, dates of Events.
     * If not Available, the default String is "N/A"
     *
     * @return - default "N/A", else the opening hours, or date of Event
     */
    @Override
    public String getFromTo() {
        return "N/A";
    }

    /**
     * Is the Place/Event child friendly?
     *
     * @return - true if child friendly, else false
     */
    @Override
    public boolean isChildFriendly() {
        return false;
    }

    /**
     * Are pets allowed to enter?
     *
     * @return - true if pets are allowed, else false
     */
    @Override
    public boolean isPetAllowed() {
        return false;
    }

    /**
     * The long description of Place, Event (max 255 chars)
     *
     * @return - String description (max 255. chars)
     */
    @Override
    public String getDescription() {
        return "N/A";
    }

    /**
     * Is picture and icon available on server. If no picture is available, only the
     * substitute icon/picture will used from Drawable resources.
     *
     * @return - true if there are icon and picture is available in multiple resolutions on the
     * server, false in any other case
     */
    @Override
    public boolean isPictureAvialable() {
        return false;
    }

    /**
     * Picture of Place/Event in ldpi, mdpi, hdpi, xhdpi,
     * xxhdpi, xxxhdpi depending on device's resolution
     *
     * @return - image of Event/Place in jpg/png format.
     * If isPictureAvialable==false, the default picture will
     * returned which is stored in Drawable resources
     */
    @Override
    public Bitmap getImage() {
        return null;
    }

    /**
     * To update picture. This method should used with AsyncTaskLoader.
     *
     * @param image - image file in png/jpg
     */
    @Override
    public void setImage(Bitmap image) {
    }

    /**
     * 64dp sized icon of Place/Event in ldpi, mdpi, hdpi, xhdpi,
     * xxhdpi, xxxhdpi depending on device's resolution
     *
     * @return - icon image of Event/Place in jpg/png format.
     * If isPictureAvialable==false, the default picture will
     * returned which is stored in Drawable resources
     */
    @Override
    public Bitmap getIconImage() {
        return null;
    }

    /**
     * To update picture. This method should used with AsyncTaskLoader.
     *
     * @param icon - image file in png/jpg
     */
    @Override
    public void setIconImage(Bitmap icon) {
    }

    /**
     * @param downloaded - set true if image has downloaded and set with AsyncTaskLoader
     */
    @Override
    public void setPictureDownloaded(boolean downloaded) {

    }

    /**
     * @return - has been assigned picture to Entity other than default image/icon?
     */
    @Override
    public boolean hasPictureDownloaded() {
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NullPlace nullPlace = (NullPlace) o;
        return mId == nullPlace.mId;
    }

    @Override
    public int hashCode() {

        return mId*31;
    }


    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mCategoryId);
    }
}
