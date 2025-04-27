package com.example.myapplication.Medication;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Calendar;
import java.util.List;

public class MedicationListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMedications;
    private MedicationAdapter medicationAdapter;
    private MedicationDAO medicationDAO;
    private FloatingActionButton buttonAddMedication;
    private EditText editTextStartDate;
    private EditText editTextEndDate;
    private EditText editTextReminderTime;

    private static final int REQUEST_SCHEDULE_EXACT_ALARM = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, REQUEST_SCHEDULE_EXACT_ALARM);
            }
        }

        recyclerViewMedications = findViewById(R.id.recyclerViewMedications);
        buttonAddMedication = findViewById(R.id.buttonAddMedication);

        medicationDAO = new MedicationDAO(this);

        // Set up RecyclerView
        recyclerViewMedications.setLayoutManager(new LinearLayoutManager(this));

        // Get medications for the current user
        long userId = getCurrentUserId();
        List<Medication> medications = medicationDAO.getAllMedicationsForUser(userId);
        medicationAdapter = new MedicationAdapter(medications);
        recyclerViewMedications.setAdapter(medicationAdapter);

        buttonAddMedication.setOnClickListener(v -> openAddMedicationDialog());
    }

    private void openAddMedicationDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_medication, null);

        EditText editTextMedicationName = dialogView.findViewById(R.id.editTextMedicationName);
        EditText editTextDosage = dialogView.findViewById(R.id.editTextDosage);
        EditText editTextFrequency = dialogView.findViewById(R.id.editTextFrequency);
        editTextStartDate = dialogView.findViewById(R.id.editTextStartDate);
        editTextEndDate = dialogView.findViewById(R.id.editTextEndDate);
        editTextReminderTime = dialogView.findViewById(R.id.editTextReminderTime);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView)
                .setTitle("Add Medication")
                .setPositiveButton("Add", (dialog, which) -> {
                    String medicationName = editTextMedicationName.getText().toString();
                    String dosage = editTextDosage.getText().toString();
                    String frequency = editTextFrequency.getText().toString();
                    String startDate = editTextStartDate.getText().toString();
                    String endDate = editTextEndDate.getText().toString();
                    String reminderTime = editTextReminderTime.getText().toString();

                    long userId = getCurrentUserId();

                    Medication medication = new Medication(userId, medicationName, dosage, frequency, startDate, endDate, reminderTime);
                    medicationDAO.insert(medication);

                    // Update the RecyclerView with the new medication
                    medicationAdapter.getMedications().add(medication);
                    medicationAdapter.notifyItemInserted(medicationAdapter.getItemCount() - 1);

                    // Schedule the reminder
                    scheduleReminder(reminderTime);
                })
                .setNegativeButton("Cancel", null)
                .show();

        // Set click listeners for the date and time fields
        editTextStartDate.setOnClickListener(v -> showDatePickerDialog(editTextStartDate));
        editTextEndDate.setOnClickListener(v -> showDatePickerDialog(editTextEndDate));
        editTextReminderTime.setOnClickListener(v -> showTimePickerDialog(editTextReminderTime));
    }

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String selectedDate = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year);
            editText.setText(selectedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showTimePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
            editText.setText(selectedTime);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }

    private void scheduleReminder(String reminderTime) {
        String[] timeParts = reminderTime.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MedicationReminderReceiver.class);

        // Use FLAG_IMMUTABLE or FLAG_MUTABLE based on your requirement
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        try{
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }catch (SecurityException e)
        {
            e.printStackTrace();
        }
    }

    private long getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        return medicationDAO.getUserIdByEmail(email);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SCHEDULE_EXACT_ALARM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with setting the alarm
            } else {
                // Permission denied, handle the case (e.g., show a message to the user)
            }
        }
    }


}
