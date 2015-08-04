package com.devipriya.reportcard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Analyse extends AppCompatActivity {

    ArrayList<String> dpOptions;
    ArrayAdapter dpAnalysisAdapter;
    ListView dpAnalysisListView;
    SharedPreferences spSubject;
    SharedPreferences spTest;
    int subSize, testSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //initialising
        dpOptions = new ArrayList<>();
        dpOptions.add("Test-Wise Analysis");
        dpOptions.add("Subject-Wise Analysis");

        dpAnalysisListView = (ListView) findViewById(R.id.dpAnalysisListView);
        //loading the list view adapter
        dpAnalysisAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dpOptions);
        dpAnalysisListView.setAdapter(dpAnalysisAdapter);

        //get test size and subject size
        spSubject = getSharedPreferences("SUBJECT_LIST", Context.MODE_PRIVATE);
        spTest = getSharedPreferences("TEST_LIST", Context.MODE_PRIVATE);

        subSize = spSubject.getInt("subject_size", 0);
        testSize = spTest.getInt("test_size", 0);

        //to open test-wise / subject-wise analysis
        dpAnalysisListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    //check if two or more subjects exist
                    if(subSize > 1){
                        Intent intent = new Intent(Analyse.this, Analysis_Test.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(Analyse.this, "Please add more than one subject to analyze subjects.", Toast.LENGTH_SHORT).show();
                } else if(position == 1){
                    //check if two or more tests exist
                    if(testSize > 1) {
                        Intent intent = new Intent(Analyse.this, Analysis_Subject.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(Analyse.this, "Please add more than one test to analyze tests.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
