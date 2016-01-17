package com.nkyrim.thessapp.ui.base;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<DS, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
	protected final String TAG = getClass().getSimpleName();

	protected List<DS> dataset;
	protected List<DS> original;
	protected List<DS> filtered;

	public BaseRecyclerAdapter(List<DS> dataset) {
		this.dataset = dataset;
		original = dataset;
		filtered = new ArrayList<>();
	}

	public int getItemCount() {
		return dataset.size();
	}
}
