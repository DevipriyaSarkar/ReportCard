package com.devipriya.reportcard.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.devipriya.reportcard.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class AnalysisSubjectActivity extends AppCompatActivity {

    private ArrayList<String> dpSubjects;
    private ArrayAdapter<String> dpSubSpinnerAdapter;
    private Spinner subSpinner;
    private SharedPreferences sharedPrefTests;
    private SharedPreferences sharedPrefSubjects;
    private SharedPreferences sharedPrefMarks;
    private SharedPreferences sharedPrefSettings;
    private boolean userValOnTop;
    private GraphView subGraph;
    private DataPoint[] bars;
    private ArrayList<Double> dpMarks;
    private ArrayList<String> dpTestName;
    private BarGraphSeries<DataPoint> series;
    private String[] dpTestArray;
    private int subSize;
    private int testSize;
    private int pos = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_subject);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPrefSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userValOnTop = sharedPrefSettings.getBoolean("prefValOnTop", true);

        //get subject list size
        sharedPrefSubjects = getSharedPreferences("SUBJECT_LIST", Context.MODE_PRIVATE);
        subSize = sharedPrefSubjects.getInt("subject_size", 0);

        //get test list size
        sharedPrefTests = getSharedPreferences("TEST_LIST", Context.MODE_PRIVATE);
        testSize = sharedPrefTests.getInt("test_size", 0);

        //find view by id
        subSpinner = (Spinner) findViewById(R.id.subSpinner);

        //initialize
        subGraph = (GraphView) findViewById(R.id.subGraph);
        dpSubjects = new ArrayList<>();
        dpSubjects.clear();
        sharedPrefSubjects = getSharedPreferences("SUBJECT_LIST", Context.MODE_PRIVATE);

        //get subject list into array list "dpSubjects"
        for (int i = 0; i < subSize; i++) {
            dpSubjects.add(i, sharedPrefSubjects.getString("subject_" + i, ""));
        }

        //populate the spinner
        dpSubSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dpSubjects);
        dpSubSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subSpinner.setAdapter(dpSubSpinnerAdapter);

        sharedPrefMarks = getSharedPreferences("MARKS_LIST", Context.MODE_PRIVATE);

        dpMarks = new ArrayList<>();
        dpTestName = new ArrayList<>();

        //get test name list into array list "dpTestName"
        dpTestName.clear();
        for(int i=0; i < testSize; i++)
            dpTestName.add((sharedPrefTests.getString("testName_" + i, "")));

        //loading the array list into string array
        dpTestArray = new String[dpTestName.size()];
        dpTestArray = dpTestName.toArray(dpTestArray);

        //set on item selected listener for spinner
        subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //draw the graph
                pos = position;

                subGraph.removeAllSeries();

                dpMarks.clear();
                for(int i=0; i < testSize; i++) {
                    dpMarks.add((double)(sharedPrefMarks.getInt("marks_" + i + "_" + pos, 0)));
                }

                //Draw the bar graph
                bars = new DataPoint[testSize];
                for (int i = 0; i < testSize; i++) {
                    bars[i] = new DataPoint((double) i, dpMarks.get(i));
                }

                if(series == null)
                    series = new BarGraphSeries<>(bars);
                else{
                    series.resetData(bars);
                }

                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(subGraph);
                staticLabelsFormatter.setHorizontalLabels(dpTestArray);
                subGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                series.setSpacing(50);
                series.setDrawValuesOnTop(userValOnTop);
                series.setValuesOnTopColor(Color.RED);
                series.setValuesOnTopSize(35);
                subGraph.addSeries(series);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //don't do anything
            }
        });
    }
}

