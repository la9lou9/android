package com.example.myapplication.Medication;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);

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
        EditText editTextStartDate = dialogView.findViewById(R.id.editTextStartDate);
        EditText editTextEndDate = dialogView.findViewById(R.id.editTextEndDate);
        EditText editTextReminderTime = dialogView.findViewById(R.id.editTextReminderTime);

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

                    // Schedule the reminder
                    scheduleReminder(reminderTime);

                    // Update the RecyclerView with the new medication
                    medicationAdapter.getMedications().add(medication);
                    medicationAdapter.notifyItemInserted(medicationAdapter.getItemCount() - 1);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
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
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private long getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        return medicationDAO.getUserIdByEmail(email);
    }


}
