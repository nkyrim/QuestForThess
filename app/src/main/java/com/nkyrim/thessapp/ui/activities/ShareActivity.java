package com.nkyrim.thessapp.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nkyrim.thessapp.R;
import com.nkyrim.thessapp.domain.Achievement;
import com.nkyrim.thessapp.domain.ObjectivePoi;
import com.nkyrim.thessapp.persistence.DbHelper;
import com.nkyrim.thessapp.ui.base.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class ShareActivity extends BaseActivity {
	@BindView(R.id.list)
	LinearLayout lv;
	@BindView(R.id.tv1)
	TextView tv1;
	@BindView(R.id.fab)
	FloatingActionButton fab;

	@Override
	protected int getLayoutResource() {
		return R.layout.activity_share;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		List<Achievement> la = DbHelper.getAllAchievements();
		if(la.isEmpty()) {
			Toast.makeText(ShareActivity.this, R.string.no_achievements, Toast.LENGTH_SHORT).show();
			return;
		}

		Achievement a = la.get(la.size() - 1);
		tv1.setText(a.getType().getTitle());

		int i = 0;
		List<ObjectivePoi> list = DbHelper.getAllPoiObjectives();
		for (ObjectivePoi o : list) {
			if(i < 3 && o.getCompletedOn() != null) {
				View v = getLayoutInflater().inflate(R.layout.row_share, null);
				TextView tv1 = v.findViewById(R.id.tv1);
				TextView tv2 = v.findViewById(R.id.tv2);
				ImageView img1 = v.findViewById(R.id.img1);
				tv1.setText(o.getPoi().getTitle());
				tv2.setText(o.getCompletedOn());
				Glide.with(img1.getContext())
					 .load(o.getPoi().getImgSmallUri())
					 .crossFade()
					 .into(img1);
				lv.addView(v);
				i++;
			}
		}

		fab.setOnClickListener((v -> {
			String now = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", new Date()).toString();

			try {
				// image naming and path  to include sd card  appending name you choose for file
				String path = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

				// create bitmap screen capture
				View v1 = getWindow().getDecorView().getRootView().findViewById(R.id.list);
				v1.setDrawingCacheEnabled(true);
				Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
				v1.setDrawingCacheEnabled(false);

				File imageFile = new File(path);

				FileOutputStream outputStream = new FileOutputStream(imageFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
				outputStream.flush();
				outputStream.close();

				// Share image
				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				sharingIntent.setType("image/jpeg");
				sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
				startActivity(Intent.createChooser(sharingIntent, "Share image using"));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}));
	}

}
