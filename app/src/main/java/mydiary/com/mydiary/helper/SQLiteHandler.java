package mydiary.com.mydiary.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "my_diary_app";

    // Login table name
    private static final String TABLE_USER = "user";
    // Story table name
    private static final String TABLE_STORY = "story";
    // Image table name
    private static final String TABLE_IMAGE = "story_image";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    // Story Table Columns Names
    private static  final  String STORY_KEY_ID = "id";
    private static  final  String STORY_KEY_USER_ID = "user_id";
    private static  final  String STORY_KEY_TITLE = "title";
    private static  final  String STORY_KEY_DESCRIPTION = "description";
    private static  final  String STORY_KEY_FAVOURITE = "favourite";
    private static  final  String STORY_KEY_CREATED_AT = "created_at";


    // Story Image Table Columns Names
    private static  final  String IMAGE_KEY_ID = "id";
    private static  final  String IMAGE_KEY_STORY_ID = "story_id";
    private static  final  String IMAGE_KEY_USER_ID = "user_id";
    private static  final  String IMAGE_KEY_IMAEGPATH = "image_path";
    private static  final  String IMAGE_KEY_CREATED_AT = "created_at";

    // Story Image Table Columns Names

    public SQLiteHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create users table
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";

        //create story table
        String CREATE_STORY_TABLE = "CREATE TABLE " + TABLE_STORY + "("
                + STORY_KEY_ID + " INTEGER PRIMARY KEY," + STORY_KEY_TITLE + " TEXT,"
                + STORY_KEY_DESCRIPTION + " TEXT," + STORY_KEY_FAVOURITE + " BOOLEAN,"
                + STORY_KEY_CREATED_AT + " TEXT" + ")";

        //create image table
        String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_IMAGE + "("
                + IMAGE_KEY_ID + " INTEGER PRIMARY KEY," + IMAGE_KEY_STORY_ID + " INTEGER,"
                + IMAGE_KEY_USER_ID + " INTEGER," + IMAGE_KEY_IMAEGPATH + " TEXT,"
                + IMAGE_KEY_CREATED_AT + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_STORY_TABLE);
        db.execSQL(CREATE_IMAGE_TABLE);

        Log.d(TAG, "Database tables created");
    }

    /************** USER ******************/

    public void addUser(String name, String email, String uid, String created_at) {
        //call db as :..
        SQLiteDatabase db = this.getWritableDatabase();

        //all of inserted data are done by Content Value
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_UID, uid);
        values.put(KEY_CREATED_AT, created_at);

        //db insert
        Long id = db.insert(TABLE_USER, null, values);
        db.close();

        Log.d(TAG, "New user is inserted" + id);

    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }


    /************** STORY *******************/
    public void addStory(Integer user_id, String title, String descp, Boolean favourite, String date) {
        ContentValues values = new ContentValues();
        values.put(STORY_KEY_USER_ID, user_id);
        values.put(STORY_KEY_TITLE, title);
        values.put(STORY_KEY_DESCRIPTION, descp);
        values.put(STORY_KEY_FAVOURITE, favourite);
        values.put(STORY_KEY_CREATED_AT, date);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_STORY, null, values);
        db.close();

        Log.d(TAG, "New story is inserted");
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
