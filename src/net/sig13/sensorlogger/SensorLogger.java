/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sig13.sensorlogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 *
 * @author pee
 */
public class SensorLogger extends Activity {

    private final static String LN = "SensorLogger";
    public static final String PREFS_NAME = "SensorLoggerPrefs";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Log.d(LN, "onCreate");

        Intent intent = new Intent(this, SensorLoggerService.class);

        startService(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(LN, "onStart");
        // The activity is about to become visible.
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LN, "onResume");
        // The activity has become visible (it is now "resumed").
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LN, "onPause");
        // Another activity is taking focus (this activity is about to be "paused").
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(LN, "onStop");
        // The activity is no longer visible (it is now "stopped")
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(LN, "onDestroy");
        // The activity is about to be destroyed.
    }
}