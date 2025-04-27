package com.example.myapplication.Prescription;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class PrescriptionListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPrescriptions;
    private PrescriptionAdapter prescriptionAdapter;
    private PrescriptionDAO prescriptionDAO;
    private FloatingActionButton buttonAddPrescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);

        recyclerViewPrescriptions = findViewById(R.id.recyclerViewPrescriptions);
        buttonAddPrescription = findViewById(R.id.buttonAddPrescription);

        prescriptionDAO = new PrescriptionDAO(this);

        // Set up RecyclerView
        recyclerViewPrescriptions.setLayoutManager(new LinearLayoutManager(this));

        // Get prescriptions for the current user
        long userId = getCurrentUserId();
        List<Prescription> prescriptions = prescriptionDAO.getAllPrescriptionsForUser(userId);
        prescriptionAdapter = new PrescriptionAdapter(prescriptions);
        recyclerViewPrescriptions.setAdapter(prescriptionAdapter);

        buttonAddPrescription.setOnClickListener(v -> openAddPrescriptionDialog());
    }

    private void openAddPrescriptionDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_prescription, null);

        EditText editTextPrescriptionDetails = dialogView.findViewById(R.id.editTextPrescriptionDetails);
        EditText editTextPrescriptionDate = dialogView.findViewById(R.id.editTextPrescriptionDate);
        EditText editTextDoctorName = dialogView.findViewById(R.id.editTextDoctorName);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView)
                .setTitle("Add Prescription")
                .setPositiveButton("Add", (dialog, which) -> {
                    String details = editTextPrescriptionDetails.getText().toString();
                    String date = editTextPrescriptionDate.getText().toString();
                    String doctorName = editTextDoctorName.getText().toString();

                    long userId = getCurrentUserId();

                    Prescription prescription = new Prescription(userId, doctorName, details, date);
                    prescriptionDAO.insert(prescription);

                    // Update the RecyclerView with the new prescription
                    prescriptionAdapter.getPrescriptions().add(prescription);
                    prescriptionAdapter.notifyItemInserted(prescriptionAdapter.getItemCount() - 1);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private long getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        return prescriptionDAO.getUserIdByEmail(email);
    }


}
