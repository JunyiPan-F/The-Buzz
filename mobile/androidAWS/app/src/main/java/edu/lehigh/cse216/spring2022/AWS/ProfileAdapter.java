package edu.lehigh.cse216.spring2022.AWS;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProfileAdapter extends ArrayAdapter<ProfileModel> {
    private Context context; //context
    private ArrayList<ProfileModel> info; //data source of the list adapter

    public ProfileAdapter(Context context, ArrayList<ProfileModel> info){
        super(context,0, info);
    }



    @Override
    public View getView(int i, View view, ViewGroup parent) {


        // get current item to be displayed
        ProfileModel currentItem = (ProfileModel) getItem(i);

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.profile_layout, parent, false);
        }
        // Lookup view for data population
        TextView id = (TextView) view.findViewById(R.id.textId);
        TextView name = (TextView) view.findViewById(R.id.textName);
        TextView email = (TextView) view.findViewById(R.id.textEmail);

        //sets the text for item name and item description from the current item object
        id.setText(currentItem.getId());
        name.setText(currentItem.getName());
        email.setText(currentItem.getEmail());

        // Return the completed view to render on screen
        return view;

    }
}
