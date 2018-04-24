package com.example.csongor.tourguideapp.appsupport;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.csongor.tourguideapp.R;

import java.util.List;

public class EntityListAdapter extends ArrayAdapter<Entity> {

    // Maximum character number of Entity's description depending on Orientation
    private static final int PORTRAIT_DESCRIPTION_LENTGH = 40;
    private static final int LANDSCAPE_DESCRIPTION_LENTGH = 73;

    private View mRootView;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param entities The objects to represent in the ListView.
     */
    public EntityListAdapter(@NonNull Context context, @NonNull List<Entity> entities) {
        super(context, R.layout.fragment_list_container, entities);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // inflate appropriate view if it's not initialized yet
        mRootView = convertView;
        if (mRootView == null) {
            mRootView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Entity place = getItem(position);

        // assigning Entity's values to appropriate TextViews
        TextView title = mRootView.findViewById(R.id.list_item_txt_title);
        title.setText(place.getTitle());
        TextView address = mRootView.findViewById(R.id.list_item_txt_address);
        address.setText(place.getAddress());
        TextView open = mRootView.findViewById(R.id.list_item_txt_datetime);
        open.setText(place.getFromTo());

        // check device's orientation
        Configuration config = getContext().getResources().getConfiguration();

        // cut Entities description - if it's too long - to appropriate value.
        TextView description = mRootView.findViewById(R.id.list_item_txt_description);
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (place.getDescription().length() > PORTRAIT_DESCRIPTION_LENTGH + 3) {
                StringBuilder builder = new StringBuilder();
                builder.append(place.getDescription().substring(0, PORTRAIT_DESCRIPTION_LENTGH))
                        .append("...");
                description.setText(builder.toString());
            } else {
                description.setText(place.getDescription());
            }
        } else {
            if (place.getDescription().length() > LANDSCAPE_DESCRIPTION_LENTGH + 3) {
                StringBuilder builder = new StringBuilder();
                builder.append(place.getDescription().substring(0, LANDSCAPE_DESCRIPTION_LENTGH))
                        .append("...");
                description.setText(builder.toString());
            } else {
                description.setText(place.getDescription());
            }
        }

        // checking children and pet allowance. If false, change icon color to red
        ImageView childFriendly = mRootView.findViewById(R.id.list_item_icon_child_friendly);
        GradientDrawable childFriendlyBackground = (GradientDrawable) childFriendly.getBackground();
        if (!place.isChildFriendly()) {
            childFriendlyBackground.setColor(ContextCompat.getColor(getContext(), R.color.color_prohibited));
        } else {
            childFriendlyBackground.setColor(ContextCompat.getColor(getContext(), R.color.color_allowed));
        }
        ImageView petsAllowed = mRootView.findViewById(R.id.list_item_icon_pets_allowed);

        GradientDrawable petsBackground = (GradientDrawable) petsAllowed.getBackground();
        if (!place.isPetAllowed()) {
            petsBackground.setColor(ContextCompat.getColor(getContext(), R.color.color_prohibited));
        } else {
            petsBackground.setColor(ContextCompat.getColor(getContext(), R.color.color_allowed));
        }
        ImageView icon = mRootView.findViewById(R.id.list_item_ic_image);
        icon.setImageBitmap(place.getIconImage());
        return mRootView;
    }
}
