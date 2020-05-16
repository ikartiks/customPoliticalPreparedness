package com.yrdtechnologies.persistence;

import java.util.ArrayList;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.yrdtechnologies.R;

public class AppPreferences {

	private static AppPreferences appPreferences;

	private Context applicationContext;

	private SharedPreferences sharedPreferences;

	private AppPreferences(Context applicationContext) {
		this.applicationContext = applicationContext;

		Resources res = this.applicationContext.getResources();
		String preferencesName = res.getString(R.string.app_name);
		sharedPreferences = this.applicationContext.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
	}

	public static AppPreferences getAppPreferences(Context applicationContext) {
		if (appPreferences != null) {
			return appPreferences;
		}

		appPreferences = new AppPreferences(applicationContext);
		return appPreferences;
	}

	public void putBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return sharedPreferences.getBoolean(key, defaultValue);
	}

	public void putInt(String key, int value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public int getInt(String key, int defaultValue) {
		return sharedPreferences.getInt(key, defaultValue);
	}

	public void putLong(String key, long value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public long getLong(String key, long defaultValue) {
		return sharedPreferences.getLong(key, defaultValue);
	}

	public void putString(String key, String value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void putArray(String key, ArrayList<String> value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value.size());

		for (int i = 0; i < value.size(); i++) {
			editor.putString(key + i, value.get(i));
		}

		editor.commit();
	}

	public ArrayList<String> getArray(String key) {

		ArrayList<String> value = new ArrayList<String>();
		int size = sharedPreferences.getInt(key, 0);

		for (int i = 0; i < size; i++) {
			value.add(sharedPreferences.getString(key + i, ""));
		}
		return value;
	}

	public String getString(String key, String defaultValue) {
		return sharedPreferences.getString(key, defaultValue);
		
	}

	public void remove(String key) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}
	
	public boolean contains(String key) {
		return sharedPreferences.contains(key);
	}

}
