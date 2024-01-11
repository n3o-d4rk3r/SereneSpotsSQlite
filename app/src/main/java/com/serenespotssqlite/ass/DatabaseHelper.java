package com.serenespotssqlite.ass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "rani.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE_PATH = "image_path";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADMISSION_NO = "admission_no";
    public static final String COLUMN_TEXT = "text";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_IMAGE_PATH + " TEXT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_ADMISSION_NO + " TEXT, " +
                    COLUMN_TEXT + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_PATH, user.getImagePath());
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_ADMISSION_NO, user.getAdmissionNo());
        values.put(COLUMN_TEXT, user.getText());
        return db.insert(TABLE_NAME, null, values);
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                user.setImagePath(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH)));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                user.setAdmissionNo(cursor.getString(cursor.getColumnIndex(COLUMN_ADMISSION_NO)));
                user.setText(cursor.getString(cursor.getColumnIndex(COLUMN_TEXT)));
                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return userList;
    }
    // Add this method to your DatabaseHelper class
    public void updateImagePath(int userId, String imagePath) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_PATH, imagePath);

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }
}
