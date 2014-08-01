
package io.yunba.yo.util;



import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;


public class SharePrefsHelper {
	private static SharedPreferences _pushPref = null;
	private static final String TAG = "SharePrefsHelper";
	private static final String SERVER_CONFIG = "com.yunba.yo.SharePrefs";

	public static String getString(Context context,String key, String defValue) {
		getDefaultSharedPreferences(context);
		String retStr= _pushPref.getString(key, defValue);
		return retStr;
	}

	public static void setString(Context context,String key,String value) {
		SharedPreferences.Editor editor = getDefaultSharedPreferences(context).edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static int getInt(Context context,String key, int value) {
		getDefaultSharedPreferences(context);
		int retInt = _pushPref.getInt(key, value);
		return retInt;
	   
	}
	public static void setInt(Context context,String key,int value) {
		SharedPreferences.Editor editor = getDefaultSharedPreferences(context).edit();
		editor.putInt(key, value);
		editor.commit();
	}
	

	
	public static long getLong(Context context,String key, long value) {
		getDefaultSharedPreferences(context);
		long retIong = _pushPref.getLong(key, value);
		return retIong;
	   
	}
	public static void setLong(Context context,String key, long value) {
		SharedPreferences.Editor editor = getDefaultSharedPreferences(context).edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	public static void setSet(Context context,String key, Set<String> set) {
		SharedPreferences.Editor editor = getDefaultSharedPreferences(context).edit();
		editor.putStringSet(key, set);
		editor.commit();
	}
	
	public static Set<String>  getSet(Context context,String key, Set<String> set) {
		getDefaultSharedPreferences(context);
		return _pushPref.getStringSet(key, null);
		
	}
	
	private static SharedPreferences getDefaultSharedPreferences(Context context) {
		if(null==_pushPref)
		_pushPref = context.getSharedPreferences(SERVER_CONFIG,
				Context.MODE_PRIVATE);
		return _pushPref;
	}


}
