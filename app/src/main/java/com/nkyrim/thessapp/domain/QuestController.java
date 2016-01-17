package com.nkyrim.thessapp.domain;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;

import com.nkyrim.thessapp.R;
import com.nkyrim.thessapp.ThessApp;
import com.nkyrim.thessapp.persistence.DbHelper;
import com.nkyrim.thessapp.ui.activities.AchievementActivity;
import com.nkyrim.thessapp.ui.activities.ObjectiveListActivity;
import com.nkyrim.thessapp.ui.util.Settings;
import com.nkyrim.thessapp.util.Constants;

import java.util.Date;

public class QuestController {

	public static void completeObjective(ObjectivePoi ob) {
		Context cxt = ThessApp.getAppContext();

		// complete objective
		String timeNow = DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()).toString();
		ob.setCompletedOn(timeNow);
		DbHelper.updatePoiObjective(ob);

		// show notification objective complete
		Intent i = new Intent(cxt, ObjectiveListActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pi = PendingIntent.getActivity(cxt, 0, i, 0);

		Notification noti = new NotificationCompat.Builder(cxt)
				.setContentTitle(cxt.getString(R.string.objective_complete))
				.setContentText(ob.getPoi().getTitle())
				.setSmallIcon(R.drawable.ic_check)
				.setColor(ContextCompat.getColor(cxt, R.color.sand))
				.setContentIntent(pi)
				.setAutoCancel(true)
				.setDefaults(Notification.DEFAULT_ALL)
				.build();

		NotificationManager notiManager = (NotificationManager) cxt.getSystemService(Context.NOTIFICATION_SERVICE);
		notiManager.notify(Constants.NOTI_ID_OBJECTIVE, noti);

		// increase points
		int points = Settings.getInt(Settings.Key.POINTS, 0);
		Settings.setInt(Settings.Key.POINTS, ++points);

		// complete achievement according to points
		AchievementType type = DbHelper.getAchType(points);
		if(type != null) {
			Achievement ach = new Achievement(timeNow, type);
			DbHelper.insertAchievement(ach);

			// show Achievement notification
			Intent in = new Intent(cxt, AchievementActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent pin = PendingIntent.getActivity(cxt, 0, in, 0);

			noti = new NotificationCompat.Builder(cxt)
					.setContentTitle(cxt.getString(R.string.achievement_unlocked))
					.setContentText(ach.getType().getTitle())
					.setSmallIcon(R.drawable.ic_check)
					.setColor(ContextCompat.getColor(cxt, R.color.east_bay_blue))
					.setContentIntent(pin)
					.setAutoCancel(true)
					.setDefaults(Notification.DEFAULT_ALL)
					.build();

			notiManager.notify(Constants.NOTI_ID_ACHIEVEMENT, noti);
		}
	}

	public static void completeObjective(ObjectiveQr ob) {
		Context cxt = ThessApp.getAppContext();

		// complete objective
		String timeNow = DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()).toString();
		ob.setCompletedOn(timeNow);
		DbHelper.insertQrObjective(ob);

		// show notification objective complete
		Intent i = new Intent(cxt, ObjectiveListActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pi = PendingIntent.getActivity(cxt, 0, i, 0);

		Notification noti = new NotificationCompat.Builder(cxt)
				.setContentTitle(cxt.getString(R.string.objective_thesswiki_complete))
				.setContentText(ob.getQr().getTitle())
				.setSmallIcon(R.drawable.ic_check)
				.setColor(ContextCompat.getColor(cxt, R.color.sand))
				.setContentIntent(pi)
				.setAutoCancel(true)
				.setDefaults(Notification.DEFAULT_ALL)
				.build();

		NotificationManager notiManager = (NotificationManager) cxt.getSystemService(Context.NOTIFICATION_SERVICE);
		notiManager.notify(Constants.NOTI_ID_OBJECTIVE, noti);

		// increase points
		int points = Settings.getInt(Settings.Key.POINTS, 0);
		Settings.setInt(Settings.Key.POINTS, ++points);

		// complete achievement according to points
		AchievementType type = DbHelper.getAchType(points);
		if(type != null) {
			Achievement ach = new Achievement(timeNow, type);
			DbHelper.insertAchievement(ach);

			// show Achievement notification
			Intent in = new Intent(cxt, AchievementActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent pin = PendingIntent.getActivity(cxt, 0, in, 0);

			noti = new NotificationCompat.Builder(cxt)
					.setContentTitle(cxt.getString(R.string.achievement_unlocked))
					.setContentText(ach.getType().getTitle())
					.setSmallIcon(R.drawable.ic_check)
					.setColor(ContextCompat.getColor(cxt, R.color.east_bay_blue))
					.setContentIntent(pin)
					.setAutoCancel(true)
					.setDefaults(Notification.DEFAULT_ALL)
					.build();

			notiManager.notify(Constants.NOTI_ID_ACHIEVEMENT, noti);
		}
	}
}
