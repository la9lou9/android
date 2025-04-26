package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class EmergencyContactDAO {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;

    public EmergencyContactDAO(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long insert(EmergencyContact contact) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_ID, contact.getUserId());
        values.put(DatabaseHelper.COLUMN_CONTACT_NAME, contact.getName());
        values.put(DatabaseHelper.COLUMN_CONTACT_PHONE, contact.getPhone());
        return database.insert(DatabaseHelper.TABLE_EMERGENCY_CONTACTS, null, values);
    }

    public List<EmergencyContact> getAllContactsForUser(long userId) {
        List<EmergencyContact> contacts = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_EMERGENCY_CONTACTS +
                " WHERE " + DatabaseHelper.COLUMN_USER_ID + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                EmergencyContact contact = new EmergencyContact(
                        cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_PHONE))
                );
                contact.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_ID)));
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contacts;
    }

    public int deleteContact(long contactId) {
        String selection = DatabaseHelper.COLUMN_CONTACT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(contactId)};
        return database.delete(DatabaseHelper.TABLE_EMERGENCY_CONTACTS, selection, selectionArgs);
    }

    public long getUserIdByEmail(String email) {
        long userId = -1;
        String query = "SELECT " + DatabaseHelper.COLUMN_USER_ID + " FROM " + DatabaseHelper.TABLE_USERS +
                " WHERE " + DatabaseHelper.COLUMN_EMAIL + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID));
        }
        cursor.close();
        return userId;
    }
}
