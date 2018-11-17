package dating.innovative.gameshowdating;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.UUID;

public class SQLiteHelper extends SQLiteOpenHelper {

    //Singleton variable for best practice
    private static SQLiteHelper sqLiteHelperInstance;

    //DB
    private static final String DATABASE_NAME = "GAMESHOWDATING_CACHEDB";
    private static final int DATABASE_VERSION = 3;

    //table
    private static final String TABLE_USERS = "users";

    //coloumns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_PROFILE_PICTURE = "profilePictureURL";
    private static final String KEY_USER_PROFILE_BIOGRAPHY = "biography";
    private static final String KEY_USER_VIDEO_1 = "firstVideoURL";
    private static final String KEY_USER_VIDEO_2 = "secondVideoURL";
    private static final String KEY_USER_VIDEO_3 = "thridVideoURL";

    //ensure singleton pattern
    public static synchronized SQLiteHelper getSqLiteHelperInstance(Context context){
        if(sqLiteHelperInstance == null){
            sqLiteHelperInstance = new SQLiteHelper(context.getApplicationContext());
        }
        return sqLiteHelperInstance;
    }

    //constructor for singleton
    public SQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //constructor from superclass
    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //constructor for superclass with errorhandler
    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, errorHandler);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "(" +
                KEY_USER_ID + " TEXT PRIMARY KEY," +
                KEY_USER_NAME + " TEXT," +
                KEY_USER_PASSWORD + " TEXT, " +
                KEY_USER_PROFILE_PICTURE + " TEXT," +
                KEY_USER_PROFILE_BIOGRAPHY + " TEXT," +
                KEY_USER_VIDEO_1 + " TEXT," +
                KEY_USER_VIDEO_2 + " TEXT," +
                KEY_USER_VIDEO_3 + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(sqLiteDatabase);
        }
    }


    /*
    CREATE UPDATE METHODS FOR EACH OF THESE VALUES TO UPDATE INDIVIDUALLY
     */
    public void addUser(User user){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try{
            String id = UUID.randomUUID().toString();
            ContentValues values = new ContentValues();
            values.put(KEY_USER_ID,id);
            values.put(KEY_USER_NAME, user.username);
            values.put(KEY_USER_PASSWORD, user.password);
            values.put(KEY_USER_PROFILE_PICTURE, user.profileImage);
            values.put(KEY_USER_PROFILE_BIOGRAPHY, user.biography);
            values.put(KEY_USER_VIDEO_1, user.video1);
            values.put(KEY_USER_VIDEO_2, user.video2);
            values.put(KEY_USER_VIDEO_3, user.video3);

            db.insertOrThrow(TABLE_USERS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception exception){
            exception.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public User getUserByUsername(String username){
        SQLiteDatabase db = getReadableDatabase();

        String USERNAME_GET_QUERY = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USER_NAME + "='" + username + "';";
        User user = new User();

        Cursor cursor = db.rawQuery(USERNAME_GET_QUERY, null);
        try {
            if(cursor.moveToFirst()){
                do {
                    user.username = cursor.getString(cursor.getColumnIndex(KEY_USER_NAME));
                    user.password = cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD));
                    user.biography = cursor.getString(cursor.getColumnIndex(KEY_USER_PROFILE_BIOGRAPHY));
                    user.profileImage = cursor.getString(cursor.getColumnIndex(KEY_USER_PROFILE_PICTURE));
                    user.video1 = cursor.getString(cursor.getColumnIndex(KEY_USER_VIDEO_1));
                    user.video2 = cursor.getString(cursor.getColumnIndex(KEY_USER_VIDEO_2));
                    user.video3 = cursor.getString(cursor.getColumnIndex(KEY_USER_VIDEO_3));
                } while(cursor.moveToNext());
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        if(user.username != null){
            return user;
        } else {
            return null;
        }

    }
}
