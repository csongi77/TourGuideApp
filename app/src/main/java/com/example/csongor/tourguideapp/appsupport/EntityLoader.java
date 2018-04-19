package com.example.csongor.tourguideapp.appsupport;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.csongor.tourguideapp.BundleArgs;
import com.example.csongor.tourguideapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EntityLoader extends AsyncTaskLoader<List<Entity>> {

    // Declaring host and port here. If it's changed, it's easier to find here
    private static final String HOST="http://csongi.sytes.net:";
    private static final String PORT="8879";
    private static final String LOG_TAG=EntityLoader.class.getSimpleName();
    /**
     * Declaring categoryId variable in order to download appropriate PlaceList (Historical Places,
     * Events, Sport places, Restaurants
      */
    private @BundleArgs int mCategoryId;


    public EntityLoader(@NonNull Context context, @BundleArgs int categoryId) {
        super(context);
        mCategoryId=categoryId;
    }

    /**
     * Called on a worker thread to perform the actual load and to return
     * the result of the load operation.
     * <p>
     * Implementations should not deliver the result directly, but should return them
     * from this method, which will eventually end up calling {@link #deliverResult} on
     * the UI thread.  If implementations need to process the results on the UI thread
     * they may override {@link #deliverResult} and do so there.
     * <p>
     * To support cancellation, this method should periodically check the value of
     * {@link #isLoadInBackgroundCanceled} and terminate when it returns true.
     * Subclasses may also override {@link #cancelLoadInBackground} to interrupt the load
     * directly instead of polling {@link #isLoadInBackgroundCanceled}.
     * <p>
     * When the load is canceled, this method may either return normally or throw
     * {@link OperationCanceledException}.  In either case, the {@link Loader} will
     * call {@link #onCanceled} to perform post-cancellation cleanup and to dispose of the
     * result object, if any.
     *
     * @return The result of the load operation.
     * @throws OperationCanceledException if the load is canceled during execution.
     * @see #isLoadInBackgroundCanceled
     * @see #cancelLoadInBackground
     * @see #onCanceled
     */
    @Nullable
    @Override
    public List<Entity> loadInBackground() {
        //open connection and retrieve JSON string
        String jsonString=getJsonString(mCategoryId);
        if(jsonString.equals("")) Snackbar.make(((Activity)getContext()).findViewById(R.id.coordinator_root_layout),
                ((Activity) getContext()).getResources().getString(R.string.connection_parsing_error), Snackbar.LENGTH_INDEFINITE).show();
        /**
         * parse json and create Entity. If there are no valid Entities, only a
         * List containing a single NullPlace Entity will returned.
         */
        List<Entity> entityList = parseJsonString(jsonString);
        return entityList;
    }


    /**
     * This method tries to make connection to server and get JSON String via REST.
     * If succeeds returns String containing JSON array list of Places
     * @param mCategoryId - the category Id which should downloaded
     * @return - String containing JSON array list or "";
     */
    private String getJsonString(@BundleArgs int mCategoryId) {
        // set up path for REST get request, StringBuilder for storing JSON string
        String path=HOST+PORT+"/q/entities/placelist/"+String.valueOf(mCategoryId);
        StringBuilder toReturn = null;
        // declaring connection here in order to let it closed in the finally block
        HttpURLConnection connection=null;
        try {
            // creating connection, setting up request method, and Buffered Reader object
            URL url=new URL(path);
            connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStreamReader stream=new InputStreamReader(connection.getInputStream());
            BufferedReader reader=new BufferedReader(stream);
            String toRead=reader.readLine();
            // retrieving JSON string
            while(toRead!=null){
                toReturn.append(toRead);
                toRead=reader.readLine();
            }
            // error handling
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"---> Error parsing URL");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG,"---> Error opening connection");
            e.printStackTrace();
        } finally {
            // close connection
            if(connection!=null)
                connection.disconnect();
        }
        if(toReturn==null) return "";
        return toReturn.toString();
    }

    /**
     * Parsing retrieved JSON String into Places which will be put into a List
     * @param jsonString - String retrieved from the server
     * @return - Entity List. If there are no valid JSON object, a List containing single NullPlace
     * will be returned.
     */
    private List<Entity> parseJsonString(String jsonString) {
        List<Entity> placeList=new ArrayList<>();
        Bitmap defaultImage, defaultIcon;
        Drawable d=getContext().getResources().getDrawable(R.drawable.ic_image_black_48dp);
        defaultIcon= ((BitmapDrawable)d).getBitmap();
        defaultImage = defaultIcon;
        try {
            JSONArray baseArray=new JSONArray(jsonString);
            for (int i = 0; i < baseArray.length(); i++) {
                JSONObject entityObject=(JSONObject)baseArray.get(i);
                int idPlace=entityObject.getInt("idPlace");
                String title=entityObject.getString("title");
                String address=entityObject.getString("address");
                String fromTo=entityObject.getString("fromTo");
                boolean childFriendly=entityObject.getBoolean("childFriendly");
                boolean dogsAllowed=entityObject.getBoolean("dogsAllowed");
                int categoryId=entityObject.getInt("categoryId");
                String description=entityObject.getString("description");
                boolean pictureAvailable=entityObject.getBoolean("pictureAvailable");
                Entity place=new Place(idPlace, categoryId, title, address, fromTo,description,childFriendly,dogsAllowed,pictureAvailable,defaultImage,defaultIcon );
                placeList.add(place);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "--->JSON parsing error");
            e.printStackTrace();
        }
        if(placeList.isEmpty())placeList.add(new NullPlace());
        return placeList;
    }
}
