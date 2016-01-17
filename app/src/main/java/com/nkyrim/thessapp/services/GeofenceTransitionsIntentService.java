package com.nkyrim.thessapp.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.nkyrim.thessapp.R;
import com.nkyrim.thessapp.domain.ObjectivePoi;
import com.nkyrim.thessapp.domain.QuestController;
import com.nkyrim.thessapp.persistence.DbHelper;
import com.nkyrim.thessapp.ui.activities.ObjectiveListActivity;
import com.nkyrim.thessapp.util.Constants;

import java.util.Date;
import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {

	public GeofenceTransitionsIntentService() {
		super("GeofenceTransitionsIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
		List<Geofence> list = geofencingEvent.getTriggeringGeofences();

		for (Geofence g : list) {
			int id = Integer.parseInt(g.getRequestId());
			ObjectivePoi p = DbHelper.getPoiObjective(id);
			if(p.getCompletedOn() != null) return;
			QuestController.completeObjective(p);
		}
	}
}
