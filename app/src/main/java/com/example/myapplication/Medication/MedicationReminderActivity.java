package com.example.myapplication.Medication;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MedicationReminderActivity extends AppCompatActivity {

    private EditText etMedicationName, etDosage, etFrequency;
    private Button btnStartDate, btnEndDate, btnReminderTime, btnSaveMedication;
    private Calendar startDateCalendar, endDateCalendar, reminderTimeCalendar;
    private SimpleDateFormat dateFormatter, timeFormatter;
    private MedicationDAO medicationDAO;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_reminder);

        // Initialize DAO
        medicationDAO = new MedicationDAO(this);

        // Get userId from intent
        userId = getIntent().getLongExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize formatters
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);

        // Initialize calendars
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
        reminderTimeCalendar = Calendar.getInstance();

        // Find views
        etMedicationName = findViewById(R.id.etMedicationName);
        etDosage = findViewById(R.id.etDosage);
        etFrequency = findViewById(R.id.etFrequency);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);
        btnReminderTime = findViewById(R.id.btnReminderTime);
        btnSaveMedication = findViewById(R.id.btnSaveMedication);

        // Set default text for buttons
        btnStartDate.setText("Select Start Date");
        btnEndDate.setText("Select End Date");
        btnReminderTime.setText("Set Reminder Time");

        // Set click listeners
        btnStartDate.setOnClickListener(v -> showDatePicker(startDateCalendar, btnStartDate));
        btnEndDate.setOnClickListener(v -> showDatePicker(endDateCalendar, btnEndDate));
        btnReminderTime.setOnClickListener(v -> showTimePicker());
        btnSaveMedication.setOnClickListener(v -> saveMedication());
    }

    private void showDatePicker(final Calendar calendar, final Button button) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                button.setText(dateFormatter.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                reminderTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                reminderTimeCalendar.set(Calendar.MINUTE, minute);
                btnReminderTime.setText(timeFormatter.format(reminderTimeCalendar.getTime()));
            }
        }, reminderTimeCalendar.get(Calendar.HOUR_OF_DAY), reminderTimeCalendar.get(Calendar.MINUTE), true).show();
    }

    private void saveMedication() {
        // Validate fields
        String medicationName = etMedicationName.getText().toString().trim();
        String dosage = etDosage.getText().toString().trim();
        String frequency = etFrequency.getText().toString().trim();
        String startDate = btnStartDate.getText().toString();
        String endDate = btnEndDate.getText().toString();
        String reminderTime = btnReminderTime.getText().toString();

        if (medicationName.isEmpty() || dosage.isEmpty() || frequency.isEmpty() ||
                startDate.equals("Select Start Date") || endDate.equals("Select End Date") ||
                reminderTime.equals("Set Reminder Time")) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create medication object
        Medication medication = new Medication();
        medication.setUserId(userId);
        medication.setMedicationName(medicationName);
        medication.setDosage(dosage);
        medication.setFrequency(frequency);
        medication.setStartDate(startDate);
        medication.setEndDate(endDate);
        medication.setReminderTime(reminderTime);

        // Save to database using DAO
        long medicationId = medicationDAO.insertMedication(medication);

        if (medicationId != -1) {
            // Set alarm for medication reminder
            scheduleReminder(medicationId, medicationName, dosage);
            Toast.makeText(this, "Medication saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save medication", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleReminder(long medicationId, String medicationName, String dosage) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create intent for the receiver
        Intent intent = new Intent(this, MedicationReminderReceiver.class);
        intent.putExtra("MEDICATION_ID", medicationId);
        intent.putExtra("MEDICATION_NAME", medicationName);
        intent.putExtra("DOSAGE", dosage);

        // Create unique request code based on medication ID
        int requestCode = (int) medicationId;

        // Create pending intent that will trigger when alarm goes off
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Set the alarm to trigger at the specified reminder time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, reminderTimeCalendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, reminderTimeCalendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, 0);

        // If time is already passed, schedule for tomorrow
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Schedule alarm as a repeating alarm (daily)
        if (alarmManager != null) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        }
    }
}
