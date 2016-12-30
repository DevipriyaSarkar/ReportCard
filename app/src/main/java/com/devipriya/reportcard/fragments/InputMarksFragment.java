package com.devipriya.reportcard.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devipriya.reportcard.R;
import com.devipriya.reportcard.adapters.CustomMarksRowAdapter;
import com.devipriya.reportcard.models.MarksRow;

import java.util.ArrayList;

/**
 * Created by Devipriya on 11-Jul-15.
 */

public class InputMarksFragment extends Fragment {

    private TextView dpMaxMarksText;
    private TextView dpTap;
    private ListView dpInputMarksListView;
    private ArrayList<MarksRow> dpItems;
    private CustomMarksRowAdapter dpItemsListViewAdapter;
    private MarksRow sample;
    private MarksRow item;
    private SharedPreferences spSubject;
    private SharedPreferences spTest;
    private SharedPreferences spMarks;
    private int testPos;
    private int max;
    private int flag;
    private Bundle bundle;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.input_marks_fragment, container, false);

        //get item position from activity
        bundle = getArguments();
        if(bundle != null)
            testPos = bundle.getInt("position", -1);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //find views by id
        dpMaxMarksText = (TextView) getActivity().findViewById(R.id.dpMaxMarksText);
        dpTap = (TextView) getActivity().findViewById(R.id.dpTap);
        dpInputMarksListView = (ListView) getActivity().findViewById(R.id.dpInputMarksListView);
        //initialise
        dpItems = new ArrayList<>();
        dpItems.clear();

        spSubject = getActivity().getSharedPreferences("SUBJECT_LIST", 0);
        spTest = getActivity().getSharedPreferences("TEST_LIST", 0);
        dpTap.setText(R.string.tap_hint);
        //initialize
        item = new MarksRow();
        sample = new MarksRow();

        //set maximum test marks of the test selected
        spTest = getActivity().getSharedPreferences("TEST_LIST", 0);
        max = spTest.getInt("testMaxMarks_" + testPos, 0);
        dpMaxMarksText.setText(String.format(getString(R.string.max_marks_text), max));
        //get subject list and marks already saved
        spSubject = getActivity().getSharedPreferences("SUBJECT_LIST", 0);
        spMarks = getActivity().getSharedPreferences("MARKS_LIST", 0);
        for (int i = 0; i < spSubject.getInt("subject_size", 0); i++) {
            sample.setSubjectName(spSubject.getString("subject_" + i, ""));
            sample.setSubjectMarks(spMarks.getInt("marks_"+testPos+"_"+i, 0));
            dpItems.add(i, sample);
            sample = new MarksRow();
        }

        //set custom list view adapter
        dpItemsListViewAdapter = new CustomMarksRowAdapter(getActivity(), dpItems);
        dpInputMarksListView.setAdapter(dpItemsListViewAdapter);

        //ask for input when list item clicked
        dpInputMarksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                flag = 0;
                //save marks using shared preferences
                spMarks = getActivity().getSharedPreferences("MARKS_LIST", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = spMarks.edit();

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.marks_input_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                // set prompts.xml to alert dialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setTitle(R.string.marks_entry_dialog_title);
                alertDialogBuilder.setPositiveButton(R.string.marks_entry_dialog_positive_btn_txt,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //check if edit text is empty
                                if (!(userInput.getText().toString()).equals("") && !(userInput.getText().toString()).isEmpty()){
                                    //check if enter marks exceeds maximum marks
                                    if ((Integer.parseInt(userInput.getText().toString())) > max)
                                        flag = 1;
                                    // get user input in edit text and set it to result
                                    if (flag == 0) {
                                        sample.setSubjectMarks(Integer.parseInt(userInput.getText().toString()));
                                        sample.setSubjectName(spSubject.getString("subject_" + position, ""));
                                        dpItems.remove(position);
                                        dpItems.add(position, sample);
                                        sample = new MarksRow();
                                        //set custom list view adapter
                                        dpItemsListViewAdapter = new CustomMarksRowAdapter(getActivity(), dpItems);
                                        dpInputMarksListView.setAdapter(dpItemsListViewAdapter);
                                        //refresh the list view
                                        dpInputMarksListView.invalidateViews();
                                        dpItemsListViewAdapter.notifyDataSetChanged();
                                        //save marks
                                        editor.putInt("marks_" + testPos + "_" + position, dpItems.get(position).getSubjectMarks());
                                        editor.commit();
                                    } else {
                                        //set marks to maximum marks if entered marks exceeds maximum marks
                                        sample.setSubjectMarks(max);
                                        sample.setSubjectName(spSubject.getString("subject_" + position, ""));
                                        dpItems.remove(position);
                                        dpItems.add(position, sample);
                                        sample = new MarksRow();
                                        //set custom list view adapter
                                        dpItemsListViewAdapter = new CustomMarksRowAdapter(getActivity(), dpItems);
                                        dpInputMarksListView.setAdapter(dpItemsListViewAdapter);
                                        //refresh the list view
                                        dpInputMarksListView.invalidateViews();
                                        dpItemsListViewAdapter.notifyDataSetChanged();
                                        //save marks
                                        editor.putInt("marks_" + testPos + "_" + position, dpItems.get(position).getSubjectMarks());
                                        editor.commit();
                                        Toast.makeText(getActivity(), String.format(getString(R.string.marks_exceed_range_error), max), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                    Toast.makeText(getActivity(), R.string.marks_field_empty_error, Toast.LENGTH_SHORT).show();
                            }
                        });
                alertDialogBuilder.setNegativeButton(R.string.marks_entry_dialog_negative_btn_text,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                //show soft keyboard
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                // show it
                alertDialog.show();
            }
        });
    }
}
