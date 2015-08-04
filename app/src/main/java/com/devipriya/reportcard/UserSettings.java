package com.devipriya.reportcard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class UserSettings extends PreferenceActivity {

    static SharedPreferences spSettings;
    static SharedPreferences spGradePref;
    static SharedPreferences spSubject;
    static SharedPreferences spTest;
    static SharedPreferences spMarks;

    static SharedPreferences.Editor editSettings;
    static SharedPreferences.Editor editGradePref;
    static SharedPreferences.Editor editSubject;
    static SharedPreferences.Editor editTest;
    static SharedPreferences.Editor editMarks;

    static int checkShowGrade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        spSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        spGradePref = getSharedPreferences("GRADE_PREF", Context.MODE_PRIVATE);
        spSubject = getSharedPreferences("SUBJECT_LIST", Context.MODE_PRIVATE);
        spTest = getSharedPreferences("TEST_LIST", Context.MODE_PRIVATE);
        spMarks = getSharedPreferences("MARKS_LIST", Context.MODE_PRIVATE);
        checkShowGrade = Integer.parseInt(spSettings.getString("gradePref", "1"));

        getFragmentManager().beginTransaction().replace(android.R.id.content, new UserSettingsFragment()).commit();
    }

    public static class UserSettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            //refresh enable/disable "Edit Rank Range" preference
            final ListPreference gradePref = (ListPreference) findPreference("gradePref");
            gradePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(Integer.parseInt(newValue.toString()) == 3){
                        final Preference rangePref = findPreference("rangePref");
                        rangePref.setEnabled(true);
                    }
                    else{
                        final Preference rangePref = findPreference("rangePref");
                        rangePref.setEnabled(false);
                    }
                    return true;
                }
            });

            //show grade preferences if user wants to show grades
            final Preference rangePref = findPreference("rangePref");
            if(checkShowGrade == 3)
                rangePref.setEnabled(true);
            else
                rangePref.setEnabled(false);
            rangePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent gradeRange = new Intent(getActivity(), GradePref.class);
                    startActivity(gradeRange);
                    return false;
                }
            });

            //if pass % exceeds 100%
            final EditTextPreference pass = (EditTextPreference) findPreference("prefPassPercent");
            pass.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int tempPass;
                    if ((newValue.toString().equals("")))
                        tempPass = 30;
                    else
                        tempPass = Integer.parseInt(newValue.toString());
                    if (tempPass > 100) {
                        SharedPreferences spDef = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor spDefEditor = spDef.edit(); // Get preference in editor mode
                        spDefEditor.putString("prefPassPercent", "30"); // set your default value here (could be empty as well)
                        spDefEditor.apply();

                        // Now, manually update it's value to default/empty
                        pass.setText("30"); // Now, if you click on the item, you'll see the value you've just set here
                        Toast.makeText(getActivity(), "Exceeds 100%. Set to default pass %.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        SharedPreferences spDef = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor spDefEditor = spDef.edit(); // Get preference in editor mode
                        spDefEditor.putString("prefPassPercent", newValue.toString()); // set your default value here (could be empty as well)
                        spDefEditor.apply();

                        // Now, manually update it's value to default/empty
                        pass.setText(newValue.toString()); // Now, if you click on the item, you'll see the value you've just set here
                    }
                    return false;
                }
            });

            //show rank range
            Preference seeRank = findPreference("seeRank");
            seeRank.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    LayoutInflater factory = LayoutInflater.from(getActivity());
                    final View rankRangeView = factory.inflate(R.layout.rank_range_view, null);
                    //rank_range_view is an Layout XML file containing text field to display in alert dialog

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setTitle("RANK RANGE");
                    builder1.setView(rankRangeView);
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do nothing
                                }
                            });

                    AlertDialog rank = builder1.create();
                    rank.show();

                    return false;
                }
            });

            //confirm and clear all user data
            Preference prefClear = findPreference("prefClear");
            prefClear.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("Even the marks will be deleted. Are you sure you want to clear all user data?");
                    builder1.setTitle("CLEAR ALL");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //clear all shared preferences files
                                    editSettings = spSettings.edit();
                                    editGradePref = spGradePref.edit();
                                    editSubject = spSubject.edit();
                                    editTest = spTest.edit();
                                    editMarks = spMarks.edit();

                                    editSettings.clear();
                                    editSettings.apply();

                                    editGradePref.clear();
                                    editGradePref.apply();

                                    editSubject.clear();
                                    editSubject.apply();

                                    editTest.clear();
                                    editTest.apply();

                                    editMarks.clear();
                                    editMarks.apply();

                                    Toast.makeText(getActivity(), "All User Data Cleared.", Toast.LENGTH_SHORT).show();

/*                                    setPreferenceScreen(null);
                                    addPreferencesFromResource(R.xml.settings);*/
                                    getFragmentManager().beginTransaction().replace(android.R.id.content, new UserSettingsFragment()).commit();
                                }
                            });
                    builder1.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //don't do anything
                                    dialog.cancel();
                                }
                            });

                    AlertDialog clearAll = builder1.create();
                    clearAll.show();

                    return false;
                }
            });
        }
    }
}