package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsDAO {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;

    public AppointmentsDAO(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long insert(Appointment appointment) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_ID,this.getUserIdByEmail(this.getLoggedInEmail()));
        values.put(DatabaseHelper.COLUMN_DOCTOR_NAME, appointment.getDoctorName());
        values.put(DatabaseHelper.COLUMN_ADDRESS, appointment.getAddress());
        values.put(DatabaseHelper.COLUMN_PHONE_NUMBER, appointment.getPhoneNumber());
        values.put(DatabaseHelper.COLUMN_DATE, appointment.getDate());
        return database.insert(DatabaseHelper.TABLE_APPOINTMENTS, null, values);
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String loggedInEmail = getLoggedInEmail();

        // Get the user ID from the email
        long userId = getUserIdByEmail(loggedInEmail);

        // Query to get appointments for the current user
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_APPOINTMENTS +
                " WHERE " + DatabaseHelper.COLUMN_USER_ID + " = ?";

        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Appointment appointment = new Appointment(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DOCTOR_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE))
                );
                appointment.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_APPOINTMENT_ID)));
                appointments.add(appointment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return appointments;
    }

    private String getLoggedInEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }

    private long getUserIdByEmail(String email) {
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
