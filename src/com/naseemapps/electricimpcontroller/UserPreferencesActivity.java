package com.naseemapps.electricimpcontroller;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class UserPreferencesActivity extends PreferenceActivity implements
		OnPreferenceChangeListener, OnPreferenceClickListener {

	final static String EXTRA_PREFRENCE = "extrPrefernce";
	final static String PREF_ELECTRIC_IMP_AGENT_ID = "prefElectecImpAgentId";
	final static String PREF_USER_ID = "prefUserId";

	EditTextPreference mElectricImpAgentId;
	EditTextPreference mUserId;
	Preference mAbout;
	Preference mHelp;
	SharedPreferences mSharedPrefs;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.user_preference);

		mElectricImpAgentId = (EditTextPreference) findPreference("prefElectecImpAgentId");
		mUserId = (EditTextPreference) findPreference("prefUserId");
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mAbout = findPreference("prefAbout");
		mHelp = findPreference("prefHelp");

		mElectricImpAgentId.setOnPreferenceChangeListener(this);
		mUserId.setOnPreferenceChangeListener(this);

		mElectricImpAgentId.setOnPreferenceClickListener(this);
		mUserId.setOnPreferenceClickListener(this);
		mAbout.setOnPreferenceClickListener(this);
		mHelp.setOnPreferenceClickListener(this);

		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			mAbout.setSummary(pInfo.versionName);
		} catch (NameNotFoundException e) {
		}

		loadPrefernces();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// Get data via the key
			switch (extras.getInt(EXTRA_PREFRENCE)) {
			case MainActivity.RES_ERR_DEV_BAD_AGENT_ID:
				mElectricImpAgentId.setIcon(R.drawable.ic_action_error);
				break;

			case MainActivity.RES_ERR_DEV_BAD_USER_ID:
				mUserId.setIcon(R.drawable.ic_action_error);
				break;

			default:
				break;
			}
			;
		}

	}

	private void loadPrefernces() {
		String agentId = mSharedPrefs.getString(PREF_ELECTRIC_IMP_AGENT_ID,
				this.getString(R.string.val_not_set));
		if (agentId.equals(""))
			agentId = getString(R.string.val_not_set);
		
		mElectricImpAgentId.setSummary(agentId);
		
		if (agentId.equals(getString(R.string.val_not_set)))
			mElectricImpAgentId.setText("");
		else
			mElectricImpAgentId.setText(agentId);
		
		mUserId.setSummary(mSharedPrefs.getString(PREF_USER_ID,
				this.getString(R.string.val_not_set)));
		mUserId.setText(mSharedPrefs.getString(PREF_USER_ID,
				this.getString(R.string.val_not_set)));
	}

	private void savePrefernces() {
		Editor editor = mSharedPrefs.edit();
		editor.putString(PREF_ELECTRIC_IMP_AGENT_ID,
				mElectricImpAgentId.getSummary() + "");

		editor.putString(PREF_USER_ID, mUserId.getSummary() + "");

		editor.commit();
	}

	@Override
	protected void onPause() {
		super.onPause();
		savePrefernces();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// if (preference.getKey().equals(PREF_ELECTRIC_IMP_AGENT_ID)) {
		// } else if (preference.getKey().equals(PREF_USER_ID)) {
		// }
		if (newValue.toString().equals("")
				|| newValue.toString().equals(getString(R.string.val_not_set))) {
			preference.setSummary(getString(R.string.val_not_set));
			((EditTextPreference) preference).setText("");
			preference.setIcon(R.drawable.ic_action_error);
		} else {
			((EditTextPreference) preference).setText(newValue.toString());
			preference.setSummary(newValue.toString());
			preference.setIcon(R.drawable.ic_action_help);
		}
		return false;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference.equals(mAbout)) {

		} else if (preference.equals(mHelp)) {
			String url = "https://github.com/appsnaseem/Electric-Imp-Controller";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
		} else {
			if (preference.getSummary().equals(getString(R.string.val_not_set))) {
				((EditTextPreference) preference).setText("");
			}
		}
		return false;
	}
}
