package com.devipriya.reportcard.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.devipriya.reportcard.R;
import com.devipriya.reportcard.adapters.CustomGradeRangeRowAdapter;
import com.devipriya.reportcard.models.GradeRangeRow;

import java.util.ArrayList;

public class GradePrefActivity extends AppCompatActivity {

    Button button;
    ListView gradeList;
    ArrayList<GradeRangeRow> dpItems;
    GradeRangeRow sample, last, first, shift;
    CustomGradeRangeRowAdapter gradeListAdapter;
    SharedPreferences spSettings, spGradePref;
    int dpPass, rangeSize, pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_pref);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //get pass percent from settings
        spSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String tempPass = spSettings.getString("prefPassPercent", "30");
        if(tempPass.equals(""))
            tempPass = "30";
        dpPass = Integer.parseInt(tempPass);

        //get number of ranges added
        spGradePref = getSharedPreferences("GRADE_PREF", Context.MODE_PRIVATE);
        rangeSize = spGradePref.getInt("grade_range_size", 0);

        button = (Button) findViewById(R.id.button);
        gradeList = (ListView) findViewById(R.id.gradeList);
        shift = new GradeRangeRow();
        sample = new GradeRangeRow();
        first = new GradeRangeRow("Above ", dpPass, "P");
        last = new GradeRangeRow("Below ", dpPass, "F");
        dpItems = new ArrayList<>();
        dpItems.clear();

        //load items back //do not load "first"
        for(int i=0; i< rangeSize-1; i++){
            sample.setGText(spGradePref.getString("gText_" + i, ""));
            sample.setGScore(spGradePref.getInt("gScore_" + i, 0));
            sample.setGChar(spGradePref.getString("gChar_" + i, ""));
            dpItems.add(i, sample);
            sample = new GradeRangeRow();
        }

        //for removing any range below passing marks, if exists, due to changes in pass marks later on
        int counter = 0;
        while(counter < dpItems.size()){
            if (dpItems.get(counter).getGScore() <= dpPass){
                dpItems.remove(counter);
            }
            else
                counter++;
        }

        //add "first"(above pass % range) and "last"(below pass % range)
        dpItems.add(first);
        dpItems.add(last);

        //set the list view adapter
        gradeListAdapter = new CustomGradeRangeRowAdapter(GradePrefActivity.this, dpItems);
        gradeList.setAdapter(gradeListAdapter);

        //show dialog box to add range on add button click
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(GradePrefActivity.this);
                final View textEntryView = factory.inflate(R.layout.grade_range_input_prompt, null);
                //text_entry is an Layout XML file containing two edit text field to display in alert dialog
                final EditText inputM = (EditText) textEntryView.findViewById(R.id.editGradeScoreInput);
                final EditText inputG = (EditText) textEntryView.findViewById(R.id.editGradeCharInput);
                final AlertDialog.Builder alert = new AlertDialog.Builder(GradePrefActivity.this);

                alert.setTitle(R.string.grade_range_dialog_title);
                alert.setView(textEntryView);
                alert.setPositiveButton(R.string.grade_range_dialog_positive_btn_text,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                //check if has any alpha numeric character
                                if (hasAlphaNum(inputM.getText().toString()) && hasAlphaNum(inputG.getText().toString())){
                                    int flag = 0;
                                    //check if any field left empty
                                    if (((inputM.getText().toString()).equals("")) || ((inputG.getText().toString()).equals("")))
                                        Toast.makeText(getApplicationContext(), R.string.field_empty_error, Toast.LENGTH_SHORT).show();

                                        //check if score equals pass %
                                    else if ((Integer.parseInt(inputM.getText().toString())) == dpPass)
                                        Toast.makeText(getApplicationContext(), R.string.range_exists_error, Toast.LENGTH_SHORT).show();

                                    else {
                                        dpItems.remove(first);
                                        dpItems.remove(last);

                                        //get edit text view values
                                        int inScore = Integer.parseInt(inputM.getText().toString());
                                        String inGrade = inputG.getText().toString().trim();

                                        //check if range already exists
                                        for (int i = 0; i < dpItems.size(); i++) {
                                            if (inScore == (dpItems.get(i).getGScore()) || inScore == dpPass) {
                                                flag = 1;
                                                break;
                                            }
                                        }

                                        //check if grade already exists
                                        for (int i = 0; i < dpItems.size(); i++) {
                                            if ((inGrade).equalsIgnoreCase(dpItems.get(i).getGChar())) {
                                                flag = 2;
                                                break;
                                            }
                                        }

                                        //check if range score is less than passing range
                                        if (inScore < dpPass)
                                            flag = 3;

                                        //check if entered range is greater than any existing range
                                        for (int i = 0; i < dpItems.size(); i++) {
                                            if (inScore > (dpItems.get(i).getGScore())) {
                                                pos = i;
                                                flag = -1;
                                                break;
                                            }
                                        }

                                        if (flag == 0 || flag == -1) {
                                            sample = new GradeRangeRow(inScore, inGrade);
                                            //if entered range is less than any existing range, add to the list view
                                            if (flag == 0)
                                                dpItems.add(sample);
                                            else {
                                                //if entered range is greater than any existing range, add to the correct position in list view
                                                dpItems.add(pos, sample);
                                            }
                                        }

                                        //again add back "first" and "last"
                                        dpItems.add(first);
                                        dpItems.add(last);
                                        sample = new GradeRangeRow();

                                        //set the adapter and refresh the list
                                        gradeListAdapter = new CustomGradeRangeRowAdapter(GradePrefActivity.this, dpItems);
                                        gradeList.setAdapter(gradeListAdapter);
                                        gradeList.invalidateViews();
                                        gradeListAdapter.notifyDataSetChanged();

                                        if (flag == 1)
                                            Toast.makeText(getApplicationContext(), R.string.range_exists_error, Toast.LENGTH_SHORT).show();
                                        else if (flag == 2)
                                            Toast.makeText(getApplicationContext(), R.string.grade_exists_error, Toast.LENGTH_SHORT).show();
                                        else if (flag == 3)
                                            Toast.makeText(getApplicationContext(), R.string.percentage_invalid_error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), R.string.invalid_data_error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                );
                alert.setNegativeButton(R.string.grade_range_dialog_negative_btn_text,
                        new DialogInterface.OnClickListener()

                        {
                            public void onClick (DialogInterface dialog,int whichButton){
                                //do not do anything
                            }
                        }

                );
                // create alert dialog
                AlertDialog alertDialog = alert.create();

                //show soft keyboard
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                // show it
                alertDialog.show();


            }
        });

        //option to remove range on item click
        gradeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(GradePrefActivity.this);
                builder1.setMessage(R.string.remove_grade_dialog_message);
                builder1.setTitle(R.string.remove_grade_dialog_title);
                builder1.setCancelable(true);
                builder1.setPositiveButton(R.string.remove_grade_dialog_positive_btn_text,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do not let user remove "first" and "last"
                                if (position == dpItems.size() - 1 || position == dpItems.size() - 2)
                                    Toast.makeText(GradePrefActivity.this, R.string.range_removal_error, Toast.LENGTH_SHORT).show();
                                else {
                                    //remove item
                                    dpItems.remove(position);
                                    //set adapter
                                    gradeListAdapter = new CustomGradeRangeRowAdapter(GradePrefActivity.this, dpItems);
                                    gradeList.setAdapter(gradeListAdapter);
                                    //refresh the list
                                    gradeListAdapter.notifyDataSetChanged();
                                    gradeList.invalidateViews();
                                    dialog.cancel();
                                    Toast.makeText(GradePrefActivity.this, R.string.range_removal_success, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder1.setNegativeButton(R.string.remove_grade_dialog_negative_btn_text,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //don't remove item
                                dialog.cancel();
                            }
                        });

                AlertDialog remove = builder1.create();
                remove.show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        //save using shared pref
        spGradePref = getSharedPreferences("GRADE_PREF", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = spGradePref.edit();
        editor.clear();
        editor.commit();

        //store only till "first" //do not store "last"
        for(int i=0; i< dpItems.size()-1; i++){
            editor.putString("gText_"+i , dpItems.get(i).getGText());
            editor.putInt("gScore_" + i, dpItems.get(i).getGScore());
            editor.putString("gChar_" + i, dpItems.get(i).getGChar());
        }

        editor.putInt("grade_range_size", (dpItems.size()-1));
        editor.commit();

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