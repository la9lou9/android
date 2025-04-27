package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "healthcare.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_APPOINTMENTS = "appointments";
    public static final String TABLE_PRESCRIPTIONS = "prescriptions";
    public static final String TABLE_EMERGENCY_CONTACTS = "emergency_contacts";
    public static final String TABLE_MEDICATIONS = "medications";

    // Common Column Names
    public static final String COLUMN_USER_ID = "user_id";

    // Users Table Columns
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // Appointments Table Columns
    public static final String COLUMN_APPOINTMENT_ID = "appointment_id";
    public static final String COLUMN_DOCTOR_NAME = "doctor_name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_DATE = "date";

    // Prescriptions Table Columns
    public static final String COLUMN_PRESCRIPTION_ID = "prescription_id";
    public static final String COLUMN_PRESCRIPTION_DETAILS = "prescription_details";
    public static final String COLUMN_PRESCRIPTION_DATE = "prescription_date";

    // Emergency Contacts Table Columns
    public static final String COLUMN_CONTACT_ID = "contact_id";
    public static final String COLUMN_CONTACT_NAME = "contact_name";
    public static final String COLUMN_CONTACT_PHONE = "contact_phone";

    // Medications Table Columns
    public static final String COLUMN_MEDICATION_ID = "medication_id";
    public static final String COLUMN_MEDICATION_NAME = "medication_name";
    public static final String COLUMN_DOSAGE = "dosage";
    public static final String COLUMN_FREQUENCY = "frequency";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_REMINDER_TIME = "reminder_time";

    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                    COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT)";

    private static final String CREATE_TABLE_APPOINTMENTS =
            "CREATE TABLE " + TABLE_APPOINTMENTS + " (" +
                    COLUMN_APPOINTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_DOCTOR_NAME + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_PHONE_NUMBER + " TEXT, " +
                    COLUMN_DATE + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

    private static final String CREATE_TABLE_PRESCRIPTIONS =
            "CREATE TABLE " + TABLE_PRESCRIPTIONS + " (" +
                    COLUMN_PRESCRIPTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_DOCTOR_NAME + " TEXT, " +
                    COLUMN_PRESCRIPTION_DETAILS + " TEXT, " +
                    COLUMN_PRESCRIPTION_DATE + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

    private static final String CREATE_TABLE_EMERGENCY_CONTACTS =
            "CREATE TABLE " + TABLE_EMERGENCY_CONTACTS + " (" +
                    COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_CONTACT_NAME + " TEXT, " +
                    COLUMN_CONTACT_PHONE + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

    private static final String CREATE_TABLE_MEDICATIONS =
            "CREATE TABLE " + TABLE_MEDICATIONS + " (" +
                    COLUMN_MEDICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_MEDICATION_NAME + " TEXT, " +
                    COLUMN_DOSAGE + " TEXT, " +
                    COLUMN_FREQUENCY + " TEXT, " +
                    COLUMN_START_DATE + " TEXT, " +
                    COLUMN_END_DATE + " TEXT, " +
                    COLUMN_REMINDER_TIME + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_APPOINTMENTS);
        db.execSQL(CREATE_TABLE_PRESCRIPTIONS);
        db.execSQL(CREATE_TABLE_EMERGENCY_CONTACTS);
        db.execSQL(CREATE_TABLE_MEDICATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESCRIPTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMERGENCY_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICATIONS);
        onCreate(db);
    }
}
