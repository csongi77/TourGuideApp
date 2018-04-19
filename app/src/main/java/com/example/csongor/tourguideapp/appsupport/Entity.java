package com.example.csongor.tourguideapp.appsupport;

import android.graphics.Bitmap;
import android.os.Parcelable;

/**
 * Interface for different Entity classes. At the moment two classes available:
 * 1) Place class - it holds all required information
 * 2) NullPlace class - instead throwing Exception this class is instantiated
 */
public interface Entity extends Parcelable {
    /**
     * Base Id of Entity. NullPlace will automatically return -1.
     * Since this Id is the primary key in database, it is unique, therefore
     * equals() method has only checks this field.
     *
     * @return - the Id of Entity
     */
    int getmId();

    /**
     * The category Id is required for ArrayAdapter objects. The icon of @+id/list_item_icon_time
     * is categoryId-dependent. If the list contains only "Events", the icon is set to ic_event_black.
     * Otherwise it remains unchanged (ic_query_builder_black)
     *
     * @return - the Category Id
     * HISTORICAL_PLACES = 1;
     * EVENTS = 2;
     * SPORTS = 3;
     * RESTAURANTS = 4;
     */
    int getCategoryId();

    /**
     * Name/title of Place, Event
     * @return - the name/title String of Entity
     */
    String getTitle();

    /**
     * Address of Place, Event
     * @return - Address String
     */
    String getAddress();

    /**
     * Work hours of Places, dates of Events.
     * If not Available, the default String is "N/A"
     * @return - default "N/A", else the opening hours, or date of Event
     */
    String getFromTo();

    /**
     * Is the Place/Event child friendly?
     * @return - true if child friendly, else false
     */
    boolean isChildFriendly();

    /**
     * Are pets allowed to enter?
     * @return - true if pets are allowed, else false
     */
    boolean isPetAllowed();

    /**
     * The long description of Place, Event (max 255 chars)
     * @return - String description (max 255. chars)
     */
    String getDescription();

    /**
     * Is picture and icon available on server. If no picture is available, only the
     * substitute icon/picture will used from Drawable resources.
     * @return - true if there are icon and picture is available in multiple resolutions on the
     * server, false in any other case
     */
    boolean isPictureAvialable();

    /**
     * Picture of Place/Event in ldpi, mdpi, hdpi, xhdpi,
     * xxhdpi, xxxhdpi depending on device's resolution
     * @return - image of Event/Place in jpg/png format.
     * If isPictureAvialable==false, the default picture will
     * returned which is stored in Drawable resources
     */
    Bitmap getImage();

    /**
     * To update picture. This method should used with AsyncTaskLoader.
     * @param image - image file in png/jpg
     */
    void setImage(Bitmap image);

    /**
     * 64dp sized icon of Place/Event in ldpi, mdpi, hdpi, xhdpi,
     * xxhdpi, xxxhdpi depending on device's resolution
     * @return - icon image of Event/Place in jpg/png format.
     * If isPictureAvialable==false, the default picture will
     * returned which is stored in Drawable resources
     */
    Bitmap getIconImage();

    /**
     * To update picture. This method should used with AsyncTaskLoader.
     * @param icon - image file in png/jpg
     */
    void setIconImage(Bitmap icon);
}
