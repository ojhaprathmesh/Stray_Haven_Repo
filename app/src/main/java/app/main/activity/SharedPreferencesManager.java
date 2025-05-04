package app.main.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility class to manage user session data using SharedPreferences
 */
public class SharedPreferencesManager {

    // SharedPreferences file name
    private static final String PREF_NAME = "StrayHavenPrefs";

    // SharedPreferences keys
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ID = "userId";

    /**
     * Get the SharedPreferences instance
     */
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Save the login status
     */
    public static void setLoggedIn(Context context, boolean isLoggedIn) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    /**
     * Check if user is logged in
     */
    public static boolean isLoggedIn(Context context) {
        return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Save user email
     */
    public static void setUserEmail(Context context, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    /**
     * Get stored user email
     */
    public static String getUserEmail(Context context) {
        return getSharedPreferences(context).getString(KEY_USER_EMAIL, "");
    }

    /**
     * Save user name
     */
    public static void setUserName(Context context, String name) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    /**
     * Get stored user name
     */
    public static String getUserName(Context context) {
        return getSharedPreferences(context).getString(KEY_USER_NAME, "");
    }

    /**
     * Save user ID
     */
    public static void setUserId(Context context, String userId) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    /**
     * Get stored user ID
     */
    public static String getUserId(Context context) {
        return getSharedPreferences(context).getString(KEY_USER_ID, "");
    }

    /**
     * Clear all user data from SharedPreferences (logout)
     */
    public static void clearUserData(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }
}
