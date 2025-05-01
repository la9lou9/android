package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.Appointment.AppointmentActivity;
import com.example.myapplication.Askme.ChatbotActivity;
import com.example.myapplication.Emergency.EmergencyContactsActivity;
import com.example.myapplication.Medication.MedicationListActivity;
import com.example.myapplication.Onboarding.OnboardingActivity;
import com.example.myapplication.Prescription.PrescriptionListActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPreferences = getSharedPreferences("onboarding_pref", MODE_PRIVATE);
        boolean isFirstLaunch = !sharedPreferences.getBoolean("is_completed", false);

        if (isFirstLaunch) {
            // Direct to onboarding screen
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CardView cardView1 = findViewById(R.id.buttonRendezVous);
        CardView cardView2 = findViewById(R.id.buttonpill);
        CardView cardView3 = findViewById(R.id.buttonemergency);
        CardView cardView4 = findViewById(R.id.buttonpresreption);
        CardView cardView5 = findViewById(R.id.buttonchatbot);
        CardView cardView6 = findViewById(R.id.buttoncalendar);



        cardView1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AppointmentActivity.class);
            startActivity(intent);
        });
        try{
        cardView2.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, MedicationListActivity.class);
            startActivity(intent);
        });
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        cardView3.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MainActivity.this, EmergencyContactsActivity.class);
                startActivity(intent);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        });

        cardView4.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PrescriptionListActivity.class);
            startActivity(intent);
        });

        cardView5.setOnClickListener(v -> {

                Intent intent = new Intent(MainActivity.this, ChatbotActivity.class);
                startActivity(intent);

        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();





        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_account_info) {
                    showAccountInfo();
                } else if (id == R.id.nav_account_security) {
                    showAccountSecurity();
                } else if (id == R.id.nav_logout) {
                    logout();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    private void onCardClick(int cardNumber) {
        String message = "Card " + cardNumber + " clicked!";
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        // Implement navigation or action for each card click
    }

    private void showAccountInfo() {
        Toast.makeText(this, "Account Information Selected", Toast.LENGTH_SHORT).show();
        // Implement navigation to account information screen
    }

    private void showAccountSecurity() {
        Toast.makeText(this, "Account Security Selected", Toast.LENGTH_SHORT).show();
        // Implement navigation to account security screen
    }
    public void logout () {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Navigate to the LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}




