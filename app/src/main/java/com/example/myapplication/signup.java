package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class signup extends AppCompatActivity {

    EditText editFirstName, editLastName, editEmail, editPassword;
    Button buttonSignUp;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        dbHelper = new DatabaseHelper(this);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = editFirstName.getText().toString();
                String lastName = editLastName.getText().toString();
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(signup.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = dbHelper.insertData(firstName, lastName, email, password);
                    if (isInserted) {
                        Toast.makeText(signup.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(signup.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
