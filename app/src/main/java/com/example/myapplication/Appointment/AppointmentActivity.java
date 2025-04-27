package com.example.myapplication.Appointment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AppointmentAdapter appointmentAdapter;
    private AppointmentsDAO appointmentDAO;
    private Button buttonAddAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize DAO
        appointmentDAO = new AppointmentsDAO(this);

        // Load appointments
        List<Appointment> appointments = appointmentDAO.getAllAppointments();
        appointmentAdapter = new AppointmentAdapter(appointments);
        recyclerView.setAdapter(appointmentAdapter);

        // Initialize Button
        buttonAddAppointment = findViewById(R.id.buttonAddAppointment);
        buttonAddAppointment.setOnClickListener(v -> showAddAppointmentDialog());

        // Handle navigation view item clicks if needed
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            // Handle navigation item clicks here
            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void showAddAppointmentDialog() {
        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_appointment, null);
        EditText editTextDoctorName = dialogView.findViewById(R.id.editTextDoctorName);
        EditText editTextAddress = dialogView.findViewById(R.id.editTextAddress);
        EditText editTextPhoneNumber = dialogView.findViewById(R.id.editTextPhoneNumber);
        EditText editTextDate = dialogView.findViewById(R.id.editTextDate);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView)
                .setTitle("Add Appointment")
                .setPositiveButton("Add", (dialog, which) -> {
                    String doctorName = editTextDoctorName.getText().toString();
                    String address = editTextAddress.getText().toString();
                    String phoneNumber = editTextPhoneNumber.getText().toString();
                    String date = editTextDate.getText().toString();

                    Appointment appointment = new Appointment(doctorName, address, phoneNumber, date);
                    addAppointment(appointment);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addAppointment(Appointment appointment) {
        long id = appointmentDAO.insert(appointment);
        appointment.setId(id);
        appointmentAdapter.addAppointment(appointment);
    }
}
