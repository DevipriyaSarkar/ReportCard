package com.devipriya.reportcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Devipriya on 7/9/2015.
 */
public class CustomTestRowAdapter extends ArrayAdapter<TestRow> {

    public CustomTestRowAdapter(Context context, ArrayList<TestRow> testRow) {
        super(context, 0, testRow);
    }

    @Override
    public View getView(int position, View customView, ViewGroup parent) {
        // Get the data item for this position
        TestRow testRow = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (customView == null) {
            customView = LayoutInflater.from(getContext()).inflate(R.layout.custom_test_row, parent, false);
        }
        // Lookup view for data population
        TextView dpTest = (TextView) customView.findViewById(R.id.dpTest);
        TextView dpTestMaxMarks = (TextView) customView.findViewById(R.id.dpTestMaxMarks);
        // Populate the data into the template view using the data object
        dpTest.setText(testRow.getTestName());
        dpTestMaxMarks.setText(String.valueOf(testRow.getTestMaxMarks()));
        // Return the completed view to render on screen
        return customView;
    }

}
