package com.devipriya.reportcard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class Tests extends AppCompatActivity {

    int settingMaxMarks;
    EditText dpTestNameInput;
    EditText dpMaxMarksInput;
    Button dpSaveButton;
    ListView dpDynamicListView;
    ArrayList<TestRow> dpItems;
    CustomTestRowAdapter dpDynamicListViewAdapter;
    TestRow itemName;
    TestRow sample;
    SharedPreferences spSubject;
    SharedPreferences spTest;
    SharedPreferences spMarks;
    SharedPreferences spSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //get default marks set by user in settings. Set 100 if nothing set
        spSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String temp = spSettings.getString("prefMaxMarks", "100");
        if(temp.equals(""))
            temp = "100";
        settingMaxMarks = Integer.parseInt(temp);

        //find view by ids
        dpTestNameInput = (EditText) findViewById(R.id.dpTestNameInput);
        dpMaxMarksInput = (EditText) findViewById(R.id.dpMaxMarksInput);
        dpMaxMarksInput.setHint("Enter Max. Marks (Default: "+(String.valueOf(settingMaxMarks))+")");
        dpSaveButton = (Button) findViewById(R.id.dpSaveButton);
        dpDynamicListView= (ListView) findViewById(R.id.dpDynamicListView);

        //initialize
        dpItems = new ArrayList<>();
        dpItems.clear();
        itemName = new TestRow();
        sample = new TestRow();

        //load test details saved in array list "dpItems"
        spTest = getSharedPreferences("TEST_LIST", Context.MODE_PRIVATE);

        for(int i=0; i< spTest.getInt("test_size", 0); i++){
            sample.setTestName(spTest.getString("testName_"+i, ""));
            sample.setTestMaxMarks(spTest.getInt("testMaxMarks_" + i, 0));
            dpItems.add(i, sample);
            sample = new TestRow();
        }

        //set custom list view adapter
        dpDynamicListViewAdapter = new CustomTestRowAdapter(Tests.this, dpItems);
        dpDynamicListView.setAdapter(dpDynamicListViewAdapter);

        //add items

        dpSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;
                //get test name
                if (dpTestNameInput.getText().toString().equals("")) {
                    itemName.setTestName("No Title");
                } else {
                    itemName.setTestName(dpTestNameInput.getText().toString());
                    String[] words = itemName.getTestName().split(" ");
                    StringBuilder sb = new StringBuilder();
                    if (words[0].length() > 0) {
                        sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString());
                        for (int i = 1; i < words.length; i++) {
                            sb.append(" ");
                            sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString());
                        }
                    }
                    itemName.setTestName(sb.toString());
                }

                //get test maximum marks
                if (dpMaxMarksInput.getText().toString().equals("")) {
                    itemName.setTestMaxMarks(settingMaxMarks);
                } else {
                    itemName.setTestMaxMarks(Integer.parseInt(dpMaxMarksInput.getText().toString()));
                }

                dpTestNameInput.setText("");
                dpMaxMarksInput.setText("");

                //check if test already exists
                for (int i=0; i < dpItems.size(); i++ ){
                    if((dpItems.get(i).getTestName()).equalsIgnoreCase(itemName.getTestName())) {
                        flag = 1;
                        break;
                    }
                }

                if(flag == 0) {
                    //add the item details into array list "dpItems" as a TestRow object
                    dpItems.add(itemName);
                    itemName = new TestRow();
                    //set the adapter
                    dpDynamicListViewAdapter = new CustomTestRowAdapter(Tests.this, dpItems);
                    dpDynamicListView.setAdapter(dpDynamicListViewAdapter);
                    //refresh the list
                    dpDynamicListView.invalidateViews();
                    dpDynamicListViewAdapter.notifyDataSetChanged();
                    Toast.makeText(Tests.this, "Test Added", Toast.LENGTH_SHORT).show();

                    //delete marks of that subject in all tests
                    spSubject = getSharedPreferences("SUBJECT_LIST", Context.MODE_PRIVATE);
                    spMarks = getSharedPreferences("MARKS_LIST", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spMarks.edit();
                    for (int i = 0; i < 50; i++) {
                        editor.putInt("marks_" + (dpItems.size() - 1) + "_" + i, 0);
                    }
                    editor.commit();
                }
                else{
                    itemName = new TestRow();
                    Toast.makeText(Tests.this, "TEST ALREADY EXISTS!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //remove items
        dpDynamicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //alert dialog box
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Tests.this);
                builder1.setMessage("Do you want to delete?");
                builder1.setTitle("DELETE");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //remove item
                                dpItems.get(position).setTestMaxMarks(0);
                                dpItems.get(position).setTestName("");
                                dpItems.remove(position);
                                //set adapter
                                dpDynamicListViewAdapter = new CustomTestRowAdapter(Tests.this, dpItems);
                                dpDynamicListView.setAdapter(dpDynamicListViewAdapter);
                                //refresh the list
                                dpDynamicListViewAdapter.notifyDataSetChanged();
                                dpDynamicListView.invalidateViews();
                                dialog.cancel();
                                Toast.makeText(Tests.this, "Test Removed", Toast.LENGTH_SHORT).show();

                                //delete marks of all subjects in that test
                                spSubject = getSharedPreferences("SUBJECT_LIST", Context.MODE_PRIVATE);
                                spMarks = getSharedPreferences("MARKS_LIST", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = spMarks.edit();
                                for(int i=0; i < spSubject.getInt("subject_size", 0); i++){
                                    editor.putInt("marks_"+position+"_"+i,0);
                                }
                                editor.commit();
                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //don't remove item
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

    }


    @Override
    public void onBackPressed() {

        //save using shared pref
        spTest = getSharedPreferences("TEST_LIST", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = spTest.edit();
        editor.clear();
        editor.commit();

        for(int i=0; i< dpItems.size(); i++){
            editor.putString("testName_"+i , dpItems.get(i).getTestName());
            editor.putInt("testMaxMarks_"+i, dpItems.get(i).getTestMaxMarks());
        }

        editor.putInt("test_size", dpItems.size());
        editor.commit();

        super.onBackPressed();

    }

}