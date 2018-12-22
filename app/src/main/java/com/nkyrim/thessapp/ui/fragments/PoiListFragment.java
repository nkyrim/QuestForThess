package com.nkyrim.thessapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nkyrim.thessapp.R;
import com.nkyrim.thessapp.domain.Poi;
import com.nkyrim.thessapp.persistence.DbHelper;
import com.nkyrim.thessapp.ui.activities.PoiDetailActivity;
import com.nkyrim.thessapp.ui.base.BaseFragment;
import com.nkyrim.thessapp.ui.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PoiListFragment extends BaseFragment {
	private static final String ARG_TYPE = "ARG_TYPE";
	@BindView(R.id.list)
	RecyclerView list;

	public PoiListFragment() {
	}

	public static PoiListFragment newInstance(int type) {
		PoiListFragment fragment = new PoiListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_TYPE, type);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		unbinder = ButterKnife.bind(this, view);

		int type = getArguments().getInt(ARG_TYPE);
		List<Poi> poi = DbHelper.getPois(type, false);
		PoiAdapter adapter = new PoiAdapter(poi);
		list.setLayoutManager(new LinearLayoutManager(getActivity()));
		list.setAdapter(adapter);

		return view;
	}

	class PoiAdapter extends BaseRecyclerAdapter<Poi, PoiAdapter.PoiViewHolder> {
		PoiAdapter(List<Poi> dataset) {
			super(dataset);
		}

		@Override
		public PoiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_poi, parent, false);
			return new PoiViewHolder(v);
		}

		@Override
		public void onBindViewHolder(PoiViewHolder holder, int position) {
			Poi p = dataset.get(position);
			holder.bind(p);
		}

		class PoiViewHolder extends RecyclerView.ViewHolder {
			@BindView(R.id.container)
			View container;
			@BindView(R.id.tv1)
			TextView tv1;
			@BindView(R.id.tv2)
			TextView tv2;
			@BindView(R.id.img1)
			ImageView img1;
			@BindView(R.id.img2)
			ImageView img2;

			PoiViewHolder(View v) {
				super(v);
				ButterKnife.bind(this, v);
			}

			void bind(Poi p) {
				tv1.setText(p.getTitle());
				tv2.setText(p.getDesc());
				Glide.with(getActivity())
					 .load(p.getImgSmallUri())
					 .crossFade()
					 .into(img1);
				img2.setOnClickListener(v -> {
					int res = DbHelper.insertPoiObjective(p);
					if(res == 1) Snackbar.make(v, R.string.objective_already_added, Snackbar.LENGTH_LONG).show();
					else Snackbar.make(v, R.string.objective_added, Snackbar.LENGTH_LONG)
							.setAction(R.string.undo, v2 -> DbHelper.deletePoiObjective(p)).show();
				});
				container.setOnClickListener(v -> {
					Intent i = new Intent(getActivity(), PoiDetailActivity.class);
					i.putExtra(PoiDetailActivity.ARG_POI, p);
					startActivity(i);
				});
			}
		}
	}
}
