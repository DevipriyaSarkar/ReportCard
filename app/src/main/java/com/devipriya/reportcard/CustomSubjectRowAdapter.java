package com.devipriya.reportcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Devipriya on 08-Aug-15.
 */
public class CustomSubjectRowAdapter extends ArrayAdapter<String> {

    public CustomSubjectRowAdapter(Context context, ArrayList<String> subject) {
        super(context, 0, subject);
    }

    @Override
    public View getView(int position, View customView, ViewGroup parent) {
        // Get the data item for this position
        String subject = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (customView == null) {
            customView = LayoutInflater.from(getContext()).inflate(R.layout.custom_subject_row, parent, false);
        }
        // Lookup view for data population
        TextView dpSubject = (TextView) customView.findViewById(R.id.dpSubject);
        // Populate the data into the template view using the data object
        dpSubject.setText(subject);

        // Animate
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.abc_slide_in_top);
        customView.startAnimation(anim);

        // Return the completed view to render on screen
        return customView;
    }

}