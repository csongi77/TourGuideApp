package com.example.csongor.tourguideapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragmentToDisplay extends Fragment {

    // Defining constants
    private static final String LOG_TAG=ListFragmentToDisplay.class.getSimpleName();
    // Defining variables
    private ContentLoadingProgressBar mProgressBar;
    private View mListView;
    private Bundle mBundleLoadArg;
    private @BundleArgs int mLoadArg;

    public ListFragmentToDisplay() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_list_container,container,false);
        // initializing variables
        mListView = mRootView.findViewById(R.id.list_view_container);
        mProgressBar=mRootView.findViewById(R.id.list_view_loading_progress_bar);

        // retrieving Bundle args
        mBundleLoadArg = getArguments();
        mLoadArg=mBundleLoadArg.getInt(BundleStringArgs.BUNDLE_TO_LOAD_ARG);

        mProgressBar.setVisibility(View.GONE);
        mProgressBar.hide();
        mListView.setVisibility(View.VISIBLE);
        if(mLoadArg!=0){
            Log.d(LOG_TAG,"-----> Bundle Arg: "+mLoadArg);
        } else {
            Log.d(LOG_TAG,"savedInstanceState null");
        }
        return mRootView;

    }
    //todo: implement v4.AsyncTaskloader.Callbacks
}
