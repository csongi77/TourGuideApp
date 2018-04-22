package com.example.csongor.tourguideapp;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.csongor.tourguideapp.appsupport.Entity;
import com.example.csongor.tourguideapp.appsupport.EntityListAdapter;
import com.example.csongor.tourguideapp.appsupport.EntityLoader;
import com.example.csongor.tourguideapp.appsupport.ImageLoader;
import com.example.csongor.tourguideapp.appsupport.NullPlace;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragmentToDisplay extends Fragment {

    // Defining constants
    private static final String LOG_TAG = ListFragmentToDisplay.class.getSimpleName();
    private static final int ENTITY_LOADER = 42;
    private static final Object mLock = new Object();
    private static final String BUNDLE_ENTITIES_WITH_IMAGES = "BUNDLE_ENTITIES_WITH_IMAGES";

    // Defining variables
    private ContentLoadingProgressBar mProgressBar;
    private ListView mListView;
    /**
     * The mBundleLoadArg contains the required categoryId which was passed by NavigationDrawer
     * For possible values check @link{@BundleArgs}
     */
    private Bundle mBundleLoadArg;
    private @BundleArgs
    int mLoadArg;
    private LoaderManager mLoaderManager;
    private Loader<List<Entity>> mEntityLoader;
    private Loader<Bitmap> mImageLoader;
    private LoaderManager.LoaderCallbacks<List<Entity>> mEntityListLoaderCallback;
    private LoaderManager.LoaderCallbacks<Bitmap> mImageLoaderCallback;
    private ArrayAdapter<Entity> mArrayAdapter;
    private List<Entity> mEntityList;
    private List<Entity> mPlacesWithImages;
    private TextView mMessage;
    private Entity mPlaceCurrent;
    //private List<Integer> mPlaceIdsWithImages;


    public ListFragmentToDisplay() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_list_container, container, false);
        // initializing variables
        mListView = mRootView.findViewById(R.id.list_view_container);
        mProgressBar = mRootView.findViewById(R.id.list_view_loading_progress_bar);
        mMessage = mRootView.findViewById(R.id.fragment_root_txt_message);
        mMessage.setVisibility(View.GONE);
        Log.e(LOG_TAG, "--------> onCreateView called, savedInstanceState exist=" + String.valueOf(savedInstanceState != null));

        /**
         *  First of all we have to define Loader Callbacks because there can be running
         *  loaders and on initLoader we must be ready to receive results immediately.
         */
        // Defining image loader callbacks
        mImageLoaderCallback = new LoaderManager.LoaderCallbacks<Bitmap>() {
            @NonNull
            @Override
            public Loader<Bitmap> onCreateLoader(int id, @Nullable Bundle args) {
                mImageLoader = new ImageLoader(getContext(), args);
                Log.d(LOG_TAG, "---> onCreateLoader, imageLoader created");
                return mImageLoader;
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Bitmap> loader, Bitmap data) {
                /*
                When finished download of an image, make a new reference of it because
                Loader will destroyed as the original reference of picture.
                Creating new Bitmap, assigning it to Entity and setting it that Picture
                has been downloaded must be synchronized.
                For example when new picture reference has been created but hasn't been added
                to Entity, on calling an event (that calls onSaveInstanceState) could interrupt
                this process.
                 */
                synchronized (mLock) {
                    Bitmap b = Bitmap.createBitmap(data);
                    mPlaceCurrent.setIconImage(b);
                    mPlaceCurrent.setPictureDownloaded(true);
                    mArrayAdapter.notifyDataSetChanged();
                    int toDestroy = mPlaceCurrent.getId();
                    mLoaderManager.destroyLoader(toDestroy);
                }
                Log.d(LOG_TAG, "----> NotifyDatasetChanged called");
                if (!mPlacesWithImages.isEmpty()) {
                    // If there are more Entity with own images to download, continue loading process
                    startDownloadImages(mPlacesWithImages);
                }


            }

            @Override
            public void onLoaderReset(@NonNull Loader<Bitmap> loader) {
                Log.d(LOG_TAG, "----> ImageLoader reset called");
                // Set mImageLoader to null in order to free up resources
                mImageLoader = null;
            }
        };

        /**
         * Implementing Callbacks of EntityLoader in order to retrieve Entity list asynchronously
         */
        mEntityListLoaderCallback = new LoaderManager.LoaderCallbacks<List<Entity>>() {

            @NonNull
            @Override
            public Loader<List<Entity>> onCreateLoader(int id, Bundle args) {
                Log.d(LOG_TAG, "---> onCreateLoader");
                // set up EntityLoader. args contains categoryId.
                if (mEntityLoader == null) {
                    mEntityLoader = new EntityLoader(getContext(), args);
                }
                return mEntityLoader;
            }

            @Override
            public void onLoadFinished(@NonNull Loader<List<Entity>> loader, List<Entity> data) {
                Log.d(LOG_TAG, "---> onLoadFinished");
                mProgressBar.hide();
                if (mEntityList == null) {
                    mEntityList = data;
                    mArrayAdapter = new EntityListAdapter(getContext(), mEntityList);
                    mListView.setAdapter(mArrayAdapter);
                    if (!(mEntityList.get(0) instanceof NullPlace)) {
                        mListView.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                        mLoaderManager.destroyLoader(ENTITY_LOADER);

                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        mMessage.setVisibility(View.VISIBLE);
                        mLoaderManager.destroyLoader(ENTITY_LOADER);
                    }
                } else {
                    mEntityList.clear();
                    mEntityList.addAll(data);
                }
                mPlacesWithImages = new ArrayList<>();
                for (Entity e : mEntityList
                        ) {
                    if (e.isPictureAvialable() && !e.hasPictureDownloaded())
                        mPlacesWithImages.add(e);
                }
                startDownloadImages(mPlacesWithImages);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<List<Entity>> loader) {
                Log.d(LOG_TAG, "---> onLoaderReset");
                mEntityList = null;
            }
        };

        // Getting bundle arguments for determining which Place Category should be loaded.
        mBundleLoadArg = getArguments();
        Log.d(LOG_TAG, "--------> onCreateView called, mBundleLoadArg: " + mBundleLoadArg.getInt(BundleStringArgs.BUNDLE_TO_LOAD_ARG));


        mLoaderManager = getActivity().getSupportLoaderManager();
        if (mLoaderManager.hasRunningLoaders()) {
            mLoaderManager.restartLoader(ENTITY_LOADER, mBundleLoadArg, mEntityListLoaderCallback);
        } else {
            mEntityLoader = mLoaderManager.initLoader(ENTITY_LOADER, mBundleLoadArg, mEntityListLoaderCallback);
        }
        if (savedInstanceState == null) {
            mProgressBar.show();
            mEntityLoader.forceLoad();
        } else {
            mEntityList = savedInstanceState.getParcelableArrayList(BundleStringArgs.BUNDLE_PARCELABLE_ENTITY_ARRAY_LIST);
            if (mEntityList != null) {
                mArrayAdapter = new EntityListAdapter(getContext(), mEntityList);
                mListView.setAdapter(mArrayAdapter);
                mListView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mProgressBar.hide();
                // Don't display data after screen orientation change if there were no valid Entities
                if (mEntityList.get(0) instanceof NullPlace) {
                    mMessage.setVisibility(View.VISIBLE);
                }
            }
        }
        return mRootView;
    }


    /**
     * Saving EntityList into Bundle in order to avoid reloading data, and sparing network traffic
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(LOG_TAG, "------> Fragment onSaveInstanceState called");
        if (mEntityList != null)
            outState.putParcelableArrayList(BundleStringArgs.BUNDLE_PARCELABLE_ENTITY_ARRAY_LIST, new ArrayList<Parcelable>(mEntityList));
            outState.putParcelableArrayList(BUNDLE_ENTITIES_WITH_IMAGES,new ArrayList<Parcelable>(mPlacesWithImages));
        super.onSaveInstanceState(outState);

    }

    private void startDownloadImages(List<Entity> mEntityList) {
        Log.d(LOG_TAG, "------> START download image");
        if (!mEntityList.isEmpty()) {
            Log.d(LOG_TAG, "------> START download image////mEntityList is not empty");
            Bundle placeBundle = new Bundle();
            mPlaceCurrent = mEntityList.remove(0);
            placeBundle.putParcelable(BundleStringArgs.BUNDLE_ENTITY, mPlaceCurrent);
            mImageLoader = mLoaderManager.restartLoader(mPlaceCurrent.getId(), placeBundle, mImageLoaderCallback);
            mImageLoader.forceLoad();
        }
    }


}
