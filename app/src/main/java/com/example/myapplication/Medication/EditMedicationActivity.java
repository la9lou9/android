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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditMedicationActivity extends AppCompatActivity {

    private EditText etMedicationName, etDosage, etFrequency;
    private Button btnStartDate, btnEndDate, btnReminderTime, btnUpdateMedication,  btnDeleteMedication;

    private Calendar startDateCalendar, endDateCalendar, reminderTimeCalendar;
    private SimpleDateFormat dateFormatter, timeFormatter;
    private MedicationDAO medicationDAO;
    private long medicationId;
    private Medication medication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medication);

        // Initialize DAO
        medicationDAO = new MedicationDAO(this);

        // Get medication ID from intent
        medicationId = getIntent().getLongExtra("MEDICATION_ID", -1);
        if (medicationId == -1) {
            Toast.makeText(this, "Medication not found", Toast.LENGTH_SHORT).show();
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
        etMedicationName = findViewById(R.id.etEditMedicationName);
        etDosage = findViewById(R.id.etEditDosage);
        etFrequency = findViewById(R.id.etEditFrequency);
        btnStartDate = findViewById(R.id.btnEditStartDate);
        btnEndDate = findViewById(R.id.btnEditEndDate);
        btnReminderTime = findViewById(R.id.btnEditReminderTime);
        btnUpdateMedication = findViewById(R.id.btnUpdateMedication);
        btnDeleteMedication = findViewById(R.id.btnDeleteMedication);

        // Set click listeners
        btnStartDate.setOnClickListener(v -> showDatePicker(startDateCalendar, btnStartDate));
        btnEndDate.setOnClickListener(v -> showDatePicker(endDateCalendar, btnEndDate));
        btnReminderTime.setOnClickListener(v -> showTimePicker());
        btnUpdateMedication.setOnClickListener(v -> updateMedication());
        btnDeleteMedication.setOnClickListener(v -> deleteMedication());

        // Load medication data
        loadMedicationData();
    }

    private void loadMedicationData() {
        medication = medicationDAO.getMedicationById(medicationId);

        if (medication != null) {
            etMedicationName.setText(medication.getMedicationName());
            etDosage.setText(medication.getDosage());
            etFrequency.setText(medication.getFrequency());
            btnStartDate.setText(medication.getStartDate());
            btnEndDate.setText(medication.getEndDate());
            btnReminderTime.setText(medication.getReminderTime());

            // Set calendar values
            try {
                Date startDate = dateFormatter.parse(medication.getStartDate());
                Date endDate = dateFormatter.parse(medication.getEndDate());
                Date reminderTime = timeFormatter.parse(medication.getReminderTime());

                if (startDate != null) {
                    startDateCalendar.setTime(startDate);
                }
                if (endDate != null) {
                    endDateCalendar.setTime(endDate);
                }
                if (reminderTime != null) {
                    Calendar tempCal = Calendar.getInstance();
                    tempCal.setTime(reminderTime);
                    reminderTimeCalendar.set(Calendar.HOUR_OF_DAY, tempCal.get(Calendar.HOUR_OF_DAY));
                    reminderTimeCalendar.set(Calendar.MINUTE, tempCal.get(Calendar.MINUTE));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Failed to load medication data", Toast.LENGTH_SHORT).show();
            finish();
        }
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

    private void updateMedication() {
        // Validate fields
        String medicationName = etMedicationName.getText().toString().trim();
        String dosage = etDosage.getText().toString().trim();
        String frequency = etFrequency.getText().toString().trim();
        String startDate = btnStartDate.getText().toString();
        String endDate = btnEndDate.getText().toString();
        String reminderTime = btnReminderTime.getText().toString();

        if (medicationName.isEmpty() || dosage.isEmpty() || frequency.isEmpty() ||
                startDate.isEmpty() || endDate.isEmpty() || reminderTime.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update medication object
        medication.setMedicationName(medicationName);
        medication.setDosage(dosage);
        medication.setFrequency(frequency);
        medication.setStartDate(startDate);
        medication.setEndDate(endDate);
        medication.setReminderTime(reminderTime);

        // Save to database using DAO
        int result = medicationDAO.updateMedication(medication);

        if (result > 0) {
            // Cancel old alarm and set a new one
            updateMedicationReminder();
            Toast.makeText(this, "Medication updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update medication", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteMedication() {
        int result = medicationDAO.deleteMedication(medicationId);
        if (result > 0) {
            Toast.makeText(this, "Médicament supprimé", Toast.LENGTH_SHORT).show();
            finish(); // Ferme l'activité
        } else {
            Toast.makeText(this, "Échec de la suppression", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateMedicationReminder() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Cancel existing alarm
        Intent intent = new Intent(this, MedicationReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (int) medicationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        // Create new intent for the updated reminder
        Intent newIntent = new Intent(this, MedicationReminderReceiver.class);
        newIntent.putExtra("MEDICATION_ID", medicationId);
        newIntent.putExtra("MEDICATION_NAME", medication.getMedicationName());
        newIntent.putExtra("DOSAGE", medication.getDosage());

        // Create new pending intent
        PendingIntent newPendingIntent = PendingIntent.getBroadcast(
                this,
                (int) medicationId,
                newIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Set the alarm to trigger at the updated reminder time
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
                    newPendingIntent
            );
        }
    }
}