package com.example.csongor.tourguideapp.appsupport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.csongor.tourguideapp.BundleStringArgs;
import com.example.csongor.tourguideapp.ResolutionConst;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ImageLoader extends AsyncTaskLoader<Integer> {
    // Declaring host and port here. If it's changed, it's easier to find here
    private static final String HOST = "http://csongi.sytes.net:";
    private static final String PORT = "8879";
    private static final String PATH_PARAM = "/tourguide/q/image/";
    private static final String LOG_TAG = EntityLoader.class.getSimpleName();
    private static final Object mLock = new Object();

    private int mImageId;
    private @ResolutionConst
    String mResolution, mImageType;
    private Entity mEntity;
    // 0 if image type is icon, 1 if image type is image. We use this for optimizing code since
    // evaluation of conditionals take less computing in case of primitive types
    private int mImageIcon;

    // Constructor of ImageLoader. We extract necessary variables from Bundle in order to
    // download appropriate image
    public ImageLoader(@NonNull Context context, Bundle args) {
        super(context);
        mEntity = args.getParcelable(BundleStringArgs.BUNDLE_ENTITY);
        mResolution = args.getString(BundleStringArgs.BUNDLE_RESOLUTION);
        mImageType = args.getString(BundleStringArgs.BUNDLE_IMAGE_TYPE);
        mImageId = mEntity.getId();
        if(mImageType.equalsIgnoreCase(ResolutionConst.ICON)){
            mImageIcon=0;
        } else {
            mImageIcon=1;
        }
    }


    /**
     * Default async download process. When image has benn successfully downloaded, a new
     * reference of Bitmap will be created in order to let this Loader get reused for another
     * image download. We must
     * 1) create new Bitmap
     * 2) set the Bitmap to Entity
     * 3) set PictureDownloaded to true
     * synchronously because if image would set to Entity and Loader would be destroyed at this
     * step then we should download image again because hasPictureDownloaded would remain false...
     * @return - the Id of image
     */
    @Nullable
    @Override
    public Integer loadInBackground() {
        Bitmap bitmap = null;
        try {
            URL url = new URL(HOST + PORT + PATH_PARAM + String.valueOf(mImageId) + "/"+mImageType+"/" + mResolution+"/");
            URLConnection conn = url.openConnection();
            InputStream stream = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
            synchronized (mLock) {
                Bitmap newBitmap = Bitmap.createBitmap(bitmap);
                if(mImageIcon==0) {
                    mEntity.setIconImage(newBitmap);
                    mEntity.setPictureDownloaded(true);
                } else {
                    mEntity.setImage(newBitmap);
                }
            }
            Log.d(LOG_TAG, "Image has been loaded. Image size=" + bitmap.getByteCount()+
                    ", resolution: "+mResolution+", pixels: "+bitmap.getHeight()+"x"+bitmap.getWidth());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "URL error");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "OpenConnection Error");
            e.printStackTrace();
        }
        return this.getId();
    }

    /**
     * Subclasses must implement this to take care of loading their data,
     * as per {@link #startLoading()}.  This is not called by clients directly,
     * but as a result of a call to {@link #startLoading()}.
     * This will always be called from the process's main thread.
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
