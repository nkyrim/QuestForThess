package com.nkyrim.thessapp.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nkyrim.thessapp.R;
import com.nkyrim.thessapp.domain.Poi;
import com.nkyrim.thessapp.persistence.DbHelper;
import com.nkyrim.thessapp.ui.base.BaseActivity;

import butterknife.BindView;

public class PoiDetailActivity extends BaseActivity {
	public static final String ARG_POI = "ARG_POI";
    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;
    @BindView(R.id.fabMap)
    FloatingActionButton fabMap;
    @BindView(R.id.banner)
    ImageView banner;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvInfo)
    TextView tvInfo;
    @BindView(R.id.tvRef)
    TextView tvRef;

	@Override
	protected int getLayoutResource() {
		return R.layout.activity_poi_detail;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Poi p = (Poi) getIntent().getExtras().getSerializable(ARG_POI);
		getSupportActionBar().setTitle(p.getTitle());
		tvDesc.setText(p.getDesc());
		if(p.getInfo() == null) tvInfo.setVisibility(View.GONE);
		else tvInfo.setText(p.getInfo());
		tvRef.setOnClickListener((v -> {
			Uri uri = Uri.parse(p.getRefUri());
			Intent i = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(i);
		}));

		Glide.with(this)
			 .load(p.getImgUri())
			 .crossFade()
			 .into(banner);

		fabAdd.setOnClickListener((v) -> {
									  int res = DbHelper.insertPoiObjective(p);
									  if(res == 1) Snackbar.make(v, R.string.objective_already_added, Snackbar.LENGTH_LONG).show();
									  else Snackbar.make(v, R.string.objective_added, Snackbar.LENGTH_LONG)
												   .setAction(R.string.undo, v2 -> DbHelper.deletePoiObjective(p)).show();
								  }
		);
		fabMap.setOnClickListener((v) -> {
									  Intent i = new Intent(this, MapActivity.class);
									  i.putExtra(MapActivity.ARG_POI, p);
									  startActivity(i);
								  }
		);
	}
}
