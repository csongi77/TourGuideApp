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
    private static final Object mLock = new Object();
    private static final String BUNDLE_ENTITIES_WITH_IMAGES = "BUNDLE_ENTITIES_WITH_IMAGES";
    private static final String BUNDLE_CURRENT_ENTITY_WITH_IMAGE = "BUNDLE_CURRENT_ENTITY_WITH_IMAGE";

    // Defining variables
    private ContentLoadingProgressBar mProgressBar;
    private ListView mListView;
    /**
     * The mBundleCategory contains the required categoryId which was passed by NavigationDrawer
     * For possible values check @link{@BundleArgs}
     */
    private Bundle mBundleCategory;
    private @BundleArgs
    int mCategoryId;
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
        mListView.setVisibility(View.GONE);
        mProgressBar = mRootView.findViewById(R.id.list_view_loading_progress_bar);
        mProgressBar.show();
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
                //  if (mImageLoader == null) {
                mImageLoader = new ImageLoader(getContext(), args);
                Log.d(LOG_TAG, "---> onCreateLoader, imageLoader created");
                //  }
                mImageLoader.forceLoad();
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
                mEntityLoader.forceLoad();
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
                        mLoaderManager.destroyLoader(mCategoryId);

                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        mMessage.setVisibility(View.VISIBLE);
                        mLoaderManager.destroyLoader(mCategoryId);
                    }
                } else {
                    mEntityList.clear();
                    mEntityList.addAll(data);
                }
                // create ArrayList of Entites with own images
                synchronized (mLock) {
                    mPlacesWithImages = new ArrayList<>();
                    for (Entity e : mEntityList
                            ) {
                        if (e.isPictureAvialable() && !e.hasPictureDownloaded())
                            mPlacesWithImages.add(e);
                    }
                }
                startDownloadImages(mPlacesWithImages);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<List<Entity>> loader) {
                Log.d(LOG_TAG, "---> onLoaderReset");
                mArrayAdapter=null;
                mEntityList=null;
                mEntityLoader=null;
            }
        };

        // Getting bundle arguments for determining which Place Category should be loaded.
        mBundleCategory = getArguments();
        mCategoryId = mBundleCategory.getInt(BundleStringArgs.BUNDLE_ENTITY_CATEGORY_TO_LOAD_ARG) * -1;
        Log.d(LOG_TAG, "--------> onCreateView called, mBundleCategory: " + mBundleCategory.getInt(BundleStringArgs.BUNDLE_ENTITY_CATEGORY_TO_LOAD_ARG));

/**
 * 1) Check savedInstanceState. If it's null it means that new Fragment has been instantiated.
 *  However, there might be active loaders with same loader ID For example, Historical Places menu
 *  option was selected in Navigation Drawer but BEFORE Entities were loaded Restaurants were selected.
 *  In this case there might be an active loader downloading Historical Places' Entities.
 *  So if savedInstanceState is null:
 *  1.TRUE) reset mEntityLoader and start download Entities with arguments retrieved from mBundleArg
 *  1.FALSE)
 *      1.F.1) This means that Fragment instance view has been recreated (i.e. after screen rotation).
 *      We must ensure which loading phase is active:
 *          a) Loading Entities OR
 *          b) loading images into existing Entity list.
 *      So try to retrieve mEnityList from savedInstanceState bundle. Check that whether the result is null.
 *      1.F.1.TRUE) This means that mEntityList values hasn't been retrieved yet.
 *          Init ENTITY_LOADER with mEntityListLoaderCallback.
 *      1.F.1.FALSE) In this case mEntityList exists and mPlacesWithImages has been already created.
 *          Set up mArrayAdapter
 *          Assign it to mListView
 *          Set up visibilities
 *          Check whether IMAGE_LOADER is still active:
 *              1.F.1.F.1) If it is, initLoader(IMAGE_LOADER).
 *              1.F.1.F.2) Else retrive mPlacesWithImages from saved Bundle and
 *                          call startDownLoadImages method with mPlacesWithImages list.
 *
 */
        mLoaderManager = getActivity().getSupportLoaderManager();
        if (savedInstanceState == null) {
            mLoaderManager.initLoader(mCategoryId, mBundleCategory, mEntityListLoaderCallback);
            // mEntityLoader.forceLoad();
        } else {
            mEntityList = savedInstanceState.getParcelableArrayList(BundleStringArgs.BUNDLE_PARCELABLE_ENTITY_ARRAY_LIST);
            if (mEntityList == null) {
                mLoaderManager.initLoader(mCategoryId, mBundleCategory, mEntityListLoaderCallback);
            } else {
                mArrayAdapter = new EntityListAdapter(getContext(), mEntityList);
                mListView.setAdapter(mArrayAdapter);
                mProgressBar.hide();
                if (mEntityList.isEmpty() || mEntityList.get(0) instanceof NullPlace) {
                    mMessage.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                } else {
                    mListView.setVisibility(View.VISIBLE);
                    mMessage.setVisibility(View.GONE);
                }
                if (mLoaderManager.hasRunningLoaders()) {
                    mLoaderManager.initLoader(mPlacesWithImages.get(0).getId(), null, mImageLoaderCallback);
                } else {
                    mPlacesWithImages = savedInstanceState.getParcelableArrayList(BUNDLE_ENTITIES_WITH_IMAGES);
                    startDownloadImages(mPlacesWithImages);
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
        outState.putInt(BundleStringArgs.BUNDLE_ENTITY_CATEGORY_TO_LOAD_ARG, mCategoryId);
        outState.putInt(BUNDLE_CURRENT_ENTITY_WITH_IMAGE,mPlaceCurrent);
        if (mEntityList != null)
            outState.putParcelableArrayList(BundleStringArgs.BUNDLE_PARCELABLE_ENTITY_ARRAY_LIST, new ArrayList<Parcelable>(mEntityList));
        if (mPlacesWithImages != null)
            outState.putParcelableArrayList(BUNDLE_ENTITIES_WITH_IMAGES, new ArrayList<Parcelable>(mPlacesWithImages));
        super.onSaveInstanceState(outState);

    }

    private void startDownloadImages(List<Entity> mEntityList) {
        Log.d(LOG_TAG, "------> START download image");
        if (!mEntityList.isEmpty()) {
            Log.d(LOG_TAG, "------> START download image////mEntityList is not empty");
            synchronized (mLock) {
                Bundle placeBundle = new Bundle();
                mPlaceCurrent = mEntityList.remove(0);
                placeBundle.putParcelable(BundleStringArgs.BUNDLE_ENTITY, mPlaceCurrent);
                mImageLoader = mLoaderManager.restartLoader(mPlaceCurrent.getId(), placeBundle, mImageLoaderCallback);
                mImageLoader.forceLoad();
            }
        }
    }


}
