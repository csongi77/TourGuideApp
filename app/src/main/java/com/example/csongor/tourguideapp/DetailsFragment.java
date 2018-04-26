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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.csongor.tourguideapp.appsupport.Entity;
import com.example.csongor.tourguideapp.appsupport.ImageLoader;

public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Bitmap>{

    private static final int LOADER_ID=-3;
    private static final int EVENT_CATEGORY=2;

    private View mRootView;
    private TextView mTitle, mAddress, mDescription, mChildFriendly,
            mPetsAllowed, mWorkingHours, mDateTime;
    private ImageView mImage;
    private ContentLoadingProgressBar mProgressBar;
    private LinearLayout mImageContainer;
    private Entity mEntity;
    private LoaderManager mLoaderManager;
    private Loader<Bitmap> mImageLoader;

    public DetailsFragment(){

    }


    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mRootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_entity_details,container,false);

        // getting Entity from Bundle arguments
        Bundle data=getArguments();
        mEntity=data.getParcelable(BundleStringArgs.BUNDLE_ENTITY);

        // assigning views to variables and adding values from Entity
        mTitle=mRootView.findViewById(R.id.details_txt_title);
        mTitle.setText(mEntity.getTitle());

        mDescription=mRootView.findViewById(R.id.details_txt_description);
        mDescription.setText(mEntity.getDescription());

        mAddress=mRootView.findViewById(R.id.details_txt_address);
        mAddress.setText(mEntity.getAddress());

        // setting up Child and Pet friendly values. If not children friendly or pets not allowed,
        // then setting text color to red
        mChildFriendly=mRootView.findViewById(R.id.details_txt_children_friendly);
        if(mEntity.isChildFriendly()){
            mChildFriendly.setText(R.string.yes);
            mChildFriendly.setTextColor(ContextCompat.getColor(getContext(),R.color.color_allowed));
        } else {
            mChildFriendly.setText(R.string.no);
            mChildFriendly.setTextColor(ContextCompat.getColor(getContext(),R.color.color_prohibited));
        }

        mPetsAllowed=mRootView.findViewById(R.id.details_txt_pets_allowed);
        if(mEntity.isPetAllowed()){
            mPetsAllowed.setText(R.string.yes);
            mPetsAllowed.setTextColor(ContextCompat.getColor(getContext(),R.color.color_allowed));
        } else {
            mPetsAllowed.setText(R.string.no);
            mPetsAllowed.setTextColor(ContextCompat.getColor(getContext(),R.color.color_prohibited));
        }

        // setting up opening time. If category id == Event (2) then change text to "Event Date:"
        mWorkingHours=mRootView.findViewById(R.id.details_txt_working_hours);
        mWorkingHours.setText(mEntity.getFromTo());
        mDateTime=mRootView.findViewById(R.id.details_txt_working_hours_or_event_date);
        if(mEntity.getCategoryId()==EVENT_CATEGORY){
            mDateTime.setText(R.string.event_date);
        } else {
            mDateTime.setText(R.string.working_hours);
        }

        //  Set up image. If Entity doesn't have downloadable photo, we hide imageView. Else dowload it.
        mImage=mRootView.findViewById(R.id.details_image);
        mProgressBar=mRootView.findViewById(R.id.details_image_loader);
        mImageContainer=mRootView.findViewById(R.id.details_image_container);
        if(!mEntity.isPictureAvialable()){
            mImageContainer.setVisibility(View.GONE);
        } else {
            LoaderManager mLoaderManager=getActivity().getSupportLoaderManager();
            Bundle bundlePlace = new Bundle();
            bundlePlace.putParcelable(BundleStringArgs.BUNDLE_ENTITY, mEntity);
            bundlePlace.putString(BundleStringArgs.BUNDLE_RESOLUTION, ((MainActivity)getActivity()).getmResolution());
            bundlePlace.putString(BundleStringArgs.BUNDLE_IMAGE_TYPE, ResolutionConst.IMAGE);
            if(savedInstanceState==null) {
                mImageLoader = mLoaderManager.restartLoader(LOADER_ID, bundlePlace, this);
            } else {
                mImageLoader = mLoaderManager.initLoader(LOADER_ID, bundlePlace, this);
            }
        }

        return mRootView;
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mImageContainer.setVisibility(View.VISIBLE);
        mImageLoader=null;
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     * <p>
     * <p>This will always be called from the process's main thread.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        mImageLoader=new ImageLoader(getContext(),args);
        return mImageLoader;
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context, * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     * <p>
     * <p>This will always be called from the process's main thread.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Bitmap> loader, Bitmap data) {
        if(data!=null) {
            mImage.setImageBitmap(data);
            mImage.setVisibility(View.VISIBLE);
            mProgressBar.hide();
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     * <p>
     * <p>This will always be called from the process's main thread.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Bitmap> loader) {
        mProgressBar.show();
        mImage.setVisibility(View.GONE);
    }
}
