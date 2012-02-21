//
//
//
package net.sig13.sensorlogger.prefs;

import android.app.Activity;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.*;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import net.sig13.sensorlogger.Constants;
import net.sig13.sensorlogger.R;

//
//
//
public class PollingFragment extends PreferenceFragment implements OnPreferenceChangeListener, OnPreferenceClickListener {

    private static final String LOG_NAME = "SL:PollingFragment";
    private CheckBoxPreference enablePolling;
    private ListPreference pollingInterval;
    private PreferenceManager pm;
    private SharedPreferences prefs;
    private BackupManager bm;
    private Activity activity;
    private Context context;
    private PreferenceScreen root;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Log.d(LOG_NAME, "onCreate()");

        //addPreferencesFromResource(R.xml.pref_polling);

        setPreferenceScreen(buildPreferenceTree());

        pm = getPreferenceManager();
        prefs = pm.getSharedPreferences();

        activity = getActivity();
        context = activity.getBaseContext();

        bm = new BackupManager(context);


    }

    private PreferenceScreen buildPreferenceTree() {

        root = pm.createPreferenceScreen(context);

        PreferenceCategory pCat = new PreferenceCategory(context);
        pCat.setTitle("Polling");
        root.addPreference(pCat);

        CheckBoxPreference pPref = new CheckBoxPreference(context);
        pPref.setKey("enable_polling");
        pPref.setTitle("enable_polling");
        pPref.setSummary("enable polling of sensor data");
        pPref.setDefaultValue(Boolean.TRUE);

        root.addPreference(pPref);

        ListPreference lp = new ListPreference(context);
        lp.setKey("polling_interval");
        lp.setTitle("polling interval");
        lp.setSummary("select polling interval for sensors");
        lp.setDependency("enable_polling");
        lp.setDialogTitle("polling interval");
        lp.setEntries(R.array.pollingIntervals);
        lp.setEntryValues(R.array.pollingIntervalValues);
        lp.setDefaultValue(Integer.decode("30000"));


        root.addPreference(lp);

//                <ListPreference
//            android:key="polling_interval"
//            android:title="polling_interval_title"
//            android:summary="polling_interval_summary"
//            android:entries="@array/pollingIntervals"
//            android:entryValues="@array/pollingIntervalValues"
//            android:defaultValue="300000"
//            android:dialogTitle="dialogTitle_polling_interval"
//            android:dependency="enable_polling"
//            />

        return root;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(LOG_NAME, "onActivityCreated");

        enablePolling = (CheckBoxPreference) findPreference(Constants.PREF_KEY_ENABLE_POLLING);
        enablePolling.setOnPreferenceChangeListener(this);

        pollingInterval = (ListPreference) findPreference(Constants.PREF_KEY_POLLING_INTERVAL);
        pollingInterval.setOnPreferenceChangeListener(this);


    }

    @Override
    public boolean onPreferenceChange(Preference pref, Object newValue) {

        Log.d(LOG_NAME, "onPreferenceChange()");

        String key = pref.getKey();
        Editor editor;
        boolean changed = false;

        // can't have a null here
        if (key == null) {
            return false;
        }

        // can't have a null here either in this instance so fuck that
        if (newValue == null) {
            return false;
        }

        editor = prefs.edit();

        if (key.equalsIgnoreCase(Constants.PREF_KEY_ENABLE_POLLING)) {
            Log.d(LOG_NAME, "enablePolling:" + newValue);
            if (newValue instanceof Boolean) {
                Log.d(LOG_NAME, "updateing enablePolling preference");
                editor.putBoolean(Constants.PREF_KEY_ENABLE_POLLING, Boolean.getBoolean(newValue.toString()));
                changed = true;

            } else {
                Log.d(LOG_NAME, "wanted a boolean preference got:" + newValue.getClass());
            }

        } else if (key.equalsIgnoreCase(Constants.PREF_KEY_POLLING_INTERVAL)) {

            Log.d(LOG_NAME, "pollingInterval:" + newValue);

            try {

                // check if it's an Integer parseable value
                int newValueAsInt = Integer.parseInt(newValue.toString());

                editor.putInt(Constants.PREF_KEY_POLLING_INTERVAL, newValueAsInt);
                changed = true;

            } catch (Exception e) {

                Log.d(LOG_NAME, "pollingInterval passed non parseable integer value:" + newValue, e);
            }

        } else {
            Log.w(LOG_NAME, "Unknown onPreferenceKeyChange:" + key + ":" + newValue);
        }

        boolean worked = editor.commit();

        if (worked == false) {
            Log.e(LOG_NAME, "Failed to commit preference changes");
        }


        if (changed == true) {
            bm.dataChanged();
        }


        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference pref) {
        Log.d(LOG_NAME, "onPreferenceClick()");
        return true;
    }
}