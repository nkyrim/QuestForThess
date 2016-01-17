package com.nkyrim.thessapp.ui.activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.nkyrim.thessapp.R;
import com.nkyrim.thessapp.ui.base.BaseActivity;
import com.nkyrim.thessapp.ui.fragments.PoiListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PoiListActivity extends BaseActivity {
	@Bind(R.id.pager) ViewPager viewPager;
	@Bind(R.id.tabs) TabLayout tabs;

	@Override
	protected int getLayoutResource() {
		return R.layout.activity_poi_list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
		tabs.setupWithViewPager(viewPager);
	}

	public class PagerAdapter extends FragmentPagerAdapter {
		private String[] titles = getResources().getStringArray(R.array.poi_types);

		public PagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return PoiListFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
	}
}
