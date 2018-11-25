package dating.innovative.gameshowdating.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceManagerClass {

    public static final String PREFERENCE_USERNAME = "username";
    public static final String PREFERENCE_PROFILE_PICTURE = "profile_picture";
    public static final String PREFERENCE_VIDEO_1 = "video1_url";
    public static final String PREFERENCE_VIDEO_2 = "video2_url";
    public static final String PREFERENCE_VIDEO_3 = "video3_url";

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

    public static void clearPreferences(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }

    public static void setPreferenceProfilePictureUpdated(Context context, String profilePictureURL){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREFERENCE_PROFILE_PICTURE, profilePictureURL);
        editor.commit();
    }

    public static String getProfilePictureUpdated(Context context){
        return getSharedPreferences(context).getString(PREFERENCE_PROFILE_PICTURE, "");
    }

    public static void clearRef(Context context, String ref){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(ref);
        editor.commit();
    }

    public static void setPreferenceVideo1(Context context, String video1URL){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREFERENCE_VIDEO_1, video1URL);
        editor.commit();
    }

    public static String getPreferenceVideo1(Context context){
        return getSharedPreferences(context).getString(PREFERENCE_VIDEO_1, "");
    }

    public static void setPreferenceVideo2(Context context, String video2URL){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREFERENCE_VIDEO_2, video2URL);
        editor.commit();
    }

    public static String getPreferenceVideo2(Context context){
        return getSharedPreferences(context).getString(PREFERENCE_VIDEO_2, "");
    }

    public static void setPreferenceVideo3(Context context, String video3URL){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREFERENCE_VIDEO_3, video3URL);
        editor.commit();
    }

    public static String getPreferenceVideo3(Context context){
        return getSharedPreferences(context).getString(PREFERENCE_VIDEO_3, "");
    }
}
