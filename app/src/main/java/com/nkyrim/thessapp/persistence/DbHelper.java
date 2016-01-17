package com.nkyrim.thessapp.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nkyrim.thessapp.R;
import com.nkyrim.thessapp.ThessApp;
import com.nkyrim.thessapp.domain.Achievement;
import com.nkyrim.thessapp.domain.AchievementType;
import com.nkyrim.thessapp.domain.ObjectivePoi;
import com.nkyrim.thessapp.domain.ObjectiveQr;
import com.nkyrim.thessapp.domain.Poi;
import com.nkyrim.thessapp.domain.QrCode;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteAssetHelper {
	private static DbHelper instance = null;
	private static final String DATABASE_NAME = "data.db";
	private static final int DATABASE_VERSION = 1;

	// language specific tables
	private static final String TABLE_POI_EN = "pois_en";
	private static final String TABLE_POI_GR = "pois_gr";
	private static final String TABLE_QR_EN = "qrs_en";
	private static final String TABLE_QR_GR = "qrs_gr";
	private static final String TABLE_ACHTYPE_EN = "achievtypes_en";
	private static final String TABLE_ACHTYPE_GR = "achievtypes_gr";

	//
	private static final String TABLE_POI_OBJECTIVES = "objectivepois";
	private static final String TABLE_QR_OBJECTIVES = "objectiveqrs";
	private static final String TABLE_ACHIEVEMENTS = "achievements";
	private static String TABLE_POI;
	private static String TABLE_QR;
	private static String TABLE_ACH_TYPES;

	private DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// do nothing
	}

	public static DbHelper getInstance() {
		resolveTables();
		if(instance == null) {
			instance = new DbHelper(ThessApp.getAppContext());
		}
		return instance;
	}

	private static void resolveTables() {
		if(ThessApp.getAppContext().getString(R.string.lang).equals("en")) {
			TABLE_POI = TABLE_POI_EN;
			TABLE_QR = TABLE_QR_EN;
			TABLE_ACH_TYPES = TABLE_ACHTYPE_EN;
		} else if(ThessApp.getAppContext().getString(R.string.lang).equals("gr")) {
			TABLE_POI = TABLE_POI_GR;
			TABLE_QR = TABLE_QR_GR;
			TABLE_ACH_TYPES = TABLE_ACHTYPE_GR;
		}
	}

	public static List<Poi> getPois(int type, boolean isObjective) {
		// Open db
		DbHelper dbhelper = getInstance();
		SQLiteDatabase db = dbhelper.getReadableDatabase();

		// Select query
		Cursor c;

		if(!isObjective) {
			String where = null;
			if(type > -1) where = "type=" + type;
			c = db.query(TABLE_POI, null, where, null, null, null, null);
		} else {
			String where = "";
			if(type > -1) where = "WHERE type=" + type;
			c = db.rawQuery("SELECT t1.id, t1.title, t1.description, t1.info, t1.referencelink, " +
									"t1.imglink, t1.imglinksmall, t1.lat, t1.lng, t1.type " +
									"FROM " + TABLE_POI + " as t1 " +
									"INNER JOIN " + TABLE_POI_OBJECTIVES + " as t2 " +
									"ON t1.id=t2.pid " +
									where, null);
		}

		ArrayList<Poi> list = new ArrayList<>();
		Poi poi;

		// Get the records
		c.moveToFirst();
		while (!c.isAfterLast()) {
			poi = new Poi(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4),
						  c.getString(5), c.getString(6), c.getDouble(7), c.getDouble(8), c.getInt(9));

			list.add(poi);
			c.moveToNext();
		}

		// close db
		c.close();
		db.close();

		return list;
	}

	public static List<ObjectivePoi> getAllPoiObjectives() {
		// Open db
		DbHelper dbhelper = getInstance();
		SQLiteDatabase db = dbhelper.getReadableDatabase();

		// Select query
		Cursor c = db.rawQuery("SELECT t1.id, t1.completedon, t2.id, title, description, info, referencelink, " +
									   "imglink, imglinksmall, lat, lng, type " +
									   "FROM " + TABLE_POI_OBJECTIVES + " as t1 " +
									   "INNER JOIN " + TABLE_POI + " as t2 " +
									   "ON t1.pid=t2.id", null);

		ArrayList<ObjectivePoi> list = new ArrayList<>();
		ObjectivePoi poi;

		// Get the records
		c.moveToFirst();
		while (!c.isAfterLast()) {
			poi = new ObjectivePoi(c.getInt(0), c.getString(1),
								   new Poi(c.getInt(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6),
										   c.getString(7), c.getString(8), c.getDouble(9), c.getDouble(10), c.getInt(11)));
			list.add(poi);
			c.moveToNext();
		}

		// close db
		c.close();
		db.close();

		return list;
	}

	public static List<Achievement> getAllAchievements() {
		// Open db
		DbHelper dbhelper = getInstance();
		SQLiteDatabase db = dbhelper.getReadableDatabase();

		// Select query
		Cursor c = db.rawQuery("select b.id, b.completedon, a.id, a.title, a.description, a.reqobjectives " +
									   "from " + TABLE_ACH_TYPES + " a " +
									   "inner join " + TABLE_ACHIEVEMENTS + " b on a.id = b.aid", null);

		ArrayList<Achievement> list = new ArrayList<>();
		Achievement achievement;

		// Get the records
		c.moveToFirst();
		while (!c.isAfterLast()) {
			achievement = new Achievement(c.getInt(0), c.getString(1),
										  new AchievementType(c.getInt(2), c.getString(3), c.getString(4), c.getInt(5)));
			list.add(achievement);
			c.moveToNext();
		}

		// close db
		c.close();
		db.close();

		return list;
	}

	public static List<ObjectiveQr> getAllQrObjectives() {
		// Open db
		DbHelper dbhelper = getInstance();
		SQLiteDatabase db = dbhelper.getReadableDatabase();

		// Select query
		Cursor c = db.rawQuery("select a.id,  a.completedon, b.id, b.title, b.link " +
									   "from " + TABLE_QR_OBJECTIVES + " a " +
									   "inner join " + TABLE_QR + " b on a.qid = b.id ", null);

		List<ObjectiveQr> list = new ArrayList<>();
		ObjectiveQr ob;

		// Get the records
		c.moveToFirst();
		while (!c.isAfterLast()) {
			ob = new ObjectiveQr(c.getInt(0), c.getString(1), new QrCode(c.getInt(2), c.getString(3), c.getString(4)));
			list.add(ob);
			c.moveToNext();
		}

		// close db
		c.close();
		db.close();

		return list;
	}

	public static List<QrCode> getAllQrCodes() {
		// Open db
		DbHelper dbhelper = getInstance();
		SQLiteDatabase db = dbhelper.getReadableDatabase();

		// Select query
		Cursor c = db.query(TABLE_QR, null, null, null, null, null, null);

		List<QrCode> list = new ArrayList<>();
		QrCode ob;

		// Get the records
		c.moveToFirst();
		while (!c.isAfterLast()) {
			ob = new QrCode(c.getInt(0), c.getString(1), c.getString(2));
			list.add(ob);
			c.moveToNext();
		}

		// close db
		c.close();
		db.close();

		return list;
	}

	public static int insertPoiObjective(Poi ob) {
		// Open db
		DbHelper dbhelper = getInstance();
		SQLiteDatabase db = dbhelper.getWritableDatabase();

		// check for existing entry
		Cursor c = db.query(TABLE_POI_OBJECTIVES, null, "pid=" + ob.getId(), null, null, null, null);
		c.moveToFirst();
		int count = c.getCount();
		c.close();
		if(count > 0) return 1;

		// Create values
		ContentValues values = new ContentValues();
		values.put("pid", ob.getId());
		db.insert(TABLE_POI_OBJECTIVES, null, values);

		// close db
		db.close();
		return 0;
	}

	public static void deletePoiObjective(Poi p) {
		// Open db
		DbHelper dbhelper = getInstance();
		SQLiteDatabase db = dbhelper.getWritableDatabase();

		//Delete row
		db.delete(TABLE_POI_OBJECTIVES, "pid=" + p.getId(), null);

		// close db
		db.close();

	}

	public static void insertAchievement(Achievement ach) {
		// Open db
		DbHelper dbhelper = getInstance();
		SQLiteDatabase db = dbhelper.getWritableDatabase();

		// Create values
		ContentValues values = new ContentValues();
		values.put("completedon", ach.getCompletedOn());
		values.put("aid", ach.getType().getId());
		db.insert(TABLE_ACHIEVEMENTS, null, values);

		// close db
		db.close();
	}

	public static void insertQrObjective(ObjectiveQr ob) {
		// Open db
		DbHelper dbhelper = getInstance();
		SQLiteDatabase db = dbhelper.getWritableDatabase();

		// Create values
		ContentValues values = new ContentValues();
		values.put("completedon", ob.getCompletedOn());
		values.put("qid", ob.getQr().getId());
		db.insert(TABLE_QR_OBJECTIVES, null, values);

		// close db
		db.close();
	}

	public static ObjectivePoi getPoiObjective(int id) {
		// Open db
		DbHelper dbhelper = getInstance();
		SQLiteDatabase db = dbhelper.getReadableDatabase();

		// Select query
		Cursor c = db.rawQuery("SELECT t1.id, t1.completedon, t2.id, title, description, info, referencelink, " +
									   "imglink, imglinksmall, lat, long, type " +
									   "FROM " + TABLE_POI_OBJECTIVES + " as t1 " +
									   "INNER JOIN " + TABLE_POI + " as t2 ON t1.pid=t2.id " +
									   "WHERE t2.id=" + id, null);

		ObjectivePoi poi = null;

		// Get the records
		c.moveToFirst();
		if(!c.isAfterLast()) {
			poi = new ObjectivePoi(c.getInt(0), c.getString(1),
								   new Poi(c.getInt(2), c.getString(3), c.getString(4), c.getString(5),
										   c.getString(6), c.getString(7), c.getString(8), c.getDouble(9),
										   c.getDouble(10), c.getInt(11)));
		}

		// close db
		c.close();
		db.close();

		return poi;
	}

	public static void updatePoiObjective(ObjectivePoi p) {
		// Open db
		DbHelper dbhelper = getInstance();
		SQLiteDatabase db = dbhelper.getWritableDatabase();

		// Create values
		ContentValues values = new ContentValues();
		values.put("completedon", p.getCompletedOn());
		db.update(TABLE_POI_OBJECTIVES, values, "id=?", new String[]{String.valueOf(p.getId())});

		// close db
		db.close();
	}

	public static AchievementType getAchType(int points) {
		// Open db
		DbHelper dbhelper = getInstance();
		SQLiteDatabase db = dbhelper.getWritableDatabase();

		// Select query
		Cursor c = db.query(TABLE_ACH_TYPES, null, "reqobjectives=" + points, null, null, null, null);

		// Get the records
		AchievementType type = null;
		c.moveToFirst();
		if(!c.isAfterLast()) type = new AchievementType(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3));

		c.close();
		db.close();

		return type;
	}
}