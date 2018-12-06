package dating.innovative.gameshowdating.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import dating.innovative.gameshowdating.model.Feedback;
import dating.innovative.gameshowdating.model.Match;
import dating.innovative.gameshowdating.model.Message;
import dating.innovative.gameshowdating.model.User;

import java.util.ArrayList;
import java.util.UUID;

public class SQLiteHelper extends SQLiteOpenHelper {

    //Singleton variable for best practice
    private static SQLiteHelper sqLiteHelperInstance;

    //DB
    private static final String DATABASE_NAME = "GAMESHOWDATING_CACHEDB";
    private static final int DATABASE_VERSION = 10;

    //tables
    private static final String TABLE_USERS = "users";
    private static final String TABLE_MATCHES = "matches";
    private static final String TABLE_MESSAGES = "messages";
    private static final String TABLE_FEEDBACK = "feedback";

    //coloumns for users
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_PROFILE_PICTURE = "profilePictureURL";
    private static final String KEY_USER_PROFILE_BIOGRAPHY = "biography";
    private static final String KEY_USER_VIDEO_1 = "firstVideoURL";
    private static final String KEY_USER_VIDEO_2 = "secondVideoURL";
    private static final String KEY_USER_VIDEO_3 = "thridVideoURL";
    private static final String KEY_USER_SEX = "sex";
    private static final String KEY_USER_AGE = "age";

    //coloumns for matches
    private static final String KEY_MATCHES_ID = "id";
    private static final String KEY_MATCHES_NAME_ONE = "userOne";
    private static final String KEY_MATCHES_NAME_TWO = "userTwo";

    //coloumns for messaging
    private static final String KEY_MESSAGES_ID = "id";
    private static final String KEY_MESSAGES_NAME_ONE = "userOne";
    private static final String KEY_MESSAGES_NAME_TWO = "userTwo";
    private static final String KEY_MESSAGES_MESSAGE_FROM_NAME_ONE = "userOneMessage";
    private static final String KEY_MESSAGES_MESSAGE_FROM_NAME_TWO = "userTwoMessage";
    private static final String KEY_MESSAGES_MESSAGE_TIMESTAMP = "timestamp";

    //coloumns for text
    private static final String KEY_FEEDBACK_ID = "id";
    private static final String KEY_FEEDBACK_NAME = "feedbackGiverName";
    private static final String KEY_FEEDBACK_FEEDBACK = "feedbackContents";


    //ensure singleton pattern
    public static synchronized SQLiteHelper getSqLiteHelperInstance(Context context) {
        if (sqLiteHelperInstance == null) {
            sqLiteHelperInstance = new SQLiteHelper(context.getApplicationContext());
        }
        return sqLiteHelperInstance;
    }

    //constructor for singleton
    public SQLiteHelper(Context context) {
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
                KEY_USER_VIDEO_3 + " TEXT," +
                KEY_USER_SEX + " TEXT," +
                KEY_USER_AGE + " INTEGER" +
                ")";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);

        String CREATE_MATCHES_TABLE = "CREATE TABLE " + TABLE_MATCHES + "(" +
                KEY_MATCHES_ID + " TEXT PRIMARY KEY," +
                KEY_MATCHES_NAME_ONE + " TEXT NOT NULL, " +
                KEY_MATCHES_NAME_TWO + " TEXT NOT NULL " +
                ")";
        sqLiteDatabase.execSQL(CREATE_MATCHES_TABLE);

        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGES + "(" +
                KEY_MESSAGES_ID + " TEXT PRIMARY KEY," +
                KEY_MESSAGES_NAME_ONE + " TEXT NOT NULL, " +
                KEY_MESSAGES_NAME_TWO + " TEXT NOT NULL, " +
                KEY_MESSAGES_MESSAGE_FROM_NAME_ONE + " TEXT, " +
                KEY_MESSAGES_MESSAGE_FROM_NAME_TWO + " TEXT, " +
                KEY_MESSAGES_MESSAGE_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";
        sqLiteDatabase.execSQL(CREATE_MESSAGES_TABLE);

