package com.example.csongor.tourguideapp;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
    // Defining variables /Views/
    private ContentLoadingProgressBar mProgressBar;
    private ListView mListView;
    private TextView mMessage;
    private @BundleArgs
    int mCategoryId;
    private @BundleStringArgs
    String mResolution;
    private LoaderManager mLoaderManager;
    private Loader<List<Entity>> mEntityLoader;
    private Loader<Bitmap> mImageLoader;
    private LoaderManager.LoaderCallbacks<Bitmap> mImageLoaderCallback;
    private ArrayAdapter<Entity> mArrayAdapter;
    private List<Entity> mEntityListWithImages, mEntityList;


    public ListFragmentToDisplay() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View mRootView = inflater.inflate(R.layout.fragment_list_container, container, false);
        // initializing View variables
        mListView = mRootView.findViewById(R.id.list_view_container);
        mListView.setVisibility(View.GONE);
        mProgressBar = mRootView.findViewById(R.id.list_view_loading_progress_bar);
        mProgressBar.show();
        mMessage = mRootView.findViewById(R.id.fragment_root_txt_message);
        mMessage.setVisibility(View.GONE);

        //------------------------------------------------------------------------------------------
        // LOADER CALLBACKS start
        // -----------------------------------------------------------------------------------------

        // Image Loader Callbacks
        mImageLoaderCallback = new LoaderManager.LoaderCallbacks<Bitmap>() {
            @NonNull
            @Override
            public Loader<Bitmap> onCreateLoader(int id, @Nullable Bundle args) {
                mImageLoader = new ImageLoader(getContext(), args);
                return mImageLoader;
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Bitmap> loader, Bitmap data) {
                /**
                 * When image loaded successfully the related Entity will be removed from the list,
                 * We create a new Bitmap reference to Entity, and attach the
                 * new Bitmap instance to Entity.
                 * Then the reduced list will passed back to downloadIcons method. Details can be found at method's
                 * description
                 */
                if (mEntityListWithImages != null && !mEntityListWithImages.isEmpty()) {
                    if (data != null) {
                        // sychronized steps in order to avoid data loss (for example at orientation change)
                        synchronized (mLock) {
                            Bitmap bitmapToSet = Bitmap.createBitmap(data);
                            Entity entity = mEntityListWithImages.remove(0);
                            entity.setIconImage(bitmapToSet);
                            entity.setPictureDownloaded(true);
                        }
                        mArrayAdapter.notifyDataSetChanged();
                    } else {
                        // removing 0th element regardless data was null in order to avoid infinite loop
                        mEntityListWithImages.remove(0);
                    }
                }
                downloadIcons(mEntityListWithImages);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Bitmap> loader) {
                mEntityLoader = null;
            }
        };

        // Entity Loader Callbacks
        LoaderManager.LoaderCallbacks<List<Entity>> mEntityListLoaderCallback = new LoaderManager.LoaderCallbacks<List<Entity>>() {
            @NonNull
            @Override
            public Loader<List<Entity>> onCreateLoader(int id, @Nullable Bundle args) {
                if (mEntityLoader == null) {
                    return mEntityLoader = new EntityLoader(getContext(), mCategoryId);
                }
                return mEntityLoader;
            }

            /**
             * When Entity list successfully downloaded from the server via REST, this callback
             * method will generate a new list containing the reference of Entities which has
             * own Images and Icons located on server
             * @param loader - the Loader object
             * @param data - the returned Entity List related to selected category
             */
            @Override
            public void onLoadFinished(@NonNull Loader<List<Entity>> loader, List<Entity> data) {
                Log.d(LOG_TAG, "-------------------------------------->LOAD FINISHED");
                mEntityList = data;
                mArrayAdapter = new EntityListAdapter(getContext(), mEntityList);
                mListView.setAdapter(mArrayAdapter);
                mListView.setOnItemClickListener((parent, view, position, id) ->
                        openDetails(position)
                );
                mProgressBar.hide();
                if (data.get(0) instanceof NullPlace) {
                    // if there were no results we show a message ("No results found")
                    mMessage.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                } else {
                    // else we show the Entity List
                    mMessage.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                }
                /**
                 * The following code selects the Entities which has own icons other then default and
                 * hasn't been downloaded yet.
                 */
                mEntityListWithImages = new ArrayList<>();
                for (Entity place : mEntityList
                        ) {
                    if (place.isPictureAvialable() && !place.hasPictureDownloaded())
                        mEntityListWithImages.add(place);
                }
                // starting download icon images asynchronously.
                downloadIcons(mEntityListWithImages);

            }

            @Override
            public void onLoaderReset(@NonNull Loader<List<Entity>> loader) {
                Log.d(LOG_TAG, "----------------> Entity onLoaderReset has been called");
                mProgressBar.show();
                mMessage.setVisibility(View.GONE);
                mListView.setVisibility(View.GONE);
            }
        };

        //------------------------------------------------------------------------------------------
        // LOADER CALLBACKS end
        //------------------------------------------------------------------------------------------

        // Loading Bundle arguments passed by MainActivity
        /*
      The mBundleCategory contains the required categoryId which was passed by NavigationDrawer
      For possible values check @link{@BundleArgs}
     */
        Bundle mBundleFromActivity = getArguments();
        mCategoryId = mBundleFromActivity.getInt(BundleStringArgs.BUNDLE_ENTITY_CATEGORY);
        mResolution = ((MainActivity) getActivity()).getmResolution();
        /**
         * getting LoaderManager.
         * If savedInstanceState is null, it means that this Fragment
         * was opened first time from Main Activity. In this case we have to restartLoader with
         * appropriate arguments since we use same Loader ID for loading Entity Lists of all
         * categories.
         * If savedInstanceState exists, it means that some config has changed, for example screen
         * rotation. In this case we can use initLoader for avoiding unnecessary network traffic.
         */
        mLoaderManager = getActivity().getSupportLoaderManager();
        if (savedInstanceState == null) {
            Log.d(LOG_TAG, "------> restartLoader");
            mLoaderManager.restartLoader(-1, mBundleFromActivity, mEntityListLoaderCallback);
        } else {
            Log.d(LOG_TAG, "------> initLoader");
            mLoaderManager.initLoader(-1, mBundleFromActivity, mEntityListLoaderCallback);
        }

        return mRootView;
    }


    /**
     * Overriding onSaveInstanceState we can assure to saveInstanceState Bundle will exists.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(BundleStringArgs.BUNDLE_ENTITY_CATEGORY, mCategoryId);
        outState.putString(BundleStringArgs.BUNDLE_RESOLUTION, mResolution);
        super.onSaveInstanceState(outState);
    }


    /**
     * Helper method for loading icon images to ArrayList asynchronously. This method is called from
     * A) EntityListLoader Callback's onFinished method (when EntityList has been created)
     * B) ImageLoader Callback's onFinished method (when an Image instance has been downloaded)
     * The method takes the first Entity, passes it to ImageLoader. Then ImageLoader downloads
     * image in appropriate resolution, Then the onLoadFinished callback method will be called.
     * Afterwards the first element will removed from the EntityList and will passed back to this
     * method until all images would be downloaded.
     * Becase of this we can use only one instance of this AsyncTaskLoader.
     *
     * @param entityList - Entity list with Entity instances which has images other than default
     *                   and hasn't been that image dowloaded yet.
     */
    private void downloadIcons(List<Entity> entityList) {
        if (!entityList.isEmpty()) {
            Entity place = entityList.get(0);
            /**
             * Creating Bundle which is necessary for downloading appropriate image:
             * 1) Entity which contains imageId to download
             * 2) Resolution for determining which image must be downloaded depending on
             *      device's resolution for optimizing network traffic
             * 3) Icon - since we using same algorithm for downloading images and icons
             *      we must send this constant.
             */
            Bundle bundlePlace = new Bundle();
            bundlePlace.putParcelable(BundleStringArgs.BUNDLE_ENTITY, place);
            bundlePlace.putString(BundleStringArgs.BUNDLE_RESOLUTION, mResolution);
            bundlePlace.putString(BundleStringArgs.BUNDLE_IMAGE_TYPE, ResolutionConst.ICON);
            mLoaderManager.restartLoader(-2, bundlePlace, mImageLoaderCallback);
        }
    }

    /**
     * helper method for onItemClickListener. This method will show the description of the
     * Entity
     *
     * @param position - the position of List<Entity>
     */
    private void openDetails(int position) {
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        // add some animation to Fragment change
        mFragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

        // add Entity to Bundle
        Entity entity = mEntityList.get(position);
        Bundle detailsToSend = new Bundle();
        detailsToSend.putParcelable(BundleStringArgs.BUNDLE_ENTITY, entity);

        // put this Fragment into BackStack in order to avoid reloading it after onBackPressed
        // In this case we can reduce unnecessary network traffic
        mFragmentTransaction.addToBackStack(FragmentStringArgs.LIST_FRAGMENT);
        Fragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(detailsToSend);
        mFragmentTransaction.replace(R.id.fragment_container, detailsFragment);
        mFragmentTransaction.commit();
    }


}
