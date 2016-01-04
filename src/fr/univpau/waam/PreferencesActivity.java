package fr.univpau.waam;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import fr.univpau.util.SeekBarPreference;

public class PreferencesActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	 public static class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	        private SeekBarPreference _seekBarPref;

	        @Override
	        public void onCreate(Bundle savedInstanceState) {

	            super.onCreate(savedInstanceState);

	            // Load the preferences from an XML resource
	            addPreferencesFromResource(R.xml.preferences);


	            // Get widgets :
	            _seekBarPref = (SeekBarPreference) this.findPreference("SEEKBAR_VALUE");

	            // Set listener :
	            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

	            // Set seekbar summary :
	            int radius = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getInt("SEEKBAR_VALUE", 50);
	            _seekBarPref.setSummary(this.getString(R.string.title_activity_preferences).replace("$1", ""+radius));
	        }

	        @Override
	        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

	            // Set seekbar summary :
	            int radius = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getInt("SEEKBAR_VALUE", 50);
	            _seekBarPref.setSummary(this.getString(R.string.app_name).replace("$1", ""+radius));
	            
	            
	        }

	    }
}
