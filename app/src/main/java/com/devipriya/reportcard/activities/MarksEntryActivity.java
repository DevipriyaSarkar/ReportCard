package com.devipriya.reportcard.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.devipriya.reportcard.R;
import com.devipriya.reportcard.fragments.InputMarksFragment;

import java.util.ArrayList;


public class MarksEntryActivity extends AppCompatActivity {

    ArrayList<String> dpTests;
    ArrayAdapter<String> dpSpinnerAdapter;
    Spinner spinner;
    SharedPreferences sharedPrefTests;
    SharedPreferences sharedPrefSubjects;
    int subSize;
    int testSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //get subject list size
        sharedPrefSubjects = getSharedPreferences("SUBJECT_LIST", Context.MODE_PRIVATE);
        subSize = sharedPrefSubjects.getInt("subject_size", 0);

        //get test list size
        sharedPrefTests = getSharedPreferences("TEST_LIST", Context.MODE_PRIVATE);
        testSize = sharedPrefTests.getInt("test_size", 0);

        //find view by id
        spinner = (Spinner) findViewById(R.id.spinner);

        //initialize
        dpTests = new ArrayList<>();
        dpTests.clear();
        sharedPrefTests = getSharedPreferences("TEST_LIST", Context.MODE_PRIVATE);

        //get test list into array list "dpTests"
        for(int i=0; i< sharedPrefTests.getInt("test_size", 0); i++){
            dpTests.add(i, sharedPrefTests.getString("testName_"+i, ""));
        }

        //populate the spinner
        dpSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dpTests);
        dpSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dpSpinnerAdapter);

        //set on item selected listener for spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //pass test selected position to input marks fragment
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);

                FragmentManager fm;

                //fragment manager //load fragments
                fm = getFragmentManager();
                if (fm.findFragmentById(R.id.dpFragment) == null) {
                    InputMarksFragment fragment;
                    fragment = new InputMarksFragment();
                    fragment.setArguments(bundle);
                    fm.beginTransaction().add(R.id.dpFragment, fragment).commit();
                } else {
                    InputMarksFragment fragment;
                    fragment = new InputMarksFragment();
                    fragment.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.dpFragment, fragment).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing if no spinner item selected
            }
        });
    }

}

