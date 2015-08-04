package com.devipriya.reportcard;

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
        dpOptions.add("Add or Delete Subjects");
        dpOptions.add("Add or Delete Tests");
        dpOptions.add("Enter Marks");
        dpOptions.add("Report Card");
        dpOptions.add("Analysis");

        dpMainListView = (ListView) findViewById(R.id.dpMainListView);
        dpMainAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dpOptions);
        dpMainListView.setAdapter(dpMainAdapter);

        dpMainListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            Intent intent = new Intent(MainActivity.this, Subjects.class);
                            startActivity(intent);
                        } else if (position == 1) {
                            Intent intent = new Intent(MainActivity.this, Tests.class);
                            startActivity(intent);
                        } else if (position == 2) {
                            //get test size and subject size
                            spSubject = getSharedPreferences("SUBJECT_LIST", Context.MODE_PRIVATE);
                            spTest = getSharedPreferences("TEST_LIST", Context.MODE_PRIVATE);

                            subSize = spSubject.getInt("subject_size", 0);
                            testSize = spTest.getInt("test_size", 0);

                            //check if test list and subject list is empty
                            if (testSize == 0 || subSize == 0)
                                Toast.makeText(MainActivity.this, "Add Test and/or Subject to Input Marks", Toast.LENGTH_LONG).show();
                            else {
                                Intent intent = new Intent(MainActivity.this, Marks.class);
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
                                Toast.makeText(MainActivity.this, "Add Test and/or Subject to Generate Report Card", Toast.LENGTH_LONG).show();
                            else {
                                Intent intent = new Intent(MainActivity.this, ReportCard.class);
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
                                Toast.makeText(MainActivity.this, "Add Test and/or Subject to Analyse", Toast.LENGTH_LONG).show();
                            else {
                                Intent intent = new Intent(MainActivity.this, Analyse.class);
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
                Intent i = new Intent(this, UserSettings.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}