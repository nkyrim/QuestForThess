package com.nkyrim.thessapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.nkyrim.thessapp.R;
import com.nkyrim.thessapp.ui.base.BaseActivity;
import com.nkyrim.thessapp.ui.util.Settings;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.fab)
    FloatingActionButton fab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);

		if(Settings.getBoolean(Settings.Key.IS_FIRST_RUN, true)) {
			Intent i = new Intent(this, IntroActivity.class);
			startActivity(i);
		}

		fab.setOnClickListener(v -> {
			Intent i = new Intent(this, AboutActivity.class);
			startActivity(i);
		});
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.activity_main;
	}

	public void showMap(View v) {
		Intent i = new Intent(this, MapActivity.class);
		startActivity(i);
	}

	public void showQrcode(View v) {
		Intent i = new Intent(this, QrcodeActivity.class);
		startActivity(i);
	}

	public void showObjectives(View v) {
		Intent i = new Intent(this, ObjectiveListActivity.class);
		startActivity(i);
	}

	public void showPois(View v) {
		Intent i = new Intent(this, PoiListActivity.class);
		startActivity(i);
	}

	public void showAchievements(View v) {
		Intent i = new Intent(this, AchievementActivity.class);
		startActivity(i);
	}

	public void showShare(View v) {
		Intent i = new Intent(this, ShareActivity.class);
		startActivity(i);
	}
}
