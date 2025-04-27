package com.example.myapplication.Medication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MedicationDAO {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public MedicationDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long insert(Medication medication) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_ID, medication.getUserId());
        values.put(DatabaseHelper.COLUMN_MEDICATION_NAME, medication.getMedicationName());
        values.put(DatabaseHelper.COLUMN_DOSAGE, medication.getDosage());
        values.put(DatabaseHelper.COLUMN_FREQUENCY, medication.getFrequency());
        values.put(DatabaseHelper.COLUMN_START_DATE, medication.getStartDate());
        values.put(DatabaseHelper.COLUMN_END_DATE, medication.getEndDate());
        values.put(DatabaseHelper.COLUMN_REMINDER_TIME, medication.getReminderTime());
        return database.insert(DatabaseHelper.TABLE_MEDICATIONS, null, values);
    }

    public List<Medication> getAllMedicationsForUser(long userId) {
        List<Medication> medications = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_MEDICATIONS +
                " WHERE " + DatabaseHelper.COLUMN_USER_ID + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Medication medication = new Medication(
                        cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MEDICATION_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DOSAGE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FREQUENCY)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_START_DATE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_END_DATE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_REMINDER_TIME))
                );
                medication.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_MEDICATION_ID)));
                medications.add(medication);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return medications;
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
