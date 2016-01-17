package com.nkyrim.thessapp.ui.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.nkyrim.thessapp.ThessApp;

/**
 * Controller for the application settings stored in SharedPreferences
 */
public final class Settings {
	private static final String TAG = "Settings";

	private static SharedPreferences getPrefs() {
		return ThessApp.getAppContext().getSharedPreferences("app_settings", Context.MODE_PRIVATE);
	}

	public static boolean getBoolean(Key key, boolean defValue) {
		return getPrefs().getBoolean(key.name(), defValue);
	}

	public static int getInt(Key key, int defValue) {
		return getPrefs().getInt(key.name(), defValue);
	}

	public static long getLong(Key key, long defValue) {
		return getPrefs().getLong(key.name(), defValue);
	}

	public static String getString(Key key, String defValue) {
		return getPrefs().getString(key.name(), defValue);
	}

	public static void setBoolean(Key key, boolean value) {
		getPrefs().edit().putBoolean(key.name(), value).commit();
	}

	public static void setInt(Key key, int value) {
		getPrefs().edit().putInt(key.name(), value).commit();
	}

	public static void setLong(Key key, long value) {
		getPrefs().edit().putLong(key.name(), value).commit();
	}

	public static void setString(Key key, String value) {
		getPrefs().edit().putString(key.name(), value).commit();
	}

	public static void setupFirstRun() {
		if(getBoolean(Key.IS_FIRST_RUN, true)) {
			setBoolean(Key.IS_FIRST_RUN, false);
		}
	}

	public enum Key {
		POINTS,
		IS_FIRST_RUN
	}
}
