package jp.ac.ehime_u.cite.sasaki.easyguide.util;

public class Log {
	static public void v(Throwable t, String message) {
		String file_name = t.getStackTrace()[0].getFileName();
		int line_numer = t.getStackTrace()[0].getLineNumber();
		String class_name = t.getStackTrace()[0].getClassName();
		String method = t.getStackTrace()[0].getMethodName();
		String simple_name = t.getStackTrace()[0].getClass().getSimpleName();
		String atline = "at " + class_name + "." + method + "(" + file_name
				+ ":" + line_numer + ")";
		android.util.Log.v(simple_name, atline);
		android.util.Log.v(simple_name, "   " + message);
	}
}
