package jp.ac.ehime_u.cite.sasaki.easyguide.util;

public class Log {
	static public void v(Throwable t, String message) {
		StackTraceElement top_of_stack = t.getStackTrace()[0];
		String file_name = top_of_stack.getFileName();
		int line_numer = top_of_stack.getLineNumber();
		String class_name = top_of_stack.getClassName();
		String method = top_of_stack.getMethodName();
		String simple_name = top_of_stack.getClass().getSimpleName();
		String atline = "at " + class_name + "." + method + "(" + file_name
				+ ":" + line_numer + ")";
		android.util.Log.v(simple_name, atline);
		android.util.Log.v(simple_name, "   " + message);
	}
}
