package com.nkyrim.thessapp.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nkyrim.thessapp.R;
import com.nkyrim.thessapp.ui.base.BaseFragment;
import com.nkyrim.thessapp.ui.util.Settings;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IntroFragment extends BaseFragment {
	private static final String ARG_POS = "ARG_POS";

	public IntroFragment() {
	}

	public static IntroFragment newInstance(int type) {
		IntroFragment fragment = new IntroFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_POS, type);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;

		int pos = getArguments().getInt(ARG_POS);
		if(pos == 0) view = inflater.inflate(R.layout.fragment_intro_1, container, false);
		if(pos == 1) view = inflater.inflate(R.layout.fragment_intro_2, container, false);
		if(pos == 2) view = inflater.inflate(R.layout.fragment_intro_3, container, false);
		if(pos == 3) view = inflater.inflate(R.layout.fragment_intro_4, container, false);
		if(pos == 4) {
			view = inflater.inflate(R.layout.fragment_intro_5, container, false);
			Button b = (Button) view.findViewById(R.id.btnBegin);
			b.setOnClickListener(v -> {
				Settings.setBoolean(Settings.Key.IS_FIRST_RUN, false);
				getActivity().finish();
			});
		}

		return view;
	}
}
