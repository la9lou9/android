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
        values.put(DatabaseHelper.USERFIRSTNAME, firstname);
        values.put(DatabaseHelper.USERLASTNAME, lastname);
        values.put(DatabaseHelper.USEREMAIL, email);
        values.put(DatabaseHelper.USERPASSWORD, password);
        return database.insert(DatabaseHelper.USER, null, values);
    }

    public Cursor getUserById(long id) {
        String[] columns = {DatabaseHelper.USERID, DatabaseHelper.USERFIRSTNAME, DatabaseHelper.USERLASTNAME, DatabaseHelper.USEREMAIL, DatabaseHelper.USERPASSWORD};
        String selection = DatabaseHelper.USERID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return database.query(DatabaseHelper.USER, columns, selection, selectionArgs, null, null, null);
    }

    public Cursor getAllUsers() {
        String[] columns = {DatabaseHelper.USERID, DatabaseHelper.USERFIRSTNAME, DatabaseHelper.USERLASTNAME, DatabaseHelper.USEREMAIL, DatabaseHelper.USERPASSWORD};
        return database.query(DatabaseHelper.USER, columns, null, null, null, null, null);
    }

    public int updateUser(long id, String firstname, String lastname, String email, String password) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.USERFIRSTNAME, firstname);
        values.put(DatabaseHelper.USERLASTNAME, lastname);
        values.put(DatabaseHelper.USEREMAIL, email);
        values.put(DatabaseHelper.USERPASSWORD, password);
        String selection = DatabaseHelper.USERID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return database.update(DatabaseHelper.USER, values, selection, selectionArgs);
    }

    public int deleteUser(long id) {
        String selection = DatabaseHelper.USERID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return database.delete(DatabaseHelper.USER, selection, selectionArgs);
    }

    public Cursor getUserByEmail(String email) {
        String selection = DatabaseHelper.USEREMAIL + " = ?";
        String[] selectionArgs = {email};
        return database.query(
                DatabaseHelper.USER,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }
    

}
