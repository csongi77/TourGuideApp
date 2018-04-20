package com.example.csongor.tourguideapp.appsupport;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.csongor.tourguideapp.R;

import java.util.List;

public class EntityListAdapter extends ArrayAdapter<Entity> {

    private View mRootView;
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param entities  The objects to represent in the ListView.
     */
    public EntityListAdapter(@NonNull Context context,  @NonNull List<Entity> entities) {
        super(context, R.layout.fragment_list_container, entities);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // inflate appropriate view if it's not initialized yet
        mRootView=convertView;
        if(mRootView==null) {
           /* LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            mRootView = inflater.inflate(R.layout.list_item, parent, false);*/
           mRootView=LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Entity place=getItem(position);
        // assigning Entity's values to appropriate TextViews
        TextView title=mRootView.findViewById(R.id.list_item_txt_title);
        title.setText(place.getTitle());
        TextView address=mRootView.findViewById(R.id.list_item_txt_address);
        address.setText(place.getAddress());
        TextView description=mRootView.findViewById(R.id.list_item_txt_description);
        // cut Entities description to maximum 40 chars.
        if(place.getDescription().length()>43){
            StringBuilder builder=new StringBuilder();
            builder.append(place.getDescription().substring(0,40))
                    .append("...");
            description.setText(builder.toString());
        } else {
            description.setText(place.getDescription());
        }
        TextView open=mRootView.findViewById(R.id.list_item_txt_datetime);
               open.setText(place.getFromTo());

        return mRootView;
    }
}