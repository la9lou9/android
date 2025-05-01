package com.example.myapplication.Medication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MedicationListActivity extends AppCompatActivity {

    private MedicationDAO medicationDAO;
    private long userId;
    private ListView medicationListView;
    private FloatingActionButton fabAddMedication;
    private MedicationAdapter medicationAdapter;
    private List<Medication> medicationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);

        medicationDAO = new MedicationDAO(this);

        // Get userId from intent
        userId = getCurrentUserId();
        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        medicationListView = findViewById(R.id.medicationListView);
        fabAddMedication = findViewById(R.id.fabAddMedication);

        fabAddMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicationListActivity.this, MedicationReminderActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        // Set click listener for list items
        medicationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Medication selectedMedication = medicationList.get(position);

                // You can open a detail view or edit page for the selected medication
                Toast.makeText(MedicationListActivity.this,
                        "Selected: " + selectedMedication.getMedicationName(),
                        Toast.LENGTH_SHORT).show();

                // Example: Open edit activity
                 Intent intent = new Intent(MedicationListActivity.this, EditMedicationActivity.class);
                 intent.putExtra("MEDICATION_ID", selectedMedication.getId());
                 startActivity(intent);
            }
        });

        // Load medications when activity is created
        loadMedications();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload medications when returning to this activity
        loadMedications();
    }

    private void loadMedications() {
        // Get medications from database
        medicationList = medicationDAO.getMedicationsForUser(userId);

        // Check if we have any medications
        if (medicationList.isEmpty()) {
            Toast.makeText(this, "No medications found. Add one!", Toast.LENGTH_SHORT).show();
        }

        // Create and set adapter
        medicationAdapter = new MedicationAdapter(this, medicationList);
        medicationListView.setAdapter(medicationAdapter);
    }

    private long getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        return medicationDAO.getUserIdByEmail(email);
    }
}

