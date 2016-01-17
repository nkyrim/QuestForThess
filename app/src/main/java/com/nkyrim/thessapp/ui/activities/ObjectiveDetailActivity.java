package com.nkyrim.thessapp.ui.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nkyrim.thessapp.R;
import com.nkyrim.thessapp.domain.Achievement;
import com.nkyrim.thessapp.domain.AchievementType;
import com.nkyrim.thessapp.domain.ObjectivePoi;
import com.nkyrim.thessapp.domain.QuestController;
import com.nkyrim.thessapp.persistence.DbHelper;
import com.nkyrim.thessapp.ui.base.BaseActivity;
import com.nkyrim.thessapp.ui.util.Settings;
import com.nkyrim.thessapp.util.Constants;

import java.util.Date;

import butterknife.Bind;

public class ObjectiveDetailActivity extends BaseActivity {
	public static final String ARG_OBJECTIVE = "ARG_OBJECTIVE";

	@Bind(R.id.fabCheck) FloatingActionButton fabCheck;
	@Bind(R.id.fabMap) FloatingActionButton fabMap;
	@Bind(R.id.banner) ImageView banner;
	@Bind(R.id.tvDesc) TextView tvDesc;
	@Bind(R.id.tvInfo) TextView tvInfo;
	@Bind(R.id.tvRef) TextView tvRef;

	@Override
	protected int getLayoutResource() {
		return R.layout.activity_objective_detail;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ObjectivePoi ob = (ObjectivePoi) getIntent().getExtras().getSerializable(ARG_OBJECTIVE);
		getSupportActionBar().setTitle(ob.getPoi().getTitle());
		tvDesc.setText(ob.getPoi().getDesc());
		if(ob.getPoi().getInfo() == null) tvInfo.setVisibility(View.GONE);
		else tvInfo.setText(ob.getPoi().getInfo());
		tvRef.setOnClickListener((v -> {
			Uri uri = Uri.parse(ob.getPoi().getRefUri());
			Intent i = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(i);
		}));

		Glide.with(this)
			 .load(ob.getPoi().getImgUri())
			 .crossFade()
			 .into(banner);

		if(ob.getCompletedOn() != null) {
			fabCheck.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green)));
		} else {
			fabCheck.setOnClickListener((v) -> {
											if(ob.getCompletedOn() != null)
												Snackbar.make(v, R.string.objective_already_completed, Snackbar.LENGTH_LONG).show();
											else {
												new AlertDialog.Builder(this)
														.setTitle(R.string.attention)
														.setMessage(R.string.objective_complete_prompt)
														.setPositiveButton(R.string.yes, (d, w) -> {
															QuestController.completeObjective(ob);
															fabCheck.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor
																	(this, R.color.green)));
															fabCheck.setOnClickListener(null);
															d.dismiss();
														})
														.setNegativeButton(R.string.no, (d, w) -> d.dismiss())
														.setCancelable(true)
														.create()
														.show();
											}
										}
			);
		}
		fabMap.setOnClickListener((v) -> {
									  Intent i = new Intent(this, MapActivity.class);
									  i.putExtra(MapActivity.ARG_POI, ob.getPoi());
									  startActivity(i);
								  }
		);
	}
}
