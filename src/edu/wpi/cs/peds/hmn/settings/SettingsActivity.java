package edu.wpi.cs.peds.hmn.settings;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import edu.wpi.cs.peds.hmn.app.R;
import edu.wpi.cs.peds.hmn.app.SleekAndroidActivity;

/**
 * 
 * @author Zhenhao Lei, zlei@wpi.edu 
 * 
 */
public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
  
    @Override
    protected void onResume() {
        super.onResume();
        // Registers a listener whenever a key changes            
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
  
    @Override
    protected void onPause() {
        super.onPause();

       // Unregisters the listener set in onResume().
       // It's best practice to unregister listeners when your app isn't using them to cut down on 
       // unnecessary system overhead. You do this in onPause().            
       getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);    
    }
  
    // When the user changes the preferences selection, 
    // onSharedPreferenceChanged() restarts the main activity as a new
    // task. Sets the refreshDisplay flag to "true" to indicate that
    // the main activity should update its display.
    // The main activity queries the PreferenceManager to get the latest settings.
    
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {    
        // Sets refreshDisplay to true so that when the user returns to the main
        // activity, the display refreshes to reflect the new settings.
        SleekAndroidActivity.refreshDisplay = true;
    }
}