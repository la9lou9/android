package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class UserDAO {
    private static UserDAO instance;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public static synchronized UserDAO getInstance(Context context) {
        if (instance == null) {
            instance = new UserDAO(context.getApplicationContext());
        }
        return instance;
    }

    public long insertUser(String firstname, String lastname, String email, String password) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FIRST_NAME, firstname);
        values.put(DatabaseHelper.COLUMN_LAST_NAME, lastname);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        return database.insert(DatabaseHelper.TABLE_USERS, null, values);
    }

    public Cursor getUserById(long id) {
        String[] columns = {DatabaseHelper.COLUMN_USER_ID, DatabaseHelper.COLUMN_FIRST_NAME, DatabaseHelper.COLUMN_LAST_NAME, DatabaseHelper.COLUMN_EMAIL, DatabaseHelper.COLUMN_PASSWORD};
        String selection = DatabaseHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return database.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);
    }

    public Cursor getAllUsers() {
        String[] columns = {DatabaseHelper.COLUMN_USER_ID, DatabaseHelper.COLUMN_FIRST_NAME, DatabaseHelper.COLUMN_LAST_NAME, DatabaseHelper.COLUMN_EMAIL, DatabaseHelper.COLUMN_PASSWORD};
        return database.query(DatabaseHelper.TABLE_USERS, columns, null, null, null, null, null);
    }

    public int updateUser(long id, String firstname, String lastname, String email, String password) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FIRST_NAME, firstname);
        values.put(DatabaseHelper.COLUMN_LAST_NAME, lastname);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        String selection = DatabaseHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return database.update(DatabaseHelper.TABLE_USERS, values, selection, selectionArgs);
    }

    public int deleteUser(long id) {
        String selection = DatabaseHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return database.delete(DatabaseHelper.TABLE_USERS, selection, selectionArgs);
    }

    public Cursor getUserByEmail(String email) {
        String selection = DatabaseHelper.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        return database.query(
                DatabaseHelper.TABLE_USERS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }
    

}
