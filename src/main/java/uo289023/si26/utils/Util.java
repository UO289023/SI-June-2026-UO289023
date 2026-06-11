package uo289023.si26.utils;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	private Util() {
		throw new IllegalStateException("Utility class");
	}

	public static Date isoStringToDate(String isoDateString) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(isoDateString);
		} catch (ParseException e) {
			throw new ApplicationException("Invalid ISO date format: " + isoDateString);
		}
	}

	public static String dateToIsoString(Date javaDate) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(javaDate);
	}
}
