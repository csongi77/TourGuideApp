package com.example.csongor.tourguideapp;


import android.os.Bundle;
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
    private static final String BUNDLE_CATEGORY_ID = "BUNDLE_CATEGORY_ID";
    private static final String BUNDLE_ENTITY_ARRAY_LIST = "BUNDLE_ENTITY_ARRAY_LIST";

    // Defining variables /Views/
    private ContentLoadingProgressBar mProgressBar;
    private ListView mListView;
    private TextView mMessage;

    /**
     * The mBundleCategory contains the required categoryId which was passed by NavigationDrawer
     * For possible values check @link{@BundleArgs}
     */
    private Bundle mBundleFromActivity;
    private @BundleArgs
    int mCategoryId;
    private LoaderManager mLoaderManager;
    private Loader<List<Entity>> mEntityLoader;
    private Loader<Integer> mImageLoader;
    private LoaderManager.LoaderCallbacks<List<Entity>> mEntityListLoaderCallback;
    private LoaderManager.LoaderCallbacks<Integer> mImageLoaderCallback;
    private ArrayAdapter<Entity> mArrayAdapter;
    private List<Entity> mEntityList;



    public ListFragmentToDisplay() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View mRootView = inflater.inflate(R.layout.fragment_list_container, container, false);
        // initializing variables
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
        mImageLoaderCallback = new LoaderManager.LoaderCallbacks<Integer>() {
            @NonNull
            @Override
            public Loader<Integer> onCreateLoader(int id, @Nullable Bundle args) {
                Entity place = (Entity) args.get(BUNDLE_CURRENT_ENTITY_WITH_IMAGE);
                mImageLoader = new ImageLoader(getContext(), place);
                return mImageLoader;
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Integer> loader, Integer data) {
                mArrayAdapter.notifyDataSetChanged();
                if (mEntityList != null && !mEntityList.isEmpty())
                    mEntityList.remove(0);
                downloadIcons(mEntityList);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Integer> loader) {
                mEntityLoader = null;
            }
        };

        // Entity Loader Callbacks
        mEntityListLoaderCallback = new LoaderManager.LoaderCallbacks<List<Entity>>() {
            @NonNull
            @Override
            public Loader<List<Entity>> onCreateLoader(int id, @Nullable Bundle args) {
                if (mEntityLoader == null) {
                    return mEntityLoader = new EntityLoader(getContext(), mCategoryId);
                }
                return mEntityLoader;
            }

            @Override
            public void onLoadFinished(@NonNull Loader<List<Entity>> loader, List<Entity> data) {
                Log.d(LOG_TAG, "-------------------------------------->LOAD FINISHED");
                mArrayAdapter = new EntityListAdapter(getContext(), data);
                mListView.setAdapter(mArrayAdapter);
                mProgressBar.hide();
                if (data.get(0) instanceof NullPlace) {
                    mMessage.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                } else {
                    mMessage.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                }
                // todo add Bundle resolution + imageType + imageId
                mEntityList = new ArrayList<>();
                for (Entity place : data
                        ) {
                    if (place.isPictureAvialable() && !place.hasPictureDownloaded())
                        mEntityList.add(place);
                }
                downloadIcons(mEntityList);

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

        mBundleFromActivity = getArguments();
        mLoaderManager = getActivity().getSupportLoaderManager();
        mCategoryId = mBundleFromActivity.getInt(BundleStringArgs.BUNDLE_ENTITY_CATEGORY_TO_LOAD_ARG);
        if (savedInstanceState == null) {
            Log.d(LOG_TAG,"------> restartLoader");
            mLoaderManager.restartLoader(-1, mBundleFromActivity, mEntityListLoaderCallback);
        } else {
            Log.d(LOG_TAG,"------> initLoader");
            mLoaderManager.initLoader(-1, mBundleFromActivity, mEntityListLoaderCallback);
        }

        return mRootView;
    }


    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "---------------> DETACHED");
    }

    private void downloadIcons(List<Entity> entityList) {
        if (!entityList.isEmpty()) {
            Entity place = entityList.get(0);
            Bundle bundlePlace = new Bundle();
            bundlePlace.putParcelable(BUNDLE_CURRENT_ENTITY_WITH_IMAGE, place);
            mLoaderManager.restartLoader(-2, bundlePlace, mImageLoaderCallback);
        }
    }

}
