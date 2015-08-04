package com.devipriya.reportcard;

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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class Analysis_Test extends AppCompatActivity {

    ArrayList<String> dpTests;
    ArrayAdapter<String> dpTestSpinnerAdapter;
    Spinner testSpinner;
    SharedPreferences sharedPrefTests;
    SharedPreferences sharedPrefSubjects;
    SharedPreferences sharedPrefMarks;
    SharedPreferences sharedPrefSettings;
    boolean userValOnTop;
    GraphView testGraph;
    DataPoint[] bars;
    ArrayList<Double> dpMarks;
    ArrayList<String> dpSubjectName;
    BarGraphSeries<DataPoint> series;
    String[] dpSubArray;
    int subSize;
    int testSize;
    int pos = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis__test);
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
        testSpinner = (Spinner) findViewById(R.id.testSpinner);

        //initialize
        testGraph = (GraphView) findViewById(R.id.testGraph);
        dpTests = new ArrayList<>();
        dpTests.clear();
        sharedPrefTests = getSharedPreferences("TEST_LIST", Context.MODE_PRIVATE);

        //get test list into array list "dpTests"
        for (int i = 0; i < testSize; i++) {
            dpTests.add(i, sharedPrefTests.getString("testName_" + i, ""));
        }

        //populate the spinner
        dpTestSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dpTests);
        dpTestSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        testSpinner.setAdapter(dpTestSpinnerAdapter);

        sharedPrefMarks = getSharedPreferences("MARKS_LIST", Context.MODE_PRIVATE);

        dpMarks = new ArrayList<>();
        dpSubjectName = new ArrayList<>();

        //get subject name list into array list "dpSubjectName"
        dpSubjectName.clear();
        for(int i=0; i < subSize; i++)
            dpSubjectName.add((sharedPrefSubjects.getString("subject_"+i, "")));

        //loading array list into string array
        dpSubArray = new String[dpSubjectName.size()];
        dpSubArray = dpSubjectName.toArray(dpSubArray);

        //set on item selected listener for spinner
        testSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //draw graph
                pos = position;

                testGraph.removeAllSeries();

                dpMarks.clear();
                for(int i=0; i < subSize; i++) {
                    dpMarks.add((double)(sharedPrefMarks.getInt("marks_" + pos + "_" + i, 0)));
                }

                //Draw the bar graph
                bars = new DataPoint[subSize];
                for (int i = 0; i < subSize; i++) {
                    bars[i] = new DataPoint((double) i, dpMarks.get(i));
                }

                if(series == null)
                    series = new BarGraphSeries<DataPoint>(bars);
                else{
                    series.resetData(bars);
                }

                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(testGraph);
                staticLabelsFormatter.setHorizontalLabels(dpSubArray);
                testGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                series.setSpacing(50);
                series.setDrawValuesOnTop(userValOnTop);
                series.setValuesOnTopColor(Color.RED);
                series.setValuesOnTopSize(35);
                testGraph.addSeries(series);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //don't do anything
            }
        });
    }

}

