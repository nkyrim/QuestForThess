package com.nkyrim.thessapp.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import butterknife.Unbinder;
import icepick.Icepick;

/**
 * Base Fragment that adds basic functionality like view injection and state preservation
 */
public class BaseFragment extends Fragment {
	protected Unbinder unbinder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Icepick.restoreInstanceState(this, savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Icepick.saveInstanceState(this, outState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (unbinder != null) unbinder.unbind();
	}
}
