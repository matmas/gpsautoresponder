package net.matmas.gpsautoresponder;

import android.content.SharedPreferences;

public class Data {

	private static SharedPreferences getPreferences() {
		return App.getContext().getSharedPreferences("Token", 0);
	}
	
	public static void setToken(String token) {
		SharedPreferences.Editor editor = getPreferences().edit();
		editor.putString("token", token);
		editor.commit();
	}
	
	public static void setPassword(String password) {
		SharedPreferences.Editor editor = getPreferences().edit();
		editor.putString("password", password);
		editor.commit();
	}
	
	public static String getToken() {
		return getPreferences().getString("token", null);
	}
	
	public static String getPassword() {
		return getPreferences().getString("password", null);
	}
	
	public static void deleteTokenAndPassword() {
		SharedPreferences settings = getPreferences();
		SharedPreferences.Editor editor = settings.edit();
		editor.remove("token");
		editor.remove("password");
		editor.commit();
	}
}
