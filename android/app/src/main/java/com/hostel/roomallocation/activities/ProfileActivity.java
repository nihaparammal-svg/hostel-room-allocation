package com.hostel.roomallocation.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hostel.roomallocation.R;
import com.hostel.roomallocation.utils.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    private ImageView btnProfileBack;
    private TextView tvProfileName, tvProfileStudentId, tvProfileAvatarInitials;
    private EditText etEditProfileName, etEditProfileEmail, etEditProfilePhone;
    private Button btnSaveProfile;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);

        initViews();
        loadProfileData();
        setupListeners();
    }

    private void initViews() {
        btnProfileBack = findViewById(R.id.btnProfileBack);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileStudentId = findViewById(R.id.tvProfileStudentId);
        tvProfileAvatarInitials = findViewById(R.id.tvProfileAvatarInitials);

        etEditProfileName = findViewById(R.id.etEditProfileName);
        etEditProfileEmail = findViewById(R.id.etEditProfileEmail);
        etEditProfilePhone = findViewById(R.id.etEditProfilePhone);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
    }

    private void loadProfileData() {
        // DYNAMIC STUDENT DATA
        String dynamicName = sessionManager.getStudentName();
        String studentId = sessionManager.getStudentId();
        String email = sessionManager.getStudentEmail();
        String phone = sessionManager.getStudentPhone();
        String initials = sessionManager.getStudentInitials();

        tvProfileName.setText(dynamicName);
        tvProfileStudentId.setText("ID: " + studentId);
        tvProfileAvatarInitials.setText(initials);

        etEditProfileName.setText(dynamicName);
        etEditProfileEmail.setText(email);
        etEditProfilePhone.setText(phone);
    }

    private void setupListeners() {
        btnProfileBack.setOnClickListener(v -> finish());

        btnSaveProfile.setOnClickListener(v -> {
            String newName = etEditProfileName.getText() != null ? etEditProfileName.getText().toString().trim() : "";
            String newEmail = etEditProfileEmail.getText() != null ? etEditProfileEmail.getText().toString().trim() : "";
            String newPhone = etEditProfilePhone.getText() != null ? etEditProfilePhone.getText().toString().trim() : "";

            if (newName.isEmpty()) {
                etEditProfileName.setError("Name cannot be empty");
                etEditProfileName.requestFocus();
                return;
            }

            if (newEmail.isEmpty()) {
                etEditProfileEmail.setError("Email cannot be empty");
                etEditProfileEmail.requestFocus();
                return;
            }

            // Save to session dynamically
            sessionManager.setStudentName(newName);
            sessionManager.setStudentEmail(newEmail);
            sessionManager.setStudentPhone(newPhone);

            // Update ID card live
            tvProfileName.setText(newName);
            tvProfileAvatarInitials.setText(sessionManager.getStudentInitials());

            Toast.makeText(this, "Profile saved! Dynamic name updated across the app.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
