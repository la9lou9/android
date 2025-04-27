package com.example.myapplication.Medication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MedicationDAO {
    private DatabaseHelper dbHelper;

    public MedicationDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Insert a new medication
    public long insertMedication(Medication medication) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_USER_ID, medication.getUserId());
        values.put(DatabaseHelper.COLUMN_MEDICATION_NAME, medication.getMedicationName());
        values.put(DatabaseHelper.COLUMN_DOSAGE, medication.getDosage());
        values.put(DatabaseHelper.COLUMN_FREQUENCY, medication.getFrequency());
        values.put(DatabaseHelper.COLUMN_START_DATE, medication.getStartDate());
        values.put(DatabaseHelper.COLUMN_END_DATE, medication.getEndDate());
        values.put(DatabaseHelper.COLUMN_REMINDER_TIME, medication.getReminderTime());

        long id = db.insert(DatabaseHelper.TABLE_MEDICATIONS, null, values);
        db.close();
        return id;
    }

    // Get all medications for a user
    public List<Medication> getMedicationsForUser(long userId) {
        List<Medication> medicationList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                DatabaseHelper.COLUMN_MEDICATION_ID,
                DatabaseHelper.COLUMN_USER_ID,
                DatabaseHelper.COLUMN_MEDICATION_NAME,
                DatabaseHelper.COLUMN_DOSAGE,
                DatabaseHelper.COLUMN_FREQUENCY,
                DatabaseHelper.COLUMN_START_DATE,
                DatabaseHelper.COLUMN_END_DATE,
                DatabaseHelper.COLUMN_REMINDER_TIME
        };

        String selection = DatabaseHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_MEDICATIONS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Medication medication = new Medication();
                medication.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEDICATION_ID)));
                medication.setUserId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
                medication.setMedicationName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEDICATION_NAME)));
                medication.setDosage(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DOSAGE)));
                medication.setFrequency(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FREQUENCY)));
                medication.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_START_DATE)));
                medication.setEndDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_END_DATE)));
                medication.setReminderTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REMINDER_TIME)));

                medicationList.add(medication);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return medicationList;
    }

    // Get a specific medication by ID
    public Medication getMedicationById(long medicationId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                DatabaseHelper.COLUMN_MEDICATION_ID,
                DatabaseHelper.COLUMN_USER_ID,
                DatabaseHelper.COLUMN_MEDICATION_NAME,
                DatabaseHelper.COLUMN_DOSAGE,
                DatabaseHelper.COLUMN_FREQUENCY,
                DatabaseHelper.COLUMN_START_DATE,
                DatabaseHelper.COLUMN_END_DATE,
                DatabaseHelper.COLUMN_REMINDER_TIME
        };

        String selection = DatabaseHelper.COLUMN_MEDICATION_ID + " = ?";
        String[] selectionArgs = { String.valueOf(medicationId) };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_MEDICATIONS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Medication medication = null;

        if (cursor.moveToFirst()) {
            medication = new Medication();
            medication.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEDICATION_ID)));
            medication.setUserId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
            medication.setMedicationName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEDICATION_NAME)));
            medication.setDosage(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DOSAGE)));
            medication.setFrequency(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FREQUENCY)));
            medication.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_START_DATE)));
            medication.setEndDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_END_DATE)));
            medication.setReminderTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REMINDER_TIME)));
        }

        cursor.close();
        db.close();
        return medication;
    }

    // Update a medication
    public int updateMedication(Medication medication) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_MEDICATION_NAME, medication.getMedicationName());
        values.put(DatabaseHelper.COLUMN_DOSAGE, medication.getDosage());
        values.put(DatabaseHelper.COLUMN_FREQUENCY, medication.getFrequency());
        values.put(DatabaseHelper.COLUMN_START_DATE, medication.getStartDate());
        values.put(DatabaseHelper.COLUMN_END_DATE, medication.getEndDate());
        values.put(DatabaseHelper.COLUMN_REMINDER_TIME, medication.getReminderTime());

        String whereClause = DatabaseHelper.COLUMN_MEDICATION_ID + " = ?";
        String[] whereArgs = { String.valueOf(medication.getId()) };

        int result = db.update(DatabaseHelper.TABLE_MEDICATIONS, values, whereClause, whereArgs);
        db.close();
        return result;
    }

    // Delete a medication
    public int deleteMedication(long medicationId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.COLUMN_MEDICATION_ID + " = ?";
        String[] whereArgs = { String.valueOf(medicationId) };

        int result = db.delete(DatabaseHelper.TABLE_MEDICATIONS, whereClause, whereArgs);
        db.close();
        return result;
    }

    public long getUserIdByEmail(String email) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

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