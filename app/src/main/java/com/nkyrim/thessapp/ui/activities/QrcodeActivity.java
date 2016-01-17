package com.nkyrim.thessapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;
import com.nkyrim.thessapp.R;
import com.nkyrim.thessapp.domain.ObjectiveQr;
import com.nkyrim.thessapp.domain.QrCode;
import com.nkyrim.thessapp.domain.QuestController;
import com.nkyrim.thessapp.persistence.DbHelper;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrcodeActivity extends Activity implements ZXingScannerView.ResultHandler {
	private ZXingScannerView scannerView;

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		scannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
		setContentView(scannerView);                // Set the scanner view as the content view
	}

	@Override
	public void onResume() {
		super.onResume();
		scannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
		scannerView.startCamera();          // Start camera on resume
	}

	@Override
	public void onPause() {
		super.onPause();
		scannerView.stopCamera();           // Stop camera on pause
	}

	@Override
	public void handleResult(Result result) {
		List<QrCode> list = DbHelper.getAllQrCodes();

		for (QrCode qr : list) {
			if(qr.getLink().equals(result.getText())) {
				QuestController.completeObjective(new ObjectiveQr(qr));
				Toast.makeText(this, getString(R.string.qr_scanned, qr.getTitle()), Toast.LENGTH_LONG).show();
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(qr.getLink()));
				startActivity(i);
				finish();
				return;
			}
		}

		Toast.makeText(this, R.string.qr_scan_failed, Toast.LENGTH_LONG).show();
		finish();
	}
}
