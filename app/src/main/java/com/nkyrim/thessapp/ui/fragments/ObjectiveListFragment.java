package com.nkyrim.thessapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nkyrim.thessapp.R;
import com.nkyrim.thessapp.domain.ObjectivePoi;
import com.nkyrim.thessapp.domain.ObjectiveQr;
import com.nkyrim.thessapp.persistence.DbHelper;
import com.nkyrim.thessapp.ui.activities.ObjectiveDetailActivity;
import com.nkyrim.thessapp.ui.base.BaseFragment;
import com.nkyrim.thessapp.ui.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ObjectiveListFragment extends BaseFragment {
	private static final String ARG_TYPE = "ARG_TYPE";
    @BindView(R.id.list)
    RecyclerView list;

	public ObjectiveListFragment() {
	}

	public static ObjectiveListFragment newInstance(int type) {
		ObjectiveListFragment fragment = new ObjectiveListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_TYPE, type);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		list.setLayoutManager(new LinearLayoutManager(getActivity()));
		BaseRecyclerAdapter adapter;
		int type = getArguments().getInt(ARG_TYPE);
		if(type == 0) {
			List<ObjectivePoi> li = DbHelper.getAllPoiObjectives();
			adapter = new PoiObjectiveAdapter(li);
		} else {
			List<ObjectiveQr> li = DbHelper.getAllQrObjectives();
			adapter = new QrObjectiveAdapter(li);
		}

		list.setAdapter(adapter);
	}

	class PoiObjectiveAdapter extends BaseRecyclerAdapter<ObjectivePoi, PoiObjectiveAdapter.ObjectiveViewHolder> {
		public PoiObjectiveAdapter(List<ObjectivePoi> dataset) {
			super(dataset);
		}

		@Override
		public ObjectiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_objective, parent, false);
			return new ObjectiveViewHolder(v);
		}

		@Override
		public void onBindViewHolder(ObjectiveViewHolder holder, int position) {
			ObjectivePoi p = dataset.get(position);
			holder.bind(p);
		}

		public class ObjectiveViewHolder extends RecyclerView.ViewHolder {
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
			private ObjectivePoi p;

			public ObjectiveViewHolder(View v) {
				super(v);
				ButterKnife.bind(this, v);
			}

			public void bind(ObjectivePoi p) {
				this.p = p;
				tv1.setText(p.getPoi().getTitle());
				Glide.with(img1.getContext())
					 .load(p.getPoi().getImgSmallUri())
					 .crossFade()
					 .into(img1);
				if(p.getCompletedOn() != null) {
					tv2.setText(p.getCompletedOn());
					img2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.check));
					img2.setOnClickListener(null);
				} else {
					tv2.setText(null);
					img2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.remove));
					img2.setOnClickListener((v -> removeObjective(p)));
				}

				container.setOnClickListener((v) -> {
					Intent i = new Intent(getActivity(), ObjectiveDetailActivity.class);
					i.putExtra(ObjectiveDetailActivity.ARG_OBJECTIVE, p);
					startActivity(i);
				});

			}

			private void removeObjective(ObjectivePoi p) {
				DbHelper.deletePoiObjective(p.getPoi());
				dataset.remove(p);
				notifyDataSetChanged();
			}
		}
	}

	class QrObjectiveAdapter extends BaseRecyclerAdapter<ObjectiveQr, QrObjectiveAdapter.ObjectiveViewHolder> {
		public QrObjectiveAdapter(List<ObjectiveQr> dataset) {
			super(dataset);
		}

		@Override
		public ObjectiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_objectiveqr, parent, false);
			return new ObjectiveViewHolder(v);
		}

		@Override
		public void onBindViewHolder(ObjectiveViewHolder holder, int position) {
			ObjectiveQr p = dataset.get(position);
			holder.bind(p);
		}

		public class ObjectiveViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.container)
			View container;
            @BindView(R.id.tv1)
			TextView tv1;
            @BindView(R.id.tv2)
			TextView tv2;
			private ObjectiveQr p;

			public ObjectiveViewHolder(View v) {
				super(v);
				ButterKnife.bind(this, v);
			}

			public void bind(ObjectiveQr p) {
				this.p = p;
				tv1.setText(p.getQr().getTitle());
				tv2.setText(p.getCompletedOn());
			}
		}
	}
}

