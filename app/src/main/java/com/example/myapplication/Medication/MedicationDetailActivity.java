package com.example.myapplication.Medication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MedicationDetailActivity extends AppCompatActivity {

    private TextView tvMedicationName, tvDosage, tvFrequency, tvStartDate, tvEndDate, tvReminderTime;
    private Button btnEditMedication;
    private long medicationId;
    private MedicationDAO medicationDAO;
    private Medication medication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_detail);

        // Initialize DAO
        medicationDAO = new MedicationDAO(this);

        // Get medication ID from intent
        medicationId = getIntent().getLongExtra("MEDICATION_ID", -1);
        if (medicationId == -1) {
            Toast.makeText(this, "Medication not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Find views
        tvMedicationName = findViewById(R.id.tvMedicationNameDetail);
        tvDosage = findViewById(R.id.tvDosageDetail);
        tvFrequency = findViewById(R.id.tvFrequencyDetail);
        tvStartDate = findViewById(R.id.tvStartDateDetail);
        tvEndDate = findViewById(R.id.tvEndDateDetail);
        tvReminderTime = findViewById(R.id.tvReminderTimeDetail);
        btnEditMedication = findViewById(R.id.btnEditMedication);

        // Set click listener for edit button
        btnEditMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicationDetailActivity.this, EditMedicationActivity.class);
                intent.putExtra("MEDICATION_ID", medicationId);
                startActivity(intent);
            }
        });

        // Load medication details
        loadMedicationDetails();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload medication details when returning to this activity
        loadMedicationDetails();
    }

    private void loadMedicationDetails() {
        medication = medicationDAO.getMedicationById(medicationId);

        if (medication != null) {
            tvMedicationName.setText(medication.getMedicationName());
            tvDosage.setText("Dosage: " + medication.getDosage());
            tvFrequency.setText("Frequency: " + medication.getFrequency());
            tvStartDate.setText("Start Date: " + medication.getStartDate());
            tvEndDate.setText("End Date: " + medication.getEndDate());
            tvReminderTime.setText("Reminder Time: " + medication.getReminderTime());
        } else {
            Toast.makeText(this, "Medication not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_medication_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_medication) {
            confirmDeleteMedication();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmDeleteMedication() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Medication")
                .setMessage("Are you sure you want to delete this medication and its reminders?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMedication();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteMedication() {
        // Cancel the alarm
        cancelMedicationReminder();

        // Delete from database
        int result = medicationDAO.deleteMedication(medicationId);

        if (result > 0) {
            Toast.makeText(this, "Medication deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to delete medication", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelMedicationReminder() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

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
    }
}