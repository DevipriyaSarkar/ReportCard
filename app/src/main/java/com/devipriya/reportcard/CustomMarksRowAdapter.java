package com.devipriya.reportcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Devipriya on 10-Jul-15.
 */
public class CustomMarksRowAdapter extends ArrayAdapter<MarksRow> {

    public CustomMarksRowAdapter(Context context, ArrayList<MarksRow> marksRow) {
        super(context, 0, marksRow);
    }

    @Override
    public View getView(int position, View customView, ViewGroup parent) {
        // Get the data item for this position
        MarksRow marksRow = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (customView == null) {
            customView = LayoutInflater.from(getContext()).inflate(R.layout.custom_marks_row, parent, false);
        }
        // Lookup view for data population
        TextView dpSubjectText = (TextView) customView.findViewById(R.id.dpSubjectText);
        TextView dpSubjectMarksText = (TextView) customView.findViewById(R.id.dpSubjectMarksText);
        // Populate the data into the template view using the data object
        dpSubjectText.setText(marksRow.getSubjectName());
        dpSubjectMarksText.setText(String.valueOf(marksRow.getSubjectMarks()));
        // Return the completed view to render on screen
        return customView;
    }

}
