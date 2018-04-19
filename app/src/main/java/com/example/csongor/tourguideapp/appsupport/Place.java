package com.example.csongor.tourguideapp.appsupport;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Entity, Parcelable {
    // Field declarations
    private int mId;
    private int mCategoryId;
    private String mTitle, mAddress,mFromTo, mDescription;
    private boolean mChildFriendly, mPetAllowed,mPictureAvailable;
    private Bitmap mImage,mIconImage;

    // default constructor

    /**
     *
     * @param id
     * @param categoryId
     * @param title
     * @param address
     * @param fromTo
     * @param description
     * @param childFriendly
     * @param petAllowed
     * @param pictureAvailable
     * @param defaultImage
     * @param defaultIconImage
     */
    public Place(int id, int categoryId, String title, String address, String fromTo, String description,
                 boolean childFriendly, boolean petAllowed, boolean pictureAvailable,
                 Bitmap defaultImage, Bitmap defaultIconImage){
        mId=id;
        mCategoryId=categoryId;
        mTitle=title;
        mAddress=address;
        mFromTo=fromTo;
        mDescription=description;
        mChildFriendly=childFriendly;
        mPetAllowed=petAllowed;
        mPictureAvailable=pictureAvailable;
        mImage=defaultImage;
        mIconImage=defaultIconImage;
    }

    // default Parcel implementation
    protected Place(Parcel in) {
        mId = in.readInt();
        mCategoryId = in.readInt();
        mTitle = in.readString();
        mAddress = in.readString();
        mFromTo = in.readString();
        mDescription = in.readString();
        mChildFriendly = in.readByte() != 0;
        mPetAllowed = in.readByte() != 0;
        mPictureAvailable = in.readByte() != 0;
        mImage = in.readParcelable(Bitmap.class.getClassLoader());
        mIconImage = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
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
        return mCategoryId;
    }

    /**
     * Name/mTitle of Place, Event
     *
     * @return - the name/mTitle String of Entity
     */
    @Override
    public String getTitle() {
        return mTitle;
    }

    /**
     * Address of Place, Event
     *
     * @return - Address String
     */
    @Override
    public String getAddress() {
        return mAddress;
    }

    /**
     * Work hours of Places, dates of Events.
     * If not Available, the default String is "N/A"
     *
     * @return - default "N/A", else the opening hours, or date of Event
     */
    @Override
    public String getFromTo() {
        return mFromTo;
    }

    /**
     * Is the Place/Event child friendly?
     *
     * @return - true if child friendly, else false
     */
    @Override
    public boolean isChildFriendly() {
        return mChildFriendly;
    }

    /**
     * Are pets allowed to enter?
     *
     * @return - true if pets are allowed, else false
     */
    @Override
    public boolean isPetAllowed() {
        return mPetAllowed;
    }

    /**
     * The long description of Place, Event (max 255 chars)
     *
     * @return - String description (max 255. chars)
     */
    @Override
    public String getDescription() {
        return mDescription;
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
        return mPictureAvailable;
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
        return mImage;
    }

    /**
     * To update picture. This method should used with AsyncTaskLoader.
     *
     * @param image - image file in png/jpg
     */
    @Override
    public void setImage(Bitmap image) {
        mImage=image;
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
        return mIconImage;
    }

    /**
     * To update picture. This method should used with AsyncTaskLoader.
     *
     * @param icon - image file in png/jpg
     */
    @Override
    public void setIconImage(Bitmap icon) {
        mIconImage=icon;
    }

    /**
     * Overriding hashCode and equals methods. Since mId of Place is unique index key in
     * database, it's enough to check it in equals method.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        return mId == place.mId;
    }

    @Override
    public int hashCode() {
        return mId;
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
        dest.writeString(mTitle);
        dest.writeString(mAddress);
        dest.writeString(mFromTo);
        dest.writeString(mDescription);
        dest.writeByte((byte) (mChildFriendly ? 1 : 0));
        dest.writeByte((byte) (mPetAllowed ? 1 : 0));
        dest.writeByte((byte) (mPictureAvailable ? 1 : 0));
        dest.writeParcelable(mImage, flags);
        dest.writeParcelable(mIconImage, flags);
    }
}
