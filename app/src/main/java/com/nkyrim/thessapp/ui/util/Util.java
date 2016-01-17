package com.nkyrim.thessapp.ui.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Util {

	public static String formatedTimestamp(long millis) {
		DateFormat df = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm:ss", Locale.getDefault());
		df.setTimeZone(TimeZone.getTimeZone("EET"));
		return df.format(millis);
	}
}
