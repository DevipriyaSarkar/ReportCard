package com.devipriya.reportcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Devipriya on 02-Aug-15.
 */
public class CustomGradeRangeRowAdapter extends ArrayAdapter<GradeRangeRow> {

    public CustomGradeRangeRowAdapter(Context context, ArrayList<GradeRangeRow> gradeRangeRow) {
        super(context, 0, gradeRangeRow);
    }

    @Override
    public View getView(int position, View customView, ViewGroup parent) {
        // Get the data item for this position
        GradeRangeRow gradeRangeRow = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (customView == null) {
            customView = LayoutInflater.from(getContext()).inflate(R.layout.custom_grade_range_row, parent, false);
        }
        // Lookup view for data population
        TextView abText = (TextView) customView.findViewById(R.id.abText);
        TextView gradeChar = (TextView) customView.findViewById(R.id.gradeChar);
        TextView gradeScore = (TextView) customView.findViewById(R.id.gradeScore);
        // Populate the data into the template view using the data object
        abText.setText(gradeRangeRow.getGText());
        gradeChar.setText(gradeRangeRow.getGChar());
        gradeScore.setText(String.valueOf(gradeRangeRow.getGScore())+"%");
        // Return the completed view to render on screen
        return customView;
    }
}
