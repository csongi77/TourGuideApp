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
import android.widget.TextView;

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
    private TextView mMessage;


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
        Log.e(LOG_TAG, "--------> onCreateView called");
        if (savedInstanceState == null) {
            // retrieving Bundle args
            mBundleLoadArg = getArguments();
            Log.d(LOG_TAG, "--------> onCreateView called, mBundleLoadArg: " + mBundleLoadArg.getInt(BundleStringArgs.BUNDLE_TO_LOAD_ARG));
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
                mProgressBar.show();
                mLoaderManager = getActivity().getSupportLoaderManager();
                mEntityLoader = mLoaderManager.initLoader(ENTITY_LOADER, mBundleLoadArg, mEntityListLoaderCallback);
                mEntityLoader.forceLoad();
            }
        }
        return mRootView;

    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(LOG_TAG, "------> Fragment onViewCreated called");
        if (savedInstanceState != null) {
            Log.d(LOG_TAG, "------> Fragment onViewStateRestored called");
            mEntityList = savedInstanceState.getParcelableArrayList(BundleStringArgs.BUNDLE_PARCELABLE_ENTITY_ARRAY_LIST);
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

    /**
     * Called when the view previously created by {@link #onCreateView} has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after {@link #onStop()} and before {@link #onDestroy()}.  It is called
     * <em>regardless</em> of whether {@link #onCreateView} returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "------> Fragment onDestroyView called");

    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "------> Fragment onDestroy called");
    }

    /**
     * Saving EntityList into Bundle in order to avoid reloading data, and sparing network traffic
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(LOG_TAG, "------> Fragment onSaveInstanceState called");
            outState.putParcelableArrayList(BundleStringArgs.BUNDLE_PARCELABLE_ENTITY_ARRAY_LIST, new ArrayList<Parcelable>(mEntityList));
        super.onSaveInstanceState(outState);

    }


    //todo check the List,
    //todo setUp AdapterView
    // todo: create ArrayAdapter for EntityList
    // todo: make Loader for loading images
    //Add List to AdapterView

}
