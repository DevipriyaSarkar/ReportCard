package com.devipriya.reportcard.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.devipriya.reportcard.R;

import java.util.ArrayList;

public class AnalyseActivity extends AppCompatActivity {

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
        dpOptions.add(getString(R.string.test_analysis_text));
        dpOptions.add(getString(R.string.subject_analysis_text));

        dpAnalysisListView = (ListView) findViewById(R.id.dpAnalysisListView);
        //loading the list view adapter
        dpAnalysisAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dpOptions);
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
                        Intent intent = new Intent(AnalyseActivity.this, AnalysisTestActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(AnalyseActivity.this, R.string.too_few_subject_error, Toast.LENGTH_SHORT).show();
                } else if(position == 1){
                    //check if two or more tests exist
                    if(testSize > 1) {
                        Intent intent = new Intent(AnalyseActivity.this, AnalysisSubjectActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(AnalyseActivity.this, R.string.too_few_test_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
