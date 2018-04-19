package com.example.csongor.tourguideapp;


import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.example.csongor.tourguideapp.appsupport.Entity;
import com.example.csongor.tourguideapp.appsupport.EntityLoader;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragmentToDisplay extends Fragment {

    // Defining constants
    private static final String LOG_TAG=ListFragmentToDisplay.class.getSimpleName();
    private static final int ENTITY_LOADER=42;
    // Defining variables
    private ContentLoadingProgressBar mProgressBar;
    private View mListView;
    /**
     * The mBundleLoadArg contains the required categoryId which was passed by NavigationDrawer
     * For possible values check @link{@BundleArgs}
     */
    private Bundle mBundleLoadArg;
    private @BundleArgs int mLoadArg;
    private LoaderManager mLoaderManager;
    private AsyncTaskLoader<List<Entity>> mEntityLoader;
    private LoaderManager.LoaderCallbacks<List<Entity>> mEntityListLoaderCallback;

    private List<Entity> mEntityList;


    public ListFragmentToDisplay() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_list_container,container,false);
        // initializing variables
        mListView = mRootView.findViewById(R.id.list_view_container);
        mProgressBar=mRootView.findViewById(R.id.list_view_loading_progress_bar);

        // retrieving Bundle args
        mBundleLoadArg = getArguments();

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
                // set up EntityLoader. args contains categoryId.
                if(mEntityLoader==null){
                    mEntityLoader=new EntityLoader(getContext(), args);
                }
                return mEntityLoader;
            }

            @Override
            public void onLoadFinished(@NonNull Loader<List<Entity>> loader, List<Entity> data) {

            }

            @Override
            public void onLoaderReset(@NonNull Loader<List<Entity>> loader) {

            }
        }

        mLoaderManager=getActivity().getSupportLoaderManager();
        mEntityLoader=mLoaderManager.initLoader(ENTITY_LOADER, mBundleLoadArg, mEntityListLoaderCallback);
        mEntityLoader.forceLoad();

        return mRootView;

    }


    //todo check the List,
    //todo setUp AdapterView
    // todo: create ArrayAdapter for EntityList
    // todo: make Loader for loading images
    //Add List to AdapterView

}
