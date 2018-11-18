package dating.innovative.gameshowdating;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceManagerClass {

    static final String PREFERENCE_USERNAME = "username";

    static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUsername(Context context, String username){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREFERENCE_USERNAME, username);
        editor.commit();
    }

    public static String getUsername(Context context){
        return getSharedPreferences(context).getString(PREFERENCE_USERNAME, "");
    }

    public static void clearUsernamePreference(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }

}
