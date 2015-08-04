package com.devipriya.reportcard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class Subjects extends AppCompatActivity {

    EditText dpInputEditText;
    Button dpSaveButton;
    ListView dpDynamicListView;
    ArrayList<String> dpItems;
    ArrayAdapter dpDynamicListViewAdapter;
    String itemName= "";
    SharedPreferences spSubject;
    SharedPreferences spTest;
    SharedPreferences spMarks;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //find view by ids

        dpInputEditText = (EditText) findViewById(R.id.dpInputEditText);
        dpSaveButton = (Button) findViewById(R.id.dpSaveButton);
        dpDynamicListView= (ListView) findViewById(R.id.dpDynamicListView);

        //initialize

        dpItems = new ArrayList<>();
        dpItems.clear();

        //load subjects saved in array list "dpItems"
        spSubject = getSharedPreferences("SUBJECT_LIST", Context.MODE_PRIVATE);
        for(int i=0; i< spSubject.getInt("subject_size", 0); i++)
            dpItems.add(i, spSubject.getString("subject_" + i, ""));


        dpDynamicListViewAdapter = new ArrayAdapter<String>(Subjects.this, android.R.layout.simple_list_item_1, dpItems);
        dpDynamicListView.setAdapter(dpDynamicListViewAdapter);

        //add items

        dpSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if text contains any alpha numeric character or not
                if (hasAlphaNum(dpInputEditText.getText().toString())) {
                    int flag = 0;
                    //get subject
                    itemName = dpInputEditText.getText().toString().trim();
                    try {
                        //make first letter of each word capital
                        String[] words = itemName.split(" ");
                        StringBuilder sb = new StringBuilder();
                        if (words[0].length() > 0) {
                            sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString());
                            for (int i = 1; i < words.length; i++) {
                                sb.append(" ");
                                sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString());
                            }
                        }
                        itemName = sb.toString().trim();
                    } catch (Exception e){
                        itemName = dpInputEditText.getText().toString().trim();
                    }

                    dpInputEditText.setText("");

                    //check if item already exists
                    for (int i = 0; i < dpItems.size(); i++) {
                        if ((dpItems.get(i)).equalsIgnoreCase(itemName)) {
                            flag = 1;
                            break;
                        }
                    }

                    //add subject into array list "dpItems"
                    if (flag == 0) {
                        dpItems.add(itemName);
                        //set the adapter
                        dpDynamicListViewAdapter = new ArrayAdapter<String>(Subjects.this, android.R.layout.simple_list_item_1, dpItems);
                        dpDynamicListView.setAdapter(dpDynamicListViewAdapter);
                        //refresh the list
                        dpDynamicListView.invalidateViews();
                        dpDynamicListViewAdapter.notifyDataSetChanged();
                        Toast.makeText(Subjects.this, "Subject Added", Toast.LENGTH_SHORT).show();

                        //delete marks of that subject in all tests
                        spTest = getSharedPreferences("TEST_LIST", Context.MODE_PRIVATE);
                        spMarks = getSharedPreferences("MARKS_LIST", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = spMarks.edit();
                        for (int i = 0; i < 50; i++) {
                            editor.putInt("marks_" + i + "_" + (dpItems.size() - 1), 0);
                        }
                        editor.commit();
                    } else {
                        Toast.makeText(Subjects.this, "SUBJECT ALREADY EXISTS!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Subjects.this, "Please enter valid subject name.", Toast.LENGTH_SHORT).show();
                    dpInputEditText.setText("");
                }
            }
        });


        //remove items
        dpDynamicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Subjects.this);
                builder1.setMessage("Do you want to delete?");
                builder1.setTitle("DELETE");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //remove the subject
                                dpItems.remove(position);
                                //set the adapter
                                dpDynamicListViewAdapter = new ArrayAdapter<String>(Subjects.this, android.R.layout.simple_list_item_1, dpItems);
                                dpDynamicListView.setAdapter(dpDynamicListViewAdapter);
                                //refresh the list
                                dpDynamicListViewAdapter.notifyDataSetChanged();
                                dpDynamicListView.invalidateViews();
                                dialog.cancel();
                                Toast.makeText(Subjects.this, "Subject Removed", Toast.LENGTH_SHORT).show();

                                //delete marks of that subject in all tests
                                spTest = getSharedPreferences("TEST_LIST", Context.MODE_PRIVATE);
                                spMarks = getSharedPreferences("MARKS_LIST", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = spMarks.edit();
                                for (int i = 0; i < spTest.getInt("test_size", 0); i++) {
                                    editor.putInt("marks_" + i + "_" + position, 0);
                                }
                                editor.commit();
                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //don't remove the subject
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
        spSubject = getSharedPreferences("SUBJECT_LIST", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = spSubject.edit();
        editor.clear();
        editor.commit();

        for(int i=0; i< dpItems.size(); i++)
            editor.putString("subject_"+i , dpItems.get(i));
        editor.putInt("subject_size", dpItems.size());
        editor.apply();

        super.onBackPressed();

    }

    boolean hasAlphaNum(String s){
        for (int i=0; i < s.length(); i++){
            if((s.charAt(i)>='A' && s.charAt(i)<='Z') || (s.charAt(i)>='a' && s.charAt(i)<='z') || (s.charAt(i)>='0' && s.charAt(i)<='9'))
                return true;
        }
        return false;
    }

}