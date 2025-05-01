package com.example.myapplication.Prescription;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public PrescriptionDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long insert(Prescription prescription) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_ID, prescription.getUserId());
        values.put(DatabaseHelper.COLUMN_DOCTOR_NAME, prescription.getDoctorName());
        values.put(DatabaseHelper.COLUMN_PRESCRIPTION_DETAILS, prescription.getDetails());
        values.put(DatabaseHelper.COLUMN_PRESCRIPTION_DATE, prescription.getDate());
        return database.insert(DatabaseHelper.TABLE_PRESCRIPTIONS, null, values);
    }

    public List<Prescription> getAllPrescriptionsForUser(long userId) {
        List<Prescription> prescriptions = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_PRESCRIPTIONS +
                " WHERE " + DatabaseHelper.COLUMN_USER_ID + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Prescription prescription = new Prescription(
                        cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DOCTOR_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRESCRIPTION_DETAILS)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRESCRIPTION_DATE))
                );
                prescription.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRESCRIPTION_ID)));
                prescriptions.add(prescription);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return prescriptions;
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
