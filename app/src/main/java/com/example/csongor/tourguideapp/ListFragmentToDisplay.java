package com.example.csongor.tourguideapp;


import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.example.csongor.tourguideapp.appsupport.Entity;
import com.example.csongor.tourguideapp.appsupport.EntityListAdapter;
import com.example.csongor.tourguideapp.appsupport.EntityLoader;
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
    private LoaderManager.LoaderCallbacks<List<Entity>> mEntityListLoaderCallback;
    private ArrayAdapter<Entity> mArrayAdapter;
    private List<Entity> mEntityList;


    public ListFragmentToDisplay() {
        mEntityLoader = null;
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

        // retrieving Bundle args
        mBundleLoadArg = getArguments();
        Log.d(LOG_TAG, "---> mBundleLoadArg: " + mBundleLoadArg.getInt(BundleStringArgs.BUNDLE_TO_LOAD_ARG));
/* todo put it in EntityLoaderCallback
        mProgressBar.setVisibility(View.GONE);
        mProgressBar.hide();
        mListView.setVisibility(View.VISIBLE);
*/
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
                if (mEntityList == null) {
                    // if (!(data.get(0) instanceof NullPlace)) {
                    mEntityList = data;
                    mArrayAdapter = new EntityListAdapter(getContext(), mEntityList);
                    mListView.setAdapter(mArrayAdapter);
                    mListView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mLoaderManager.destroyLoader(ENTITY_LOADER);
                    //  }
                } else {
                    mEntityList.clear();
                    mEntityList.addAll(data);
                    mArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<List<Entity>> loader) {
                Log.d(LOG_TAG, "---> onLoaderReset");
                mEntityList = null;
                mProgressBar.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                mEntityLoader.abandon();
                mEntityLoader = null;
                mLoaderManager.destroyLoader(ENTITY_LOADER);
            }
        };

        if (mEntityList == null) {
        mLoaderManager = getActivity().getSupportLoaderManager();
        mEntityLoader = mLoaderManager.initLoader(ENTITY_LOADER, mBundleLoadArg, mEntityListLoaderCallback);
            mEntityLoader.forceLoad();
        }
        return mRootView;
//mBundleLoadArg.getInt(BundleStringArgs.BUNDLE_TO_LOAD_ARG)
    }



    //todo check the List,
    //todo setUp AdapterView
    // todo: create ArrayAdapter for EntityList
    // todo: make Loader for loading images
    //Add List to AdapterView

}
