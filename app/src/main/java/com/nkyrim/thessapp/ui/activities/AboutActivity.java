package com.nkyrim.thessapp.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.nkyrim.thessapp.R;
import com.nkyrim.thessapp.ui.base.BaseActivity;

import butterknife.Bind;

public class AboutActivity extends BaseActivity {
	@Bind(R.id.fab) FloatingActionButton fab;
	@Bind(R.id.txtCredits) TextView txt;

	@Override
	protected int getLayoutResource() {
		return R.layout.activity_about;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		txt.setText(Html.fromHtml(getString(R.string.credits)));

		fab.setOnClickListener(v -> {
			Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "nkyrim@gmail.com", null));
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Quest for Thessaloniki");
			emailIntent.putExtra(Intent.EXTRA_TEXT, "");
			startActivity(Intent.createChooser(emailIntent, "Send email"));
		});
	}

	public void showIntro(View v) {
		Intent i = new Intent(this, IntroActivity.class);
		startActivity(i);
	}
}
