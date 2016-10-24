package alobar.preferencetest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

/**
 * Fragment for demoing module alobar-preference
 */
public class MyPreferenceFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String TAG = MyPreferenceFragment.class.getSimpleName();

    // must be the same keys as used in /res/xml/prefs.xml
    private final static String PREF_PERSISTS_PREFERENCES = "key_persists_preferences";
    private final static String PREF_GIVE_ME_A_NUMBER = "key_give_me_a_number";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        // attach change listeners to the preference widgets
        findPreference(PREF_PERSISTS_PREFERENCES).setOnPreferenceChangeListener(this);
        findPreference(PREF_GIVE_ME_A_NUMBER).setOnPreferenceChangeListener(this);

        // init the summary of the 'Give me a number' preference widget with its value
        int aNumber = getPreferenceManager().getSharedPreferences().getInt(PREF_GIVE_ME_A_NUMBER, 0);
        Preference giveMeANumberPreference = findPreference(PREF_GIVE_ME_A_NUMBER);
        giveMeANumberPreference.setSummary(Integer.toString(aNumber));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    /**
     * Called when a Preference has been changed by the user. This is called before the state of the
     * Preference is about to be updated and before the state is persisted. Return true to persist
     * the changed value, or false to ignore the change.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // show that a preference is about to be updated
        Log.i(TAG, String.format("onPreferenceChange(key: %s, newValue: %s)", preference.getKey(), newValue.toString()));

        // return whether to allow or block the update
        switch (preference.getKey()) {
            case PREF_PERSISTS_PREFERENCES:
                // This preference should always be persisted
                return true;
            case PREF_GIVE_ME_A_NUMBER:
                // we want this preference to only be persisted when PREF_PERSIST_PREFERENCES is set to true.
                boolean allowChange = getPreferenceManager().getSharedPreferences().getBoolean(PREF_PERSISTS_PREFERENCES, false);
                if (allowChange) {
                    // update the summary of the 'Give me a number' preference widget with its new value
                    preference.setSummary(newValue.toString());
                }
                return allowChange;
            default:
                return true;
        }
    }

    /**
     * Called when a shared preference is changed, added, or removed. This may be called even if a
     * preference is set to its existing value.
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // show when a preference is actually being persisted.
        Log.i(TAG, String.format("onSharedPreferenceChanged(key: %s)", key));
    }
}