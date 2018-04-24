package com.example.csongor.tourguideapp.appsupport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.OperationCanceledException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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

    // todo set imageloader args to Bundle -> Entity + resolution + imageType
    public ImageLoader(@NonNull Context context, Entity entity) {
        super(context);
        mEntity = entity;
        mImageId = mEntity.getId();
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
    public Integer loadInBackground() {
        Bitmap bitmap = null;
        try {
            URL url = new URL(HOST + PORT + PATH_PARAM + String.valueOf(mImageId) + "/icon/" + "hdpi");
            URLConnection conn = url.openConnection();
            InputStream stream = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
            synchronized (mLock) {
                Bitmap newBitmap = Bitmap.createBitmap(bitmap);
                mEntity.setIconImage(newBitmap);
                mEntity.setPictureDownloaded(true);
            }
            Log.d(LOG_TAG, "Image has been loaded. Image size=" + bitmap.getByteCount());
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
