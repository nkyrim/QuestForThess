package com.nkyrim.thessapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nkyrim.thessapp.R;
import com.nkyrim.thessapp.domain.Achievement;
import com.nkyrim.thessapp.domain.ObjectivePoi;
import com.nkyrim.thessapp.persistence.DbHelper;
import com.nkyrim.thessapp.ui.base.BaseActivity;
import com.nkyrim.thessapp.ui.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AchievementActivity extends BaseActivity {
	@Bind(R.id.list) RecyclerView list;

	@Override
	protected int getLayoutResource() {
		return R.layout.activity_achievement;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		List<Achievement> li = DbHelper.getAllAchievements();
		BaseRecyclerAdapter adapter = new AchievementAdapter(li);
		list.setLayoutManager(new LinearLayoutManager(this));
		list.setAdapter(adapter);
	}

	class AchievementAdapter extends BaseRecyclerAdapter<Achievement, AchievementAdapter.AchievementViewHolder> {
		public AchievementAdapter(List<Achievement> dataset) {
			super(dataset);
		}

		@Override
		public AchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_achievement, parent, false);
			return new AchievementViewHolder(v);
		}

		@Override
		public void onBindViewHolder(AchievementViewHolder holder, int position) {
			Achievement p = dataset.get(position);
			holder.bind(p);
		}

		public class AchievementViewHolder extends RecyclerView.ViewHolder {
			@Bind(R.id.container) View container;
			@Bind(R.id.tv1) TextView tv1;
			@Bind(R.id.tv2) TextView tv2;
			@Bind(R.id.tv3) TextView tv3;

			public AchievementViewHolder(View v) {
				super(v);
				ButterKnife.bind(this, v);
			}

			public void bind(Achievement a) {
				tv1.setText(a.getType().getTitle());
				tv2.setText(a.getType().getDescription());
				tv3.setText(a.getCompletedOn());

				if(a.getType().getReqobjectives() < 4) {
					container.setBackgroundColor(ContextCompat.getColor(AchievementActivity.this, R.color.bronze));
				} else if(a.getType().getReqobjectives() < 8) {
					container.setBackgroundColor(ContextCompat.getColor(AchievementActivity.this, R.color.silver));
				} else if(a.getType().getReqobjectives() < 11) {
					container.setBackgroundColor(ContextCompat.getColor(AchievementActivity.this, R.color.gold));
				}
			}
		}
	}
}
