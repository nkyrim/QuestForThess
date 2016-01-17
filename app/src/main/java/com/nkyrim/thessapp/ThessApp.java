package com.nkyrim.thessapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.nkyrim.thessapp.ui.activities.IntroActivity;
import com.nkyrim.thessapp.ui.util.Settings;

/**
 * Application class to allow easy usage of Application Context in general purpose classes, and possibly store global objects.
 * <b>Necessary for MultiDex on pre-Lollipop devices</b>
 */
public class ThessApp extends Application {
	private static Context context;

	public static Context getAppContext() {
		return context;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}
}