        String CREATE_FEEDBACK_TABLE = "CREATE TABLE " + TABLE_FEEDBACK + "(" +
                KEY_FEEDBACK_ID + " TEXT PRIMARY KEY," +
                KEY_FEEDBACK_NAME + " TEXT, " +
                KEY_FEEDBACK_FEEDBACK + " TEXT" +
                ")";
        sqLiteDatabase.execSQL(CREATE_FEEDBACK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
            onCreate(sqLiteDatabase);
        }
    }

    public void addFeedback(String name, String feedback) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            String id = UUID.randomUUID().toString();
            ContentValues values = new ContentValues();
            values.put(KEY_FEEDBACK_ID, id);
            values.put(KEY_FEEDBACK_NAME, name);
            values.put(KEY_FEEDBACK_FEEDBACK, feedback);

            db.insertOrThrow(TABLE_FEEDBACK, null, values);
            db.setTransactionSuccessful();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Feedback> getFeedback() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Feedback> feedbackArrayList = new ArrayList<>();
        String GET_FEEDBACK = "SELECT * FROM " + TABLE_FEEDBACK;
        Cursor cursor = db.rawQuery(GET_FEEDBACK, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Feedback feedback = new Feedback();
                    //TODO sqlite
                    feedback.from = cursor.getString(cursor.getColumnIndex(KEY_FEEDBACK_NAME));
                    feedback.text = cursor.getString(cursor.getColumnIndex(KEY_FEEDBACK_FEEDBACK));
                    feedbackArrayList.add(feedback);
                } while (cursor.moveToNext());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return feedbackArrayList;
    }

    public void addMessageToConversationFromSelf(String self, String match, String message) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            String id = UUID.randomUUID().toString();
            ContentValues values = new ContentValues();
            values.put(KEY_MESSAGES_ID, id);
            values.put(KEY_MESSAGES_NAME_ONE, self);
            values.put(KEY_MESSAGES_NAME_TWO, match);
            values.put(KEY_MESSAGES_MESSAGE_FROM_NAME_ONE, message);

            db.insertOrThrow(TABLE_MESSAGES, null, values);
            db.setTransactionSuccessful();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void addMessageToConversationFromMatch(String self, String match, String message) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            String id = UUID.randomUUID().toString();
            ContentValues values = new ContentValues();
            values.put(KEY_MESSAGES_ID, id);
            values.put(KEY_MESSAGES_NAME_ONE, self);
            values.put(KEY_MESSAGES_NAME_TWO, match);
            values.put(KEY_MESSAGES_MESSAGE_FROM_NAME_TWO, message);

            db.insertOrThrow(TABLE_MESSAGES, null, values);
            db.setTransactionSuccessful();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    /*
    helper method for testing
     */
    public void truncateMessages() {
        SQLiteDatabase db = getWritableDatabase();
        String truncate = "DELETE FROM " + TABLE_MESSAGES;
        db.execSQL(truncate);
    }

    public ArrayList<Message> getMessagesForConversation(String self, String match) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Message> messages = new ArrayList<>();
        String GET_MESSAGES_FOR_CONVERSATION =
                "SELECT *" +
                        " FROM " + TABLE_MESSAGES +
                        " WHERE " + KEY_MESSAGES_NAME_ONE + "='" + self + "' AND "
                        + KEY_MATCHES_NAME_TWO + "='" + match + "';";
        Cursor cursor = db.rawQuery(GET_MESSAGES_FOR_CONVERSATION, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Message message = new Message();
                    message.self = cursor.getString(cursor.getColumnIndex(KEY_MESSAGES_NAME_ONE));
                    message.match = cursor.getString(cursor.getColumnIndex(KEY_MESSAGES_NAME_TWO));
                    if (cursor.getString(cursor.getColumnIndex(KEY_MESSAGES_MESSAGE_FROM_NAME_ONE)) != null) {
                        message.messageFromSelf = cursor.getString(cursor.getColumnIndex(KEY_MESSAGES_MESSAGE_FROM_NAME_ONE));
                    } else {
                        message.messageFromMatch = cursor.getString(cursor.getColumnIndex(KEY_MESSAGES_MESSAGE_FROM_NAME_TWO));
                    }
                    message.timestamp = cursor.getString(cursor.getColumnIndex(KEY_MESSAGES_MESSAGE_TIMESTAMP));
                    messages.add(message);
                } while (cursor.moveToNext());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return messages;
    }

    public void addMatch(String self, String match) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            String id = UUID.randomUUID().toString();
            ContentValues values = new ContentValues();
            values.put(KEY_MATCHES_ID, id);
            values.put(KEY_MATCHES_NAME_ONE, self);
            values.put(KEY_MATCHES_NAME_TWO, match);

            db.insertOrThrow(TABLE_MATCHES, null, values);
            db.setTransactionSuccessful();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Match> getAllMatchesForUser(String username) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Match> matches = new ArrayList<>();
        String GET_MATCHES_FOR_LOGGED_IN_USER_QUERY =
                "SELECT * " +
                        " FROM " + TABLE_MATCHES +
                        " WHERE " + KEY_MATCHES_NAME_ONE + "='" + username + "';";

        Cursor cursor = db.rawQuery(GET_MATCHES_FOR_LOGGED_IN_USER_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Match match = new Match();
                    match.nameOne = cursor.getString(cursor.getColumnIndex(KEY_MATCHES_NAME_ONE));
                    match.nameTwo = cursor.getString(cursor.getColumnIndex(KEY_MATCHES_NAME_TWO));
                    matches.add(match);
                } while (cursor.moveToNext());
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        if (matches.size() > 0) {
            return matches;
        } else {
            return null;
        }
    }

    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            String id = UUID.randomUUID().toString();
            ContentValues values = new ContentValues();
            values.put(KEY_USER_ID, id);
            values.put(KEY_USER_NAME, user.username);
            values.put(KEY_USER_PASSWORD, user.password);
            values.put(KEY_USER_PROFILE_PICTURE, user.profileImage);
            values.put(KEY_USER_PROFILE_BIOGRAPHY, user.biography);
            values.put(KEY_USER_VIDEO_1, user.video1);
            values.put(KEY_USER_VIDEO_2, user.video2);
            values.put(KEY_USER_VIDEO_3, user.video3);
            values.put(KEY_USER_SEX, user.sex);
            values.put(KEY_USER_AGE, user.age);

            db.insertOrThrow(TABLE_USERS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();

        String USERNAME_GET_QUERY = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USER_NAME + "='" + username + "';";
        User user = new User();

        Cursor cursor = db.rawQuery(USERNAME_GET_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    user.username = cursor.getString(cursor.getColumnIndex(KEY_USER_NAME));
                    user.password = cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD));
                    user.biography = cursor.getString(cursor.getColumnIndex(KEY_USER_PROFILE_BIOGRAPHY));
                    user.profileImage = cursor.getString(cursor.getColumnIndex(KEY_USER_PROFILE_PICTURE));
                    user.video1 = cursor.getString(cursor.getColumnIndex(KEY_USER_VIDEO_1));
                    user.video2 = cursor.getString(cursor.getColumnIndex(KEY_USER_VIDEO_2));
                    user.video3 = cursor.getString(cursor.getColumnIndex(KEY_USER_VIDEO_3));
                    user.sex = cursor.getString(cursor.getColumnIndex(KEY_USER_SEX));
                    user.age = cursor.getInt(cursor.getColumnIndex(KEY_USER_AGE));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        if (user.username != null) {
            return user;
        } else {
            return null;
        }

    }

    public int updateUserBiography(User user, String biography) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_PROFILE_BIOGRAPHY, biography);
        return db.update(TABLE_USERS, contentValues, KEY_USER_NAME + " = ?", new String[]{String.valueOf(user.username)});
    }

    public int updateUserProfileImage(User user, String profileImageURL) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_PROFILE_PICTURE, profileImageURL);
        return db.update(TABLE_USERS, contentValues, KEY_USER_NAME + " = ?", new String[]{String.valueOf(user.username)});
    }

    public int updateUserVideo1(User user, String video1URL) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_VIDEO_1, video1URL);
        return db.update(TABLE_USERS, contentValues, KEY_USER_NAME + " = ?", new String[]{String.valueOf(user.username)});
    }

    public int updateUserVideo2(User user, String video2URL) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_VIDEO_2, video2URL);
        return db.update(TABLE_USERS, contentValues, KEY_USER_NAME + " = ?", new String[]{String.valueOf(user.username)});
    }

    public int updateUserVideo3(User user, String video3URL) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_VIDEO_3, video3URL);
        return db.update(TABLE_USERS, contentValues, KEY_USER_NAME + " = ?", new String[]{String.valueOf(user.username)});
    }


}
