package com.devipriya.reportcard.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.devipriya.reportcard.R;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    SharedPreferences spSubject;
    SharedPreferences spTest;
    ArrayList<String> dpOptions;
    ArrayAdapter dpMainAdapter;
    ListView dpMainListView;
    int subSize, testSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dpOptions = new ArrayList<>();
        dpOptions.add(getString(R.string.subject_option_text));
        dpOptions.add(getString(R.string.text_option_text));
        dpOptions.add(getString(R.string.marks_entry_option_text));
        dpOptions.add(getString(R.string.view_report_card_option_text));
        dpOptions.add(getString(R.string.analysis_option_text));

        dpMainListView = (ListView) findViewById(R.id.dpMainListView);
        dpMainAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dpOptions);
        dpMainListView.setAdapter(dpMainAdapter);

        dpMainListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            Intent intent = new Intent(MainActivity.this, SubjectsEntryActivity.class);
                            startActivity(intent);
                        } else if (position == 1) {
                            Intent intent = new Intent(MainActivity.this, TestsEntryActivity.class);
                            startActivity(intent);
                        } else if (position == 2) {
                            //get test size and subject size
                            spSubject = getSharedPreferences("SUBJECT_LIST", Context.MODE_PRIVATE);
                            spTest = getSharedPreferences("TEST_LIST", Context.MODE_PRIVATE);

                            subSize = spSubject.getInt("subject_size", 0);
                            testSize = spTest.getInt("test_size", 0);

                            //check if test list and subject list is empty
                            if (testSize == 0 || subSize == 0)
                                Toast.makeText(MainActivity.this, R.string.no_subject_test_for_marks_entry_error,
                                        Toast.LENGTH_LONG).show();
                            else {
                                Intent intent = new Intent(MainActivity.this, MarksEntryActivity.class);
                                startActivity(intent);
                            }
                        } else if (position == 3) {
                            //get test size and subject size
                            spSubject = getSharedPreferences("SUBJECT_LIST", Context.MODE_PRIVATE);
                            spTest = getSharedPreferences("TEST_LIST", Context.MODE_PRIVATE);

                            subSize = spSubject.getInt("subject_size", 0);
                            testSize = spTest.getInt("test_size", 0);

                            //check if test list and subject list is empty
                            if (subSize == 0 || testSize == 0)
                                Toast.makeText(MainActivity.this, R.string.no_subject_test_for_report_generation_error,
                                        Toast.LENGTH_LONG).show();
                            else {
                                Intent intent = new Intent(MainActivity.this, ReportCardActivity.class);
                                startActivity(intent);
                            }
                        }  else if (position == 4) {
                            //get test size and subject size
                            spSubject = getSharedPreferences("SUBJECT_LIST", Context.MODE_PRIVATE);
                            spTest = getSharedPreferences("TEST_LIST", Context.MODE_PRIVATE);

                            subSize = spSubject.getInt("subject_size", 0);
                            testSize = spTest.getInt("test_size", 0);

                            //check if test list and subject list is empty
                            if (subSize == 0 || testSize == 0)
                                Toast.makeText(MainActivity.this, R.string.no_subject_test_for_analysis_error,
                                        Toast.LENGTH_LONG).show();
                            else {
                                Intent intent = new Intent(MainActivity.this, AnalyseActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    //react to the user tapping/selecting an options menu item

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_settings:
                Intent i = new Intent(this, UserSettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}