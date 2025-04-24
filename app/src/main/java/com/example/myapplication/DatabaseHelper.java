package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "app";
    public static final String USER = "User";
    public static final String USERID = "ID";
    public static final String USERFIRSTNAME = "FIRSTNAME";
    public static final String USERLASTNAME = "LASTNAME";
    public static final String USEREMAIL = "EMAIL";
    public static final String USERPASSWORD = "PASSWORD";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + USER + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "FIRSTNAME TEXT, LASTNAME TEXT, EMAIL TEXT, PASSWORD TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER);
        onCreate(db);
    }

    public boolean insertData(String firstName, String lastName, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERFIRSTNAME, firstName);
        contentValues.put(USERLASTNAME, lastName);
        contentValues.put(USEREMAIL, email);
        contentValues.put(USERPASSWORD, password);
        long result = db.insert(USER, null, contentValues);
        return result != -1;
    }
}
