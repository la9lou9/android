package com.example.myapplication.Emergency;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.content.Intent;

public class EmergencyContactsActivity extends AppCompatActivity implements EmergencyContactAdapter.OnContactInteractionListener {

    private RecyclerView recyclerView;
    private EmergencyContactAdapter adapter;
    private EmergencyContactDAO contactDAO;
    private FloatingActionButton buttonAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        recyclerView = findViewById(R.id.recyclerViewContacts);
        buttonAddContact = findViewById(R.id.buttonAddContact);

        contactDAO = new EmergencyContactDAO(this);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get contacts for the current user
        long userId = getCurrentUserId();
        List<EmergencyContact> contacts = contactDAO.getAllContactsForUser(userId);
        adapter = new EmergencyContactAdapter(contacts, this);
        recyclerView.setAdapter(adapter);

        buttonAddContact.setOnClickListener(v -> openAddContactDialog());
    }

    private long getCurrentUserId() {
        // Retrieve the current user's ID from SharedPreferences or another session manager
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        return contactDAO.getUserIdByEmail(email);
    }

    private void openAddContactDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_contact, null);

        EditText editTextContactName = dialogView.findViewById(R.id.editTextContactName);
        EditText editTextContactPhone = dialogView.findViewById(R.id.editTextContactPhone);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView)
                .setTitle("Add Emergency Contact")
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = editTextContactName.getText().toString();
                    String phone = editTextContactPhone.getText().toString();

                    long userId = getCurrentUserId();
                    EmergencyContact contact = new EmergencyContact(userId, name, phone);
                    contactDAO.insert(contact);

                    // Update the RecyclerView with the new contact
                    adapter.addContact(contact);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    @Override
    public void onCallClick(EmergencyContact contact) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + contact.getPhone()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
