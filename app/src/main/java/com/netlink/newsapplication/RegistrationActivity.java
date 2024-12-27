package com.netlink.newsapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.netlink.newsapplication.models.UserModel;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextMobile, editTextPassword;
    private Button buttonRegister;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(v -> registerUser());
    }
    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || mobile.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Collect favorite categories
        Map<String, String> favoriteCategories = new HashMap<>();
        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.buttonToggleGroup);
        // Check if any button is checked
        for (int id : new int[]{R.id.buttonSports, R.id.buttonBusiness, R.id.buttonEntertainment, R.id.buttonAstrology, R.id.buttonGlobal}) {
            MaterialButton button = findViewById(id);
            if (button.isChecked()) {
                favoriteCategories.put(button.getText().toString(), button.getText().toString());
            }
        }

        // Save user data in Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(mobile);
        UserModel user = new UserModel(email, favoriteCategories, mobile, name, password);
        databaseReference.setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                finish(); // Close registration activity
            } else {
                Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                Log.e("Registration", "Error: ", task.getException());
            }
        });
    }
}
